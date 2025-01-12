package dev.lugami.tails.chat;

import dev.lugami.tails.Tails;
import dev.lugami.tails.chat.filter.ChatFilter;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.punishment.PunishmentType;
import java.util.ArrayList;
import java.util.List;

import dev.lugami.tails.util.Cooldown;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class Chat {

	private Tails tails;

	public Chat(Tails tails) {
		this.tails = tails;
	}

	@Getter @Setter private int delayTime = 3;
	@Getter private boolean publicChatMuted = false;
	@Getter private boolean publicChatDelayed = false;
	@Getter private final List<ChatFilter> filters = new ArrayList<>();
	@Getter private List<String> filteredPhrases = new ArrayList<>();
	@Getter private List<String> linkWhitelist = new ArrayList<>();

	public void togglePublicChatMute() {
		publicChatMuted = !publicChatMuted;
	}

	public void togglePublicChatDelay() {
		publicChatDelayed = !publicChatDelayed;
	}

	public ChatAttempt attemptChatMessage(Player player, String message) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (profile.getActivePunishmentByType(PunishmentType.MUTE) != null) {
			return new ChatAttempt(ChatAttempt.Response.PLAYER_MUTED, profile.getActivePunishmentByType(PunishmentType.MUTE));
		}

		if (publicChatMuted && !player.hasPermission("tails.staff")) {
			return new ChatAttempt(ChatAttempt.Response.CHAT_MUTED);
		}

		if (publicChatDelayed && !profile.getChatCooldown().hasExpired() && !player.hasPermission("tails.staff")) {
			ChatAttempt attempt = new ChatAttempt(ChatAttempt.Response.CHAT_DELAYED);
			attempt.setValue(profile.getChatCooldown().getRemaining());
			return attempt;
		}

		String msg = message.toLowerCase()
		                    .replace("3", "e")
		                    .replace("1", "i")
		                    .replace("!", "i")
		                    .replace("@", "a")
		                    .replace("7", "t")
		                    .replace("0", "o")
		                    .replace("5", "s")
		                    .replace("8", "b")
		                    .replaceAll("\\p{Punct}|\\d", "").trim();

		String[] words = msg.trim().split(" ");

		for (ChatFilter chatFilter : filters) {
			if (chatFilter.isFiltered(msg, words)) {
				return new ChatAttempt(ChatAttempt.Response.MESSAGE_FILTERED);
			}
		}

		if (publicChatDelayed) {
			profile.setChatCooldown(new Cooldown(delayTime * 1000L));
		}

		return new ChatAttempt(ChatAttempt.Response.ALLOWED);
	}

}
