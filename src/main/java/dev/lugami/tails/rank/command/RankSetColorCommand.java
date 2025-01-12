package dev.lugami.tails.rank.command;

import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setcolor", permission = "tails.admin.rank", async = true)
public class RankSetColorCommand {

	public void execute(CommandSender sender, Rank rank, ChatColor chatColor) {
		if (rank == null) {
			sender.sendMessage(CC.RED + "A rank with that name does not exist.");
			return;
		}

		if (chatColor == null) {
			sender.sendMessage(CC.RED + "That color is not valid.");
			return;
		}

		rank.setColor(chatColor);
		rank.save();
		rank.refresh();

		sender.sendMessage(CC.GREEN + "You updated the rank's color.");
	}

}
