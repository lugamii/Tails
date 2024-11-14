package dev.lugami.tails.tags.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

@CommandMeta(label = {"tag", "prefix"}, permission = "core.admin.tags")
public class TagCommand {

    public void execute(Player player){
        player.sendMessage(CC.translate("&9&m" + StringUtils.repeat("-", 45)));
        player.sendMessage(CC.translate("&6/tag create <name>"));
        player.sendMessage(CC.translate("&6/tag delete <name>"));
        player.sendMessage(CC.translate("&6/tag prefix <name> <prefix>"));
        player.sendMessage(CC.translate("&6/tag perm <name> <permission>"));
        player.sendMessage(CC.translate("&6/tag list"));
        player.sendMessage(CC.translate("&9&m" + StringUtils.repeat("-", 45)));
    }

}
