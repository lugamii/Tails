package dev.lugami.tails.profile.grant;

import dev.lugami.tails.Tails;
import dev.lugami.tails.network.packet.PacketDeleteGrant;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.grant.event.GrantAppliedEvent;
import dev.lugami.tails.profile.grant.event.GrantExpireEvent;
import dev.lugami.tails.profile.grant.procedure.GrantProcedure;
import dev.lugami.tails.profile.grant.procedure.GrantProcedureStage;
import dev.lugami.tails.profile.grant.procedure.GrantProcedureType;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.TimeUtil;
import dev.lugami.tails.util.callback.TypeCallback;
import dev.lugami.tails.util.menu.menus.ConfirmMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GrantListener implements Listener {

	@EventHandler
	public void onGrantAppliedEvent(GrantAppliedEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(CC.GREEN + ("A `{rank}` grant has been applied to you for {time-remaining}.")
				.replace("{rank}", grant.getRank().getDisplayName())
				.replace("{time-remaining}", grant.getDuration() == Integer.MAX_VALUE ?
						"forever" : TimeUtil.millisToRoundedTime((grant.getAddedAt() + grant.getDuration()) -
						                                         System.currentTimeMillis())));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler
	public void onGrantExpireEvent(GrantExpireEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(CC.RED + ("Your `{rank}` grant has expired.")
				.replace("{rank}", grant.getRank().getDisplayName()));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("tails.staff.grant")) {
			return;
		}

		GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

		if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
			event.setCancelled(true);

			if (event.getMessage().equalsIgnoreCase("cancel")) {
				GrantProcedure.getProcedures().remove(procedure);
				event.getPlayer().sendMessage(CC.RED + "You have cancelled the grant procedure.");
				return;
			}

			if (procedure.getType() == GrantProcedureType.REMOVE) {
				new ConfirmMenu(CC.YELLOW + "Delete this grant?", new TypeCallback<Boolean>() {
					@Override
					public void callback(Boolean data) {
						if (data) {
							procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
							procedure.getGrant().setRemovedAt(System.currentTimeMillis());
							procedure.getGrant().setRemovedReason(event.getMessage());
							procedure.getGrant().setRemoved(true);
							procedure.finish();
							event.getPlayer().sendMessage(CC.GREEN + "The grant has been removed.");

							Tails.get().getPidgin().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(),
									procedure.getGrant()));
						} else {
							procedure.cancel();
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
						}
					}
				}, true) {
					@Override
					public void onClose(Player player) {
						if (!isClosedByMenu()) {
							procedure.cancel();
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
						}
					}
				}.openMenu(event.getPlayer());
			}
		}
	}

}
