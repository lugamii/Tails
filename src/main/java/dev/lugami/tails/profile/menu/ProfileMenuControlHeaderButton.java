package dev.lugami.tails.profile.menu;

import dev.lugami.tails.cache.RedisPlayerData;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.ItemBuilder;
import dev.lugami.tails.util.menu.Button;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@AllArgsConstructor
public class ProfileMenuControlHeaderButton extends Button {

	private Profile profile;
	private RedisPlayerData playerData;

	@Override
	public ItemStack getButtonItem(Player player) {
		ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		skullMeta.setOwner(profile.getName());
		itemStack.setItemMeta(skullMeta);

		List<String> lore = new ArrayList<>();
		lore.add(CC.MENU_BAR);

		if (playerData == null) {
			lore.add("&cStatus data not available");
		} else {
			if (playerData.getLastAction() == RedisPlayerData.LastAction.JOINING_SERVER) {
				lore.add("&aCurrently Online");
			} else if (playerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER) {
				lore.add("&cLast Seen");
			}

			lore.add("&3Server: &e" + playerData.getLastSeenServer());
			lore.add("&3Updated: &e" + playerData.getTimeAgo());
		}

		lore.add(CC.MENU_BAR);

		return new ItemBuilder(itemStack)
				.name("&3" + profile.getName())
				.lore(lore)
				.build();
	}

}
