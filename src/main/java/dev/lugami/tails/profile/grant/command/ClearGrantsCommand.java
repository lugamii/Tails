package dev.lugami.tails.profile.grant.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.network.packet.PacketClearGrants;
import dev.lugami.tails.profile.Profile;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "cleargrants", permission = "tails.admin.cleargrants", async = true)
public class ClearGrantsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		profile.getGrants().clear();
		profile.save();

		Tails.get().getPidgin().sendPacket(new PacketClearGrants(profile.getUuid()));

		sender.sendMessage(ChatColor.GREEN + "Cleared grants of " + profile.getName() + "!");
	}

}
