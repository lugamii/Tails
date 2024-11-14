package dev.lugami.tails.disguise.menu;

import dev.lugami.tails.Tails;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.ItemBuilder;
import dev.lugami.tails.util.menu.Button;
import dev.lugami.tails.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class DisguiseRankMenu extends Menu {

    private final Player player;
    private final String name;

    @Override
    public String getTitle(Player player) {
        return "Ranks";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int[] BLACK_SLOTS = new int[]{
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
        };

        int[] RANK_SLOTS = new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25
        };

        List<String> blacklistedRankNames = Tails.get().getMainConfig().getStringList("disguise.blacklisted-ranks");

        for (int i : BLACK_SLOTS) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)4, CC.translate("&e")));
        }

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanks().values());
        sortedRanks.sort((o1, o2) -> o2.getWeight() - o1.getWeight());
        Profile profile = Profile.getByUuid(player.getUniqueId());

        final int[] i = {0};
        sortedRanks.forEach(rank -> {
            if(!blacklistedRankNames.contains(rank.getDisplayName()) && profile.getActiveRank().getWeight() >= rank.getWeight()){
                buttons.put(RANK_SLOTS[i[0]], new RankButton(player, name, rank));
                i[0]++;
            }
        });

        return buttons;
    }

    @AllArgsConstructor
    private static class RankButton extends Button{

        private final Player player;
        private final String name;
        private final Rank rank;

        @Override
        public ItemStack getButtonItem(Player player) {
            ChatColor color = rank.getColor();
            Map<ChatColor, DyeColor> dyeColorMap = new HashMap<>();
            dyeColorMap.put(ChatColor.BLACK, DyeColor.BLACK);
            dyeColorMap.put(ChatColor.DARK_BLUE, DyeColor.BLUE);
            dyeColorMap.put(ChatColor.DARK_GREEN, DyeColor.GREEN);
            dyeColorMap.put(ChatColor.DARK_AQUA, DyeColor.CYAN);
            dyeColorMap.put(ChatColor.DARK_RED, DyeColor.RED);
            dyeColorMap.put(ChatColor.DARK_PURPLE, DyeColor.PURPLE);
            dyeColorMap.put(ChatColor.GOLD, DyeColor.ORANGE);
            dyeColorMap.put(ChatColor.GRAY, DyeColor.GRAY);
            dyeColorMap.put(ChatColor.DARK_GRAY, DyeColor.SILVER);
            dyeColorMap.put(ChatColor.BLUE, DyeColor.LIGHT_BLUE);
            dyeColorMap.put(ChatColor.GREEN, DyeColor.LIME);
            dyeColorMap.put(ChatColor.AQUA, DyeColor.LIGHT_BLUE);
            dyeColorMap.put(ChatColor.RED, DyeColor.RED);
            dyeColorMap.put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA);
            dyeColorMap.put(ChatColor.YELLOW, DyeColor.YELLOW);
            dyeColorMap.put(ChatColor.WHITE, DyeColor.WHITE);

            // Get the DyeColor from the map or default to WHITE
            DyeColor woolColor = dyeColorMap.getOrDefault(color, DyeColor.WHITE);

            ItemStack wool = new ItemStack(Material.WOOL, 1, woolColor.getWoolData());
            return new ItemBuilder(new ItemStack(wool))
                    .name(CC.translate(rank.getColor() + rank.getDisplayName()))
                    .lore(CC.translate("&eClick to select " + rank.getColor() + rank.getDisplayName() + " &erank."))
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            disguise(player, Profile.getByUuid(this.player.getUniqueId()), rank, name);
        }
    }

    private static void disguise(Player player, Profile data, Rank rank, String name) {
        try {
            if (Tails.get().getDisguiseManager().disguise(player, rank, name)) {
                data.setDisguisedRank(rank);
            }
            player.closeInventory();
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(CC.RED + "Something went wrong while disguising you! Please contact a staff member or any online developer.");
        }
    }
}
