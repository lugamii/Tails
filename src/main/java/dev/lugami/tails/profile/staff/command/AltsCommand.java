package dev.lugami.tails.profile.staff.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.Locale;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandMeta(label = "alts", async = true, permission = "core.staff.alts")
public class AltsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		List<Profile> alts = new ArrayList<>();

		for (UUID altUuid : profile.getKnownAlts()) {
			Profile altProfile = Profile.getByUuid(altUuid);

			if (altProfile != null/* && altProfile.isLoaded()*/) {
				alts.add(altProfile);
			}
		}

		if (alts.isEmpty()) {
			sender.sendMessage(CC.RED + "This player has no known alt accounts.");
		} else {
			StringBuilder builder = new StringBuilder();

			for (Profile altProfile : alts) {
				builder.append(altProfile.getName());
				builder.append(", ");
			}

			sender.sendMessage(CC.GOLD + "Alts: " + CC.RESET + builder.toString());
		}
	}

}
