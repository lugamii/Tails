//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.rank.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Locale;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(
        label = {"rank delete"},
        permission = "tails.admin.rank",
        async = true
)
public class RankDeleteCommand {
    public RankDeleteCommand() {
    }

    public void execute(CommandSender sender, Rank rank) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format(new Object[0]));
        } else {
            rank.delete();
            sender.sendMessage(CC.GREEN + "You deleted the rank.");
        }
    }
}
