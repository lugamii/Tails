//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"day"}
)
public class DayCommand {
    public DayCommand() {
    }

    public void execute(Player player) {
        player.setPlayerTime(6000L, false);
        player.sendMessage(CC.GREEN + "It's now day time.");
    }
}
