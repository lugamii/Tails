package dev.lugami.tails.essentials.command;

import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.util.BukkitReflection;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.StyleUtil;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "ping")
public class PingCommand {

    public void execute(Player player) {
        player.sendMessage(CC.YELLOW + "Your Ping: " + StyleUtil.colorPing(BukkitReflection.getPing(player)));
    }

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
        } else {
            player.sendMessage(TailsAPI.getColoredName(target) + CC.YELLOW + "'s Ping: " +
                               StyleUtil.colorPing(BukkitReflection.getPing(target)));
        }
    }

}
