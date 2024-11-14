package dev.lugami.tails.network;

import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.network.event.ReceiveStaffChatEvent;
import dev.lugami.tails.network.packet.PacketAddGrant;
import dev.lugami.tails.network.packet.PacketBroadcastPunishment;
import dev.lugami.tails.network.packet.PacketDeleteGrant;
import dev.lugami.tails.network.packet.PacketDeleteRank;
import dev.lugami.tails.network.packet.PacketRefreshRank;
import dev.lugami.tails.network.packet.PacketStaffChat;
import dev.lugami.tails.network.packet.PacketStaffJoinNetwork;
import dev.lugami.tails.network.packet.PacketStaffLeaveNetwork;
import dev.lugami.tails.network.packet.PacketStaffReport;
import dev.lugami.tails.network.packet.PacketStaffRequest;
import dev.lugami.tails.network.packet.PacketStaffSwitchServer;
import dev.lugami.tails.network.packet.PacketClearPunishments;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.grant.Grant;
import dev.lugami.tails.profile.grant.event.GrantAppliedEvent;
import dev.lugami.tails.profile.grant.event.GrantExpireEvent;
import dev.lugami.tails.profile.punishment.Punishment;
import dev.lugami.tails.rank.Rank;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NetworkPacketListener implements PacketListener {

	private Tails tails;

	public NetworkPacketListener(Tails tails) {
		this.tails = tails;
	}

	@IncomingPacketHandler
	public void onAddGrant(PacketAddGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantAppliedEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onDeleteGrant(PacketDeleteGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantExpireEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
		Punishment punishment = packet.getPunishment();
		punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent());

		Player player = Bukkit.getPlayer(packet.getTargetUuid());

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
			profile.getPunishments().add(punishment);

			if (punishment.getType().isBan() && !punishment.isRemoved() && !punishment.hasExpired()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.kickPlayer(punishment.getKickMessage());
					}
				}.runTask(Tails.get());
			}
		}
	}

	@IncomingPacketHandler
	public void onRankRefresh(PacketRefreshRank packet) {
		Rank rank = Rank.getRankByUuid(packet.getUuid());

		if (rank == null) {
			rank = new Rank(packet.getUuid(), packet.getName());
		}

		rank.load();

		Tails.broadcastOps("&8[&eNetwork&8] &fRefreshed rank " + rank.getDisplayName());
	}

	@IncomingPacketHandler
	public void onRankDelete(PacketDeleteRank packet) {
		Rank rank = Rank.getRanks().remove(packet.getUuid());

		if (rank != null) {
			Tails.broadcastOps("&8[&eNetwork&8] &fDeleted rank " + rank.getDisplayName() );
		}
	}

	@IncomingPacketHandler
	public void onStaffChat(PacketStaffChat packet) {
		tails.getServer().getOnlinePlayers().stream()
		    .filter(onlinePlayer -> onlinePlayer.hasPermission("tails.staff"))
		    .forEach(onlinePlayer -> {
			    ReceiveStaffChatEvent event = new ReceiveStaffChatEvent(onlinePlayer);

			    tails.getServer().getPluginManager().callEvent(event);

			    if (!event.isCancelled()) {
			    	Profile profile = Profile.getProfiles().get(event.getPlayer().getUniqueId());

			    	if (profile != null && profile.getStaffOptions().staffModeEnabled()) {
					    onlinePlayer.sendMessage(Locale.STAFF_CHAT.format(packet.getPlayerName(), packet.getServerName(),
							    packet.getChatMessage()));
				    }
			    }
		    });
	}

	@IncomingPacketHandler
	public void onStaffJoinNetwork(PacketStaffJoinNetwork packet) {
		tails.getServer().broadcast(Locale.STAFF_JOIN_NETWORK.format(packet.getPlayerName(), packet.getServerName()),
				"tails.staff");
	}

	@IncomingPacketHandler
	public void onStaffLeaveNetwork(PacketStaffLeaveNetwork packet) {
		tails.getServer().broadcast(Locale.STAFF_LEAVE_NETWORK.format(packet.getPlayerName()), "tails.staff");
	}

	@IncomingPacketHandler
	public void onStaffSwitchServer(PacketStaffSwitchServer packet) {
		tails.getServer().broadcast(Locale.STAFF_SWITCH_SERVER.format(packet.getPlayerName(), packet.getToServerName(),
				packet.getFromServerName()), "tails.staff");
	}

	@IncomingPacketHandler
	public void onStaffReport(PacketStaffReport packet) {
		List<String> messages = Locale.STAFF_REPORT_BROADCAST.formatLines(packet.getSentBy(), packet.getAccused(),
				packet.getReason(), packet.getServerId(), packet.getServerName());

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("tails.staff")) {
				Profile profile = Profile.getByUuid(player.getUniqueId());

				if (profile.getStaffOptions().staffModeEnabled()) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	@IncomingPacketHandler
	public void onStaffRequest(PacketStaffRequest packet) {
		List<String> messages = Locale.STAFF_REQUEST_BROADCAST.formatLines(packet.getSentBy(), packet.getReason(),
				packet.getServerId(), packet.getServerName());

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("tails.staff")) {
				Profile profile = Profile.getByUuid(player.getUniqueId());

				if (profile.getStaffOptions().staffModeEnabled()) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	@IncomingPacketHandler
	public void onClearGrants(PacketClearPunishments packet) {
		Player player = Bukkit.getPlayer(packet.getUuid());

		if (player != null) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getGrants().clear();
		}
	}

	@IncomingPacketHandler
	public void onClearPunishments(PacketClearPunishments packet) {
		Player player = Bukkit.getPlayer(packet.getUuid());

		if (player != null) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getPunishments().clear();
		}
	}

}
