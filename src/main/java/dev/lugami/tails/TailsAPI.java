package dev.lugami.tails;

import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.tags.Tag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TailsAPI {

	public static ChatColor getColorOfPlayer(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return profile == null ? ChatColor.WHITE : (profile.getDisguisedRank() != null ? profile.getDisguisedRank().getColor() : profile.getActiveRank().getColor());
	}

	public static String getColoredName(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return (profile == null ? ChatColor.WHITE : (profile.getDisguisedRank() != null ? profile.getDisguisedRank().getColor() : profile.getActiveRank().getColor())) + player.getName();
	}

	public static Rank getRankOfPlayer(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return profile == null ? Rank.getDefaultRank() : (profile.getDisguisedRank() != null ? profile.getDisguisedRank() : profile.getActiveRank());
	}

	public static Tag getTagByName(String name) {
		return Tag.getTags().stream().filter(check -> check.getName().equalsIgnoreCase(name)).findAny().orElse(null);
	}

	public static boolean isInStaffMode(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		return profile != null && player.hasPermission("tails.staff") && profile.getStaffOptions().staffModeEnabled();
	}

}
