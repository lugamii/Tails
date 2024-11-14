//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"more"},
        permission = "tails.more"
)
public class MoreCommand {
    public MoreCommand() {
    }

    public void execute(Player player) {
        if (player.getItemInHand() == null) {
            player.sendMessage(CC.RED + "There is nothing in your hand.");
        } else {
            player.getItemInHand().setAmount(64);
            player.updateInventory();
            player.sendMessage(CC.GREEN + "You gave yourself more of the item in your hand.");
        }
    }
}
