//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import org.bukkit.command.CommandSender;

@CommandMeta(
        label = {"tails debug"},
        permission = "tails.debug"
)
public class TailsDebugCommand {
    public TailsDebugCommand() {
    }

    public void execute(CommandSender sender) {
        Tails.get().setDebug(!Tails.get().isDebug());
        sender.sendMessage("Debug: " + Tails.get().isDebug());
    }
}
