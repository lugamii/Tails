package dev.lugami.tails.profile.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Locale;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = {"delperm", "delpermission"}, async = true, permission = "core.admin.profile")
public class DelPermission {

    public void execute(Player player, @CPL("player") Profile profile, String permission){
        if (profile == null) {
            player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
            return;
        }

        permission = permission.toLowerCase().trim();

        if (!profile.getPermissions().contains(permission)) {
            player.sendMessage(CC.RED + "That player do not have that permission.");
        } else {
            profile.getPermissions().remove(permission);
            profile.save();
            profile.load();

            player.sendMessage(CC.GREEN + "Successfully removed permission from the player.");
        }
    }

}
