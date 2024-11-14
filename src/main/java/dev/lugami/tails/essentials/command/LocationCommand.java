//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.util.LocationUtil;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"loc"},
        permission = "tails.loc"
)
public class LocationCommand {
    public LocationCommand() {
    }

    public void execute(Player player) {
        player.sendMessage(LocationUtil.serialize(player.getLocation()));
        System.out.println(LocationUtil.serialize(player.getLocation()));
    }
}
