package dev.lugami.tails.essentials.command;

import dev.lugami.tails.TailsAPI;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@CommandMeta(label = "list")
public class ListCommand {
    public void execute(Player sender) {
        List<Player> sortedPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        sortedPlayers.sort((o1, o2) -> {
            Profile p1 = Profile.getByUuid(o1.getUniqueId());
            Profile p2 = Profile.getByUuid(o2.getUniqueId());

            return (p2.getDisguisedRank() == null ? p2.getActiveRank().getWeight() : p2.getDisguisedRank().getWeight()) - (p1.getDisguisedRank() == null ? p1.getActiveRank().getWeight() : p1.getDisguisedRank().getWeight());
        });

        List<String> playerNames = new ArrayList<>();

        for (Player player : sortedPlayers) {
            playerNames.add(TailsAPI.getColoredName(player));
        }

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanks().values());
        sortedRanks.sort((o1, o2) -> o2.getWeight() - o1.getWeight());

        List<String> rankNames = new ArrayList<>();

        for (Rank rank : sortedRanks) {
            rankNames.add(rank.getColor() + rank.getDisplayName());
        }

        sender.sendMessage(StringUtils.join(rankNames, ChatColor.WHITE + ", "));
        sender.sendMessage("(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "): " +
                StringUtils.join(playerNames, ChatColor.WHITE + ", "));
    }

}
