//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"spawn"},
        permission = "tails.spawn"
)
public class SpawnCommand {
    public SpawnCommand() {
    }

    public void execute(Player player) {
        Tails.get().getEssentials().teleportToSpawn(player);
        player.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
    }
}
