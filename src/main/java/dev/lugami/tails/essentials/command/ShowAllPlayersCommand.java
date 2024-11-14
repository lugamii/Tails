//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"showallplayers"},
        permission = "tails.showallplayers"
)
public class ShowAllPlayersCommand {
    public ShowAllPlayersCommand() {
    }

    public void execute(Player player) {

        for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
            player.showPlayer(otherPlayer);
        }

    }
}
