package dev.lugami.tails.profile.staff.command;

import dev.lugami.tails.Tails;
import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.network.packet.PacketStaffChat;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffchat", "sc" }, permission = "tails.staff")
public class StaffChatCommand {

	public void execute(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getStaffOptions().staffChatModeEnabled(!profile.getStaffOptions().staffChatModeEnabled());

		player.sendMessage(profile.getStaffOptions().staffChatModeEnabled() ?
				CC.GREEN + "You are now talking in staff chat." : CC.RED + "You are no longer talking in staff chat.");
	}

	public void execute(Player player, String message) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (!profile.getStaffOptions().staffModeEnabled()) {
			player.sendMessage(CC.RED + "You are not in staff mode.");
			return;
		}

		Tails.get().getPidgin().sendPacket(new PacketStaffChat(TailsAPI.getColoredName(player),
				Bukkit.getServerId(), message));
	}

}
