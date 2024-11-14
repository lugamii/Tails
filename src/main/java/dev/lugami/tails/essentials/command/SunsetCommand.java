//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"sunset"}
)
public class SunsetCommand {
    public SunsetCommand() {
    }

    public void execute(Player player) {
        player.setPlayerTime(12000L, false);
        player.sendMessage(CC.GREEN + "It's now sunset.");
    }
}
