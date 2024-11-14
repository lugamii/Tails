package dev.lugami.tails.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = {"ss", "freeze"}, permission = "tails.staff.freeze")
public class FreezeCommand {

    public void execute(Player player, @CPL("target") Player target) {
        if (player == target) {
            player.sendMessage(CC.translate("&cYou cannot freeze yourself."));
        }
        Profile targetProfile = Profile.getByUuid(target.getUniqueId());
        boolean freeze = !targetProfile.isFrozen();
        targetProfile.setFrozen(freeze);
        if (freeze) {
            target.sendMessage(CC.translate(""));
            target.sendMessage(CC.translate("&cYou were frozen by a staff member."));
            target.sendMessage(CC.translate("&cYou have 5 minutes to join our Discord."));
            target.sendMessage(CC.translate("&chttps://discord.pots.lol"));
            target.sendMessage(CC.translate(""));
        } else {
            target.sendMessage(CC.translate(""));
            target.sendMessage(CC.translate("&aYou were unfrozen by a staff member."));
            target.sendMessage(CC.translate(""));
        }
    }

}
