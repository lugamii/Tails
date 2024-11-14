//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Locale;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(
        label = {"sudo"},
        permission = "tails.sudo"
)
public class SudoCommand {
    public SudoCommand() {
    }

    public void execute(CommandSender sender, Player target, String chat) {
        if (target == null) {
            sender.sendMessage(Locale.PLAYER_NOT_FOUND.format(new Object[0]));
        } else {
            target.chat(chat);
            sender.sendMessage(ChatColor.GREEN + "Forced target to chat!");
        }
    }
}
