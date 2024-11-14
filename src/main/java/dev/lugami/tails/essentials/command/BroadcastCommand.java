//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import dev.lugami.tails.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandMeta(
        label = {"broadcast", "bc"},
        options = {"r"},
        permission = "zoot.broadcast"
)
public class BroadcastCommand {
    public BroadcastCommand() {
    }

    public void execute(CommandSender sender, CommandOption option, String broadcast) {
        String message = broadcast.replaceAll("(&([a-f0-9l-or]))", "§$2");
        Bukkit.broadcastMessage(CC.translate((option == null ? "&6[Broadcast] &r" : "") + message));
    }
}
