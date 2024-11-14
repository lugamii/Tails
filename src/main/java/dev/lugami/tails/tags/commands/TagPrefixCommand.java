package dev.lugami.tails.tags.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.tags.Tag;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = {"tag prefix", "prefix prefix"}, permission = "core.admin.tags")
public class TagPrefixCommand {

    public void execute(Player player, String name, String prefix){
        Tag tag = TailsAPI.getTagByName(name);
        if (tag == null) {
            player.sendMessage(CC.translate("&6A tag with that name doesn't exists."));
        } else {
            tag.setPrefix(prefix);
            tag.save();
            player.sendMessage(CC.translate("&aTag " + tag.getPrefix() + " &acalled &7(&f" + tag.getName() + "&a) has been updated."));
            Tag.getTags().clear();
            Tag.load();
        }
    }

}
