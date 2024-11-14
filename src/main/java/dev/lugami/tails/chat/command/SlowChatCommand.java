package dev.lugami.tails.chat.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.TailsAPI;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "slowchat", permission = "tails.staff.slowchat")
public class SlowChatCommand {

	public void execute(CommandSender sender) {
		Tails.get().getChat().togglePublicChatDelay();

		String senderName;

		if (sender instanceof Player) {
			senderName = TailsAPI.getColoredName((Player) sender);
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Tails.get().getChat().getDelayTime() == 1 ? "" : "s";

		if (Tails.get().getChat().isPublicChatDelayed()) {
			Bukkit.broadcastMessage(Locale.DELAY_CHAT_ENABLED_BROADCAST.format(senderName,
					Tails.get().getChat().getDelayTime(), context));
		} else {
			Bukkit.broadcastMessage(Locale.DELAY_CHAT_DISABLED_BROADCAST.format(senderName));
		}
	}

	public void execute(CommandSender sender, Integer seconds) {
		if (seconds < 0 || seconds > 60) {
			sender.sendMessage(ChatColor.RED + "A delay can only be between 1-60 seconds.");
			return;
		}

		String context = seconds == 1 ? "" : "s";

		sender.sendMessage(ChatColor.GREEN + "You have updated the chat delay to " + seconds + " second" + context + ".");
		Tails.get().getChat().setDelayTime(seconds);
	}

}
