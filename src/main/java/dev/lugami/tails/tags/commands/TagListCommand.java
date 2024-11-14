package dev.lugami.tails.tags.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.tags.Tag;
import dev.lugami.tails.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

@CommandMeta(label = {"tag list", "prefix list"}, permission = "core.admin.tags")
public class TagListCommand {

    public void execute(Player player){
        if (Tag.getTags().isEmpty()) {
            player.sendMessage(CC.translate("&6There are no tags created in this server."));
        } else {
            player.sendMessage(CC.translate("&9&m" + StringUtils.repeat("-", 45)));
            player.sendMessage(CC.translate("&6Tags&7: &f(" + Tag.getTags().size() + ')'));
            player.sendMessage("");
            for (Tag tag : Tag.getTags()) {
                player.sendMessage(CC.translate(" &7- " + (tag.getPrefix() == null ? tag.getName() : tag.getPrefix() + " &7(&f" + tag.getName() + "&7)")));
            }
            player.sendMessage(CC.translate("&9&m" + StringUtils.repeat("-", 45)));
        }
    }

}
