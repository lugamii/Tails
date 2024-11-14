package dev.lugami.tails.profile.staff.command;

import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffmode", "sm" }, permission = "tails.staff")
public class StaffModeCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().staffModeEnabled(!profile.getStaffOptions().staffModeEnabled());

        player.sendMessage(profile.getStaffOptions().staffModeEnabled() ?
                CC.GREEN + "You are now in staff mode." : CC.RED + "You are no longer in staff mode.");
    }

}
