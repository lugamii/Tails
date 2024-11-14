package dev.lugami.tails.profile.punishment.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.network.packet.PacketClearPunishments;
import dev.lugami.tails.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "clearpunishments", permission = "tails.admin.clearpunishments", async = true)
public class ClearPunishmentsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		profile.getPunishments().clear();
		profile.save();

		Tails.get().getPidgin().sendPacket(new PacketClearPunishments(profile.getUuid()));

		sender.sendMessage(ChatColor.GREEN + "Cleared punishments of " + profile.getColoredUsername() +
		                   ChatColor.GREEN + "!");
	}

}
