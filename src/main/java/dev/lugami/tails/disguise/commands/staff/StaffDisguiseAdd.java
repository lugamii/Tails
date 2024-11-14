package dev.lugami.tails.disguise.commands.staff;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import dev.lugami.tails.util.CC;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandMeta(label = { "staffdisguise add", "staffdis add", "disgmanager add" }, permission = "core.admin.disguise")
public class StaffDisguiseAdd {

    public void execute(Player player, String skin, String name) {

        if(name.length() > 16 || skin.length() > 16) {
            player.sendMessage(CC.RED + "Name must have less than 16 characters.");
            return;
        }

        Document document = new Document();
        document.put("uuid", UUID.randomUUID().toString());
        document.put("name", name);
        document.put("skin", skin);

        Tails.get().getMongoDatabase().getCollection("disguise").insertOne(document);

        player.sendMessage(CC.YELLOW + "You've added disguise profie with name " + CC.GOLD + name + CC.YELLOW + " and skin " + CC.GOLD + skin + CC.YELLOW + '.');

    }

}
