package dev.lugami.tails.disguise.commands.staff;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import dev.lugami.tails.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffdisguise list", "staffdis list", "disgmanager list" }, permission = "core.admin.disguise")
public class StaffDisguiseList {

    public void execute(Player player) {
        player.sendMessage("");
        player.sendMessage(CC.YELLOW + "Viewing list of disguised names...");

        Tails.get().getMongoDatabase().getCollection("disguise").find().iterator().forEachRemaining(doc -> {
            player.sendMessage(CC.GRAY + " - " +
                    "Name: " + doc.getString("name") + " " + // try again (and if possible delete all data from disguise collection(u know make fix ?
                    "Skin: " + doc.getString("skin"));
        });

        player.sendMessage("");
    }

}
