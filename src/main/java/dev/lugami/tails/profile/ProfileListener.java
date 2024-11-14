package dev.lugami.tails.profile;

import com.mojang.authlib.GameProfile;
import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.cache.RedisPlayerData;
import dev.lugami.tails.handler.PacketHandler;
import dev.lugami.tails.network.packet.PacketStaffChat;
import dev.lugami.tails.profile.punishment.Punishment;
import dev.lugami.tails.profile.punishment.PunishmentType;
import dev.lugami.tails.util.CC;
import java.util.List;
import java.util.NoSuchElementException;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileListener implements Listener {

	private static Tails plugin = Tails.get();

	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		Player player = Bukkit.getPlayer(event.getUniqueId());

		// Need to check if player is still logged in when receiving another login attempt
		// This happens when a player using a custom client can access the server list while in-game (and reconnecting)
		if (player != null && player.isOnline()) {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(CC.RED + "You tried to login too quickly after disconnecting.\nTry again in a few seconds.");
			plugin.getServer().getScheduler().runTask(plugin, () -> player.kickPlayer(CC.RED + "Duplicate login kick"));
			return;
		}

		Profile profile = null;

		try {
			profile = new Profile(event.getName(), event.getUniqueId());

			if (!profile.isLoaded()) {
				event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
				event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
				return;
			}

			if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
				handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN));
				return;
			}

			for (Profile profile1 : Profile.getByIpAddress(event.getAddress().getHostAddress())) {
				if (profile1.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
					handleBan(event, profile.getActivePunishmentByType(PunishmentType.BLACKLIST));
					return;
				}
			}

			if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
				handleBan(event, profile.getActivePunishmentByType(PunishmentType.BLACKLIST));
				return;
			}

			profile.setName(event.getName());

			if (profile.getFirstSeen() == null) {
				profile.setFirstSeen(System.currentTimeMillis());
			}

			profile.setLastSeen(System.currentTimeMillis());

			if (profile.getCurrentAddress() == null) {
				profile.setCurrentAddress(event.getAddress().getHostAddress());
			}

			if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
				profile.getIpAddresses().add(event.getAddress().getHostAddress());
			}

			if (!profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
				List<Profile> alts = Profile.getByIpAddress(event.getAddress().getHostAddress());

				for (Profile alt : alts) {
					if (alt.getActivePunishmentByType(PunishmentType.BAN) != null) {
						handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN));
						return;
					}
				}
			}

			profile.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (profile == null || !profile.isLoaded()) {
			event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			return;
		}

		Profile.getProfiles().put(profile.getUuid(), profile);

		RedisPlayerData playerData = new RedisPlayerData(event.getUniqueId(), event.getName());
		playerData.setLastAction(RedisPlayerData.LastAction.JOINING_SERVER);
		playerData.setLastSeenServer(Bukkit.getServerId());
		playerData.setLastSeenAt(System.currentTimeMillis());

		plugin.getRedisCache().updatePlayerData(playerData);
		plugin.getRedisCache().updateNameAndUUID(event.getName(), event.getUniqueId());
	}

	@EventHandler(
			priority = EventPriority.LOWEST
	)
	public void onBreakEvent(BlockBreakEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
		if (profile.isFrozen()) {
			event.setCancelled(true);
		}
	}

	@EventHandler(
			priority = EventPriority.LOWEST
	)
	public void onDamageEvent(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Profile profile = Profile.getByUuid(event.getEntity().getUniqueId());
			if (profile.isFrozen()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(
			priority = EventPriority.LOWEST
	)
	public void onPlayerMove(PlayerMoveEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
		if (profile.isFrozen()) {
			event.getPlayer().teleport(event.getPlayer());
			event.getPlayer().sendMessage(CC.translate(""));
			event.getPlayer().sendMessage(CC.translate("&cYou were frozen by a staff member."));
			event.getPlayer().sendMessage(CC.translate("&cYou have 5 minutes to join our Discord."));
			event.getPlayer().sendMessage(CC.translate("&chttps://discord.pots.lol"));
			event.getPlayer().sendMessage(CC.translate(""));
		} else {
			event.setCancelled(false);
		}
	}

	@EventHandler(
			priority = EventPriority.LOWEST
	)
	public void onBlockPlace(BlockPlaceEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
		if (profile.isFrozen()) {
			event.setCancelled(true);
		}
	}

	@EventHandler(
			priority = EventPriority.HIGHEST
	)
	public void onQuitEvent(PlayerQuitEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
		if (profile.isFrozen()) {
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban -s " + profile.getPlayer().getName() + " 30d Logged out during SS.");
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.setupBukkitPlayer(player);

		if (player.hasPermission("plugin..staff")) {
			player.sendMessage(CC.GOLD + "Your staff mode is currently: " +
					(profile.getStaffOptions().staffModeEnabled() ? CC.GREEN + "Enabled" : CC.RED + "Disabled"));
		}
		EntityPlayer ent = ((CraftPlayer) event.getPlayer()).getHandle();
		if (ent.playerConnection.networkManager.channel != null) {
			try {
				ent.playerConnection.networkManager.channel.pipeline().addBefore("packet_handler",
						"gay_packet_handler", new PacketHandler(event.getPlayer()));
			} catch (NoSuchElementException unused) {
				// do nothing, player was instantly disconnected
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Profile profile = Profile.getProfiles().remove(event.getPlayer().getUniqueId());
		profile.setLastSeen(System.currentTimeMillis());

		if (profile.isLoaded()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					profile.save();
				}
			}.runTaskAsynchronously(Tails.get());
		}

		RedisPlayerData playerData = new RedisPlayerData(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		playerData.setLastAction(RedisPlayerData.LastAction.LEAVING_SERVER);
		playerData.setLastSeenServer(Bukkit.getServerId());
		playerData.setLastSeenAt(System.currentTimeMillis());

		plugin.getRedisCache().updatePlayerData(playerData);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

		if (profile.getStaffOptions().staffChatModeEnabled()) {
			if (profile.getStaffOptions().staffModeEnabled()) {
				Tails.get().getPidgin().sendPacket(new PacketStaffChat(
						TailsAPI.getColoredName(event.getPlayer()), Bukkit.getServerId(), event.getMessage()));
			} else {
				event.getPlayer().sendMessage(CC.RED + "You must enable staff mode or disable staff chat mode.");
			}

			event.setCancelled(true);
		}
	}

	private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
		event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
		event.setKickMessage(punishment.getKickMessage());
	}


}
