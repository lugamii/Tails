package dev.lugami.tails.rank.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank addpermission", "rank addperm" }, permission = "tails.admin.rank", async = true)
public class RankAddPermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		permission = permission.toLowerCase().trim();

		if (rank.getPermissions().contains(permission)) {
			sender.sendMessage(CC.RED + "That rank is already assigned that permission.");
			return;
		}

		for (Rank childRank : rank.getInherited()) {
			if (childRank.hasPermission(permission)) {
				sender.sendMessage(CC.RED + "That rank is inheriting that permission from the " +
				                   rank.getColor() + rank.getDisplayName() + " rank.");
				return;
			}
		}

		rank.getPermissions().add(permission);
		rank.save();
		rank.refresh();

		sender.sendMessage(CC.GREEN + "Successfully added permission to rank.");
	}

}
