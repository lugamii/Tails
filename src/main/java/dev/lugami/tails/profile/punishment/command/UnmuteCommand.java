package dev.lugami.tails.profile.punishment.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.network.packet.PacketBroadcastPunishment;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.punishment.Punishment;
import dev.lugami.tails.profile.punishment.PunishmentType;
import dev.lugami.tails.util.CC;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "unmute", permission = "tails.staff.unmute", async = true, options = "s")
public class UnmuteCommand {

	public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, String reason) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (profile.getActivePunishmentByType(PunishmentType.MUTE) == null) {
			sender.sendMessage(CC.RED + "That player is not muted.");
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

		Punishment punishment = profile.getActivePunishmentByType(PunishmentType.MUTE);
		punishment.setRemovedAt(System.currentTimeMillis());
		punishment.setRemovedReason(reason);
		punishment.setRemoved(true);

		if (sender instanceof Player) {
			punishment.setRemovedBy(((Player) sender).getUniqueId());
		}

		profile.save();

		Tails.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), option != null));
	}

}
