package dev.lugami.tails.tags;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.ItemBuilder;
import dev.lugami.tails.util.menu.Button;
import dev.lugami.tails.util.menu.pagination.PaginatedMenu;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@CommandMeta(label = {"prefix", "tags", "titles", "prefixes"})
public class PrefixCommand {

    public void execute(Player player) {

        if ((!player.hasPlayedBefore()) && (!player.isOnline())) {
            player.sendMessage(CC.translate("&6" + player.getName() + " has never played before."));
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());

        new PaginatedMenu(){

            @Override
            public String getPrePaginatedTitle(Player player) {
                return "Tags - " + getPage() + "/" + getPages(player);
            }

            @Override
            public Map<Integer, Button> getAllPagesButtons(Player player) {
                Map<Integer, Button> buttons = new HashMap<>();

                for(Tag tag : Tag.getTags()){
                    buttons.put(buttons.size(), new Button() {
                        @Override
                        public ItemStack getButtonItem(Player player) {
                            return new ItemBuilder(Material.NETHER_STAR).name(tag.getName()).lore("", "&fClick here to select &6" + tag.getName(), "&fIt will show up as: " + tag.getPrefix(), "", (profile.getTag() == tag ? "&aAlready selected" : tag.getPermission() != null && !player.hasPermission(tag.getPermission()) ? "&6No permissions!" : "&aClick here to select!")).build();
                        }

                        @Override
                        public void clicked(Player player, ClickType clickType) {
                            if (tag.getPermission() != null && !player.hasPermission(tag.getPermission())) {
                                return;
                            }

                            if (profile.getTag() != null && profile.getTag().equals(tag)) {
                                return;
                            }

                            player.closeInventory();

                            profile.setTag(tag);
                            profile.save();

                            player.sendMessage(CC.translate("&aYour tag has been changed to " + tag.getPrefix() + "&a."));

                        }
                    });
                }

                return buttons;
            }
        }.openMenu(player);
    }
}