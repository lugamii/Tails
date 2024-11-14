//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.rank.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(
        label = {"rank create"},
        permission = "tails.admin.rank",
        async = true
)
public class RankCreateCommand {
    public RankCreateCommand() {
    }

    public void execute(CommandSender sender, String name) {
        if (Rank.getRankByDisplayName(name) != null) {
            sender.sendMessage(CC.RED + "A rank with that name already exists.");
        } else {
            Rank rank = new Rank(name);
            rank.save();
            sender.sendMessage(CC.GREEN + "You created a new rank.");
        }
    }
}
