package dev.lugami.tails.tags.commands;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.tags.Tag;
import dev.lugami.tails.util.CC;
import org.bukkit.*;
import org.bukkit.entity.Player;

@CommandMeta(label = {"tag create", "prefix create"}, permission = "core.admin.tags")
public class TagCreateCommand {

    public void execute(Player player, String name){
        Tag tag = TailsAPI.getTagByName(name);
        if(tag != null){
            player.sendMessage(CC.translate("&6A tag with that name already exists."));
        }else{
            tag = new Tag(name);
            tag.setServerName(Bukkit.getServerName().toUpperCase().replace(" ", ""));
            tag.save();
            player.sendMessage(CC.translate("&aTag &7(&f" + tag.getName() + "&7) &ahas been successfully created!"));
            Tag.getTags().clear();
            Tag.load();
        }
    }

}
