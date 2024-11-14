package dev.lugami.tails.network.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.network.packet.PacketStaffReport;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.Cooldown;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandMeta(label = "report", async = true)
public class ReportCommand {

	public void execute(Player player, @CPL("target") Player target, @CPL("reason") String reason) {
		if (target == null) {
			player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
			return;
		}

		if (player.equals(target)) {
			player.sendMessage(ChatColor.RED + "You cannot report yourself.");
			return;
		}

		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (!profile.getRequestCooldown().hasExpired()) {
			player.sendMessage(ChatColor.RED + "You cannot request assistance that quickly. Try again later.");
			return;
		}

		Tails.get().getPidgin().sendPacket(new PacketStaffReport(
				TailsAPI.getColoredName(player),
				TailsAPI.getColoredName(target),
				reason,
				Bukkit.getServerId(),
				Bukkit.getServerName()
		));

		profile.setRequestCooldown(new Cooldown(120_000L));
		player.sendMessage(Locale.STAFF_REQUEST_SUBMITTED.format());
	}

}
