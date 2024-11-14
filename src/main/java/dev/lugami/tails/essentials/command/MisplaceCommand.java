package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = "misplace", permission = "tails.admin.misplace")
public class MisplaceCommand {

    public void execute(Player player, Double value) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (value != null && value >= 0.0 && value <= 3.0) {
            profile.setMisplace(value);
            player.sendMessage(CC.translate("&aMisplace set to " + value + "!"));
        } else {
            player.sendMessage(CC.translate("&cInvalid value."));
        }
    }

}
