package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = "night")
public class NightCommand {

    public void execute(Player player) {
        player.setPlayerTime(18000L, false);
        player.sendMessage(CC.GREEN + "It's now night time.");
    }

}