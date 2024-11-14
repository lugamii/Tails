//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

@CommandMeta(
        label = {"sudoall"},
        permission = "tails.sudo",
        options = {"s"}
)
public class SudoAllCommand {
    public SudoAllCommand() {
    }

    public void execute(CommandSender sender, CommandOption option, String chat) {
        Iterator var4 = Bukkit.getOnlinePlayers().iterator();

        while(true) {
            Player player;
            do {
                if (!var4.hasNext()) {
                    sender.sendMessage(ChatColor.GREEN + "Forced all players to chat!");
                    return;
                }

                player = (Player)var4.next();
            } while(option == null && !player.equals(sender));

            player.chat(chat);
        }
    }
}
