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
        label = {"setspawn"},
        permission = "tails.setspawn"
)
public class SetSpawnCommand {
    public SetSpawnCommand() {
    }

    public void execute(Player player) {
        Tails.get().getEssentials().setSpawn(player.getLocation());
        player.sendMessage(CC.GREEN + "You updated this world's spawn.");
    }
}
