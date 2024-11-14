package dev.lugami.tails.disguise.commands.staff;

import com.mongodb.client.model.Filters;
import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffdisguise remove", "staffdis remove", "disgmanager remove" }, permission = "core.admin.disguise")
public class StaffDisguiseRemove {

    public void execute(Player player, String name) {

        Tails.get().getMongoDatabase().getCollection("disguise").deleteOne(Filters.eq("name", name));

        player.sendMessage(CC.YELLOW + "You've removed disguise profie with id " + CC.GOLD + name + CC.YELLOW + '.');

    }

}
