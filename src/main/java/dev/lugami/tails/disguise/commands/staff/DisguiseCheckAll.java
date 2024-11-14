package dev.lugami.tails.disguise.commands.staff;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = { "disguisecheck all", "discheck all", "disgcheck all" }, permission = "core.admin.disguise", async = true)
public class DisguiseCheckAll {

    public void execute(Player player) {
        player.sendMessage("");
        player.sendMessage(CC.YELLOW + "Viewing all disguised players:");
        Profile.getProfiles().values().stream().filter(data -> data != null && data.getPlayer() != null
                && data.getDisguisedName() != null && data.getDisguisedRank() != null).forEach(data ->
                player.sendMessage(data.getActiveRank().getColor() + data.getName() + ": "
                        + CC.YELLOW + "Name: " + CC.GOLD + data.getDisguisedName()
                        + CC.YELLOW + " Skin: " + CC.GOLD + data.getDisguisedName()
                        + CC.YELLOW + " Rank: " + data.getDisguisedRank().getColor() + data.getDisguisedRank().getDisplayName()));
        player.sendMessage("");
    }

}
