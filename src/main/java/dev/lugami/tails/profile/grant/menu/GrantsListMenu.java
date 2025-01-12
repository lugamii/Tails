package dev.lugami.tails.profile.grant.menu;

import dev.lugami.tails.profile.grant.Grant;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.grant.procedure.GrantProcedure;
import dev.lugami.tails.profile.grant.procedure.GrantProcedureStage;
import dev.lugami.tails.profile.grant.procedure.GrantProcedureType;
import dev.lugami.tails.util.ItemBuilder;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.TimeUtil;
import dev.lugami.tails.util.menu.Button;
import dev.lugami.tails.util.menu.pagination.PaginatedMenu;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class GrantsListMenu extends PaginatedMenu {

	private Profile profile;

	@Override
	public String getPrePaginatedTitle(Player player) {
		return "&6" + profile.getName() + "'s Grants (" + profile.getGrants().size() + ")";
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (Grant grant : profile.getGrants()) {
			buttons.put(buttons.size(), new GrantInfoButton(profile, grant));
		}

		return buttons;
	}

	@AllArgsConstructor
	private class GrantInfoButton extends Button {

		private Profile profile;
		private Grant grant;

		@Override
		public ItemStack getButtonItem(Player player) {
			int durability;

			if (grant.isRemoved()) {
				durability = 5;
			} else if (grant.hasExpired()) {
				durability = 4;
			} else {
				durability = 14;
			}

			String addedBy = "Console";

			if (grant.getAddedBy() != null) {
				addedBy = "Could not fetch...";

				Profile addedByProfile = Profile.getByUuid(grant.getAddedBy());

				if (addedByProfile != null && addedByProfile.isLoaded()) {
					addedBy = addedByProfile.getName();
				}
			}

			String removedBy = "Console";

			if (grant.getRemovedBy() != null) {
				removedBy = "Could not fetch...";

				Profile removedByProfile = Profile.getByUuid(grant.getRemovedBy());

				if (removedByProfile != null && removedByProfile.isLoaded()) {
					removedBy = removedByProfile.getName();
				}
			}

			List<String> lore = new ArrayList<>();

			lore.add(CC.MENU_BAR);
			lore.add("&3Rank: &e" + grant.getRank().getDisplayName());
			lore.add("&3Duration: &e" + grant.getDurationText());
			lore.add("&3Issued by: &e" + addedBy);
			lore.add("&3Reason: &e" + grant.getAddedReason());

			if (grant.isRemoved()) {
				lore.add(CC.MENU_BAR);
				lore.add("&a&lGrant Removed");
				lore.add("&a" + TimeUtil.dateToString(new Date(grant.getRemovedAt()), "&7"));
				lore.add("&aRemoved by: &7" + removedBy);
				lore.add("&aReason: &7&o\"" + grant.getRemovedReason() + "\"");
			} else {
				if (!grant.hasExpired()) {
					lore.add(CC.MENU_BAR);

					if (player.hasPermission("tails.grants.remove")) {
						lore.add("&aRight click to remove this grant");
					} else {
						lore.add("&cYou cannot remove grants");
					}
				}
			}

			lore.add(CC.MENU_BAR);

			return new ItemBuilder(Material.PAPER)
					.name("&3" + TimeUtil.dateToString(new Date(grant.getAddedAt()), "&7"))
					.durability(durability)
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			if (clickType == ClickType.RIGHT && !grant.isRemoved() && !grant.hasExpired()) {
				if (player.hasPermission("tails.grants.remove")) {
					GrantProcedure procedure = new GrantProcedure(player, profile, GrantProcedureType.REMOVE,
							GrantProcedureStage.REQUIRE_TEXT
					);
					procedure.setGrant(grant);

					player.sendMessage(CC.GREEN + "Type a reason for removing this grant in chat...");
					player.closeInventory();
				} else {
					player.sendMessage(CC.RED + "You cannot remove grants.");
				}
			}
		}
	}

}
