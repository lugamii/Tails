//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.profile.grant.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Locale;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.grant.menu.GrantsListMenu;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"grants"},
        async = true,
        permission = "tails.grants.show"
)
public class GrantsCommand {
    public GrantsCommand() {
    }

    public void execute(Player player, @CPL("player") Profile profile) {
        if (profile != null && profile.isLoaded()) {
            (new GrantsListMenu(profile)).openMenu(player);
        } else {
            player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format(new Object[0]));
        }
    }
}
