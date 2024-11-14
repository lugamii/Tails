//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import dev.lugami.tails.util.BukkitReflection;
import dev.lugami.tails.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(
        label = {"setslots"},
        async = true,
        permission = "tails.setslots"
)
public class SetSlotsCommand {
    public SetSlotsCommand() {
    }

    public void execute(CommandSender sender, int slots) {
        BukkitReflection.setMaxPlayers(Tails.get().getServer(), slots);
        sender.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
    }
}
