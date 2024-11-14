package dev.lugami.tails.profile.punishment.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.network.packet.PacketBroadcastPunishment;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.punishment.Punishment;
import dev.lugami.tails.profile.punishment.PunishmentType;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.duration.Duration;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandMeta(label = {"ban", "banir"}, permission = "tails.staff.ban", async = true, options = "s")
public class BanCommand {

	public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, Duration duration, String reason) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
			sender.sendMessage(CC.RED + "That player is already banned.");
			return;
		}

		if (duration.getValue() == -1) {
			sender.sendMessage(CC.RED + "That duration is not valid.");
			sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BAN, System.currentTimeMillis(),
				reason, duration.getValue());

		if (sender instanceof Player) {
			punishment.setAddedBy(((Player) sender).getUniqueId());
		}

		profile.getPunishments().add(punishment);
		profile.save();

		Tails.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), option != null));

		Player player = profile.getPlayer();

		if (player != null) {
			new BukkitRunnable() {
				@Override
				public void run() {
					player.kickPlayer(punishment.getKickMessage());
				}
			}.runTask(Tails.get());
		}
	}

}
