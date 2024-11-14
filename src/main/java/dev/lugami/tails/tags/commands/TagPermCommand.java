package dev.lugami.tails.tags.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.tags.Tag;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = {"tag perm", "prefix perm"}, permission = "core.admin.tags")
public class TagPermCommand {

    public void execute(Player player, String name, String permission){
        Tag tag = TailsAPI.getTagByName(name);
        if (tag == null) {
            player.sendMessage(CC.translate("&6A tag with that name doesn't exists."));
        } else {
            tag.setPermission(permission);
            tag.save();
            player.sendMessage(CC.translate("&aTag &7(&f" + tag.getName() + "&7) &ahas been updated!"));
            Tag.getTags().clear();
            Tag.load();
        }
    }

}
