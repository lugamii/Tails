package dev.lugami.tails.essentials;

import dev.lugami.tails.util.CC;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EssentialsListener implements Listener {

	private static List<String> BLOCKED_COMMANDS = Arrays.asList(
			"//calc",
			"//eval",
			"//solve",
			"/bukkit:",
			"/me",
			"/bukkit:me",
			"/minecraft:",
			"/minecraft:me",
			"/version",
			"/ver"
	);

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandProcess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = (event.getMessage().startsWith("/") ? "" : "/") + event.getMessage();

		for (String blockedCommand : BLOCKED_COMMANDS) {
			if (message.startsWith(blockedCommand)) {
				if (message.equalsIgnoreCase("/version") || message.equalsIgnoreCase("/ver") || message.equalsIgnoreCase("/pl") || message.equalsIgnoreCase("/plugins")) {
					if (event.getPlayer().isOp()) {
						return;
					}
				}

				player.sendMessage(CC.RED + "You cannot perform this command.");
				event.setCancelled(true);
				return;
			}
		}
	}

}
