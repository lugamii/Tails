package dev.lugami.tails.tags.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.tags.Tag;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = {"tag delete", "prefix delete"}, permission = "core.admin.tags")
public class TagDeleteCommand {

    public void execute(Player player, String name){
        Tag tag = TailsAPI.getTagByName(name);
        if(tag == null){
            player.sendMessage(CC.translate("&6A tag with that name not exists."));
        }else{
            player.sendMessage(CC.translate("&6Tag " + (tag.getPrefix() == null ? tag.getName() : tag.getPrefix()) + " &6has been successfully deleted!"));
            tag.remove();
            Tag.getTags().clear();
            Tag.load();
        }
    }

}
