package dev.lugami.tails.disguise.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

import java.util.Objects;

@CommandMeta(label = {"undisguise", "ud"}, permission = "disguise.use")
public class UndisguiseCommand {

    public void execute(Player player){
        Profile data = Profile.getByUuid(player.getUniqueId());

        if(data == null) {
            player.sendMessage("rip 2");
            return;
        }

        if(!Tails.get().getDisguiseManager().isDisguised(player.getUniqueId())) {
            player.sendMessage(CC.RED + "You aren't disguised. To disguise use /disguise!");
            return;
        }

        player.sendMessage(CC.WHITE + "You are no longer disguised as " + Objects.requireNonNull(data.getDisguisedRank()).getColor() + player.getName() + CC.WHITE + '.');

        Tails.get().getDisguiseManager().undisguise(player);
    }
}
