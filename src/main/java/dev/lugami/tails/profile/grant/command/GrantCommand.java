package dev.lugami.tails.profile.grant.command;

import dev.lugami.tails.Locale;
import dev.lugami.tails.Tails;
import dev.lugami.tails.network.packet.PacketAddGrant;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.grant.Grant;
import dev.lugami.tails.profile.grant.event.GrantAppliedEvent;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.TimeUtil;
import dev.lugami.tails.util.duration.Duration;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "grant", async = true, permission = "tails.grants.add")
public class GrantCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile, Rank rank, Duration duration, String reason) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (duration.getValue() == -1) {
			sender.sendMessage(CC.RED + "That duration is not valid.");
			sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
			return;
		}

		UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
		Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
				duration.getValue());

		profile.getGrants().add(grant);
		profile.save();
		profile.activateNextGrant();

		Tails.get().getPidgin().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

		sender.sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
				.replace("{rank}", rank.getDisplayName())
				.replace("{player}", profile.getName())
				.replace("{time-remaining}", duration.getValue() == Integer.MAX_VALUE ? "forever"
						: TimeUtil.millisToRoundedTime(duration.getValue())));

		Player player = profile.getPlayer();

		if (player != null) {
			new GrantAppliedEvent(player, grant).call();
		}
	}

}
