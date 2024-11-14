//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.profile.option.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.profile.option.menu.ProfileOptionsMenu;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"options", "settings"}
)
public class OptionsCommand {
    public OptionsCommand() {
    }

    public void execute(Player player) {
        (new ProfileOptionsMenu()).openMenu(player);
    }
}
