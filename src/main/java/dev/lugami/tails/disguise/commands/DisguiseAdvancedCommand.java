package dev.lugami.tails.disguise.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.disguise.menu.DisguiseRankMenu;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

@CommandMeta(label = {"disguise", "dis"})
public class DisguiseAdvancedCommand {

    public void execute(Player player, String name) {
        if(!player.hasPermission("disguise.admin.use") && player.hasPermission("disguise.use")){
            player.sendMessage(CC.translate("Usage: /disguise random"));
            return;
        }else if(!player.hasPermission("disguise.admin.use") && !player.hasPermission("disguise.use")){
            player.sendMessage(CC.translate("&cYou do not have permission to execute this command."));
            return;
        }

        if (name.length() > 16) {
            player.sendMessage(CC.RED + "Name must have less than 16 characters.");
            return;
        }

        if (!Pattern.compile("[a-zA-Z0-9_]*").matcher(name).matches() || name.length() < 3 || name.length() > 16) {
            player.sendMessage(CC.translate("&cInvalid name."));
            return;
        }

        new DisguiseRankMenu(player, name).openMenu(player);

    }
}
