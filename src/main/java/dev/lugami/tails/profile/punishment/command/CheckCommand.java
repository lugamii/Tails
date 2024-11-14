package dev.lugami.tails.profile.punishment.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.cache.RedisPlayerData;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.punishment.menu.PunishmentsListMenu;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "check", "c" }, permission = "tails.staff.check", async = true)
public class CheckCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		RedisPlayerData redisPlayerData = Tails.get().getRedisCache().getPlayerData(profile.getUuid());

		if (redisPlayerData == null) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new PunishmentsListMenu(profile, redisPlayerData).openMenu(player);
	}

}
