package dev.lugami.tails.chat;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.chat.event.ChatAttemptEvent;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.TimeUtil;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		ChatAttempt chatAttempt = Tails.get().getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
		ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

		Bukkit.getServer().getPluginManager().callEvent(chatAttemptEvent);

		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
		if (!chatAttemptEvent.isCancelled()) {
			switch (chatAttempt.getResponse()) {
				case ALLOWED: {
					event.setFormat("%1$s" + CC.translate(profile.getTagPrefix()) + CC.RESET + ": %2$s");
				}
				break;
				case MESSAGE_FILTERED: {
					event.setCancelled(true);
					chatAttempt.getFilterFlagged().punish(event.getPlayer());
				}
				break;
				case PLAYER_MUTED: {
					event.setCancelled(true);

					if (chatAttempt.getPunishment().isPermanent()) {
						event.getPlayer().sendMessage(CC.RED + "You are muted for forever.");
					} else {
						event.getPlayer().sendMessage(CC.RED + "You are muted for another " +
								chatAttempt.getPunishment().getTimeRemaining() + ".");
					}
				}
				break;
				case CHAT_MUTED: {
					event.setCancelled(true);
					event.getPlayer().sendMessage(CC.RED + "The public chat is currently muted.");
				}
				break;
				case CHAT_DELAYED: {
					event.setCancelled(true);
					event.getPlayer().sendMessage(Locale.CHAT_DELAYED.format(
							TimeUtil.millisToSeconds((long) chatAttempt.getValue())) + " seconds");
				}
				break;
			}
		}

		if (chatAttempt.getResponse() == ChatAttempt.Response.ALLOWED) {
			event.getRecipients().removeIf(player -> {
				Profile profile1 = Profile.getProfiles().get(player.getUniqueId());
				return profile1 != null && !profile1.getOptions().publicChatEnabled();
			});
		}
	}

}
