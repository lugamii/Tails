package dev.lugami.tails.disguise.commands;

import com.google.common.collect.Lists;
import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Tails;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@CommandMeta(label = {"disguise random", "dis random"}, permission = "disguise.use")
public class DisguiseNormalCommand {

    public void execute(Player player) {
        List<Document> documents = Lists.newArrayList();
        Tails.get().getMongoDatabase().getCollection("disguise").find().iterator().forEachRemaining(documents::add);

        Document result = documents.get(ThreadLocalRandom.current().nextInt(documents.size()));



        if(result != null) {
            disguise(player, Profile.getByUuid(player.getUniqueId()), Rank.getDefaultRank(), result.getString("name"));
        }
    }

    private static void disguise(Player player, Profile data, Rank rank, String name) {
        try {
            if(Tails.get().getDisguiseManager().disguise(player, rank, name)) {
                data.setDisguisedRank(rank);
                player.sendMessage(CC.WHITE + "You are now disguised as " + CC.GOLD + name + CC.WHITE + " with the rank " + rank.getColor() + rank.getColor() + rank.getDisplayName() + CC.WHITE + '.');
            }
            player.closeInventory();
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(CC.RED + "Something went wrong while disguising you! Please contact a staff member or any online developer.");
        }
    }

}
