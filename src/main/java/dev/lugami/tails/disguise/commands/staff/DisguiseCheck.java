package dev.lugami.tails.disguise.commands.staff;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = { "disguisecheck", "discheck", "disgcheck" }, permission = "core.admin.disguise", async = true)
public class DisguiseCheck {

    public void execute(Player player, String name) {
        Profile data = Profile.getByUsername(name);

        if(data == null) {
            player.sendMessage(CC.RED + "No player found.");
            return;
        }

        boolean check = data.getDisguisedName() != null && data.getDisguisedRank() != null;

        player.sendMessage(CC.YELLOW + "Player " + data.getActiveRank().getColor() + data.getName() + CC.YELLOW + " is " + (check ? "disguised" : "not disguised") + '.');
        if(check) {
            player.sendMessage(CC.GRAY.toString() + ' ' + "*" + ' ' + CC.YELLOW + "Disguise Name: " + CC.GOLD + data.getDisguisedName());
            player.sendMessage(CC.GRAY.toString() + ' ' + "*" + ' ' + CC.YELLOW + "Disguise Skin: " + CC.GOLD + data.getDisguisedName());
            player.sendMessage(CC.GRAY.toString() + ' ' + "*" + ' ' + CC.YELLOW + "Disguise Rank: " + data.getDisguisedRank().getColor() + data.getDisguisedRank().getDisplayName());
        }
    }

}
