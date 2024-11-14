package dev.lugami.tails.rank.command;

import dev.lugami.tails.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "ranks", permission = "tails.admin.rank")
public class RanksCommand {

	public void execute(CommandSender sender) {
		List<Rank> ranks = new ArrayList<>(Rank.getRanks().values());
		ranks.sort((o1, o2) -> o2.getWeight() - o1.getWeight());

		sender.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "Ranks");

		for (Rank rank : ranks) {
			sender.sendMessage(
					ChatColor.GRAY + " - " + ChatColor.RESET + rank.getColor() + rank.getDisplayName() +
			          ChatColor.RESET +  " (Weight: " + rank.getWeight() + ")"
			);
		}
	}

}
