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
        label = {"rank setprefix"},
        permission = "tails.admin.rank",
        async = true
)
public class RankSetPrefixCommand {
    public RankSetPrefixCommand() {
    }

    public void execute(CommandSender sender, Rank rank, String prefix) {
        if (rank == null) {
            sender.sendMessage(CC.RED + "A rank with that name does not exist.");
        } else {
            rank.setPrefix(CC.translate(prefix));
            rank.save();
            rank.refresh();
            sender.sendMessage(CC.GREEN + "You updated the rank's prefix.");
        }
    }
}
