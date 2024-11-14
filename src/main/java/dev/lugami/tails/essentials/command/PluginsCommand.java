package dev.lugami.tails.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.ItemBuilder;
import dev.lugami.tails.util.menu.Button;
import dev.lugami.tails.util.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

@CommandMeta(label = {"plugins", "pl"})
public class PluginsCommand {

    public void execute(Player player) {
        Menu menu = new Menu() {
            @Override
            public String getTitle(Player player) {
                return "&6Plugins";
            }

            @Override
            public void onOpen(Player player) {
                player.sendMessage(CC.GREEN + "We don't hide our plugins, so feel free to search anything you might want :)");
            }

            @Override
            public Map<Integer, Button> getButtons(Player player) {
                Map<Integer, Button> buttons = new HashMap<>();
                List<Plugin> plugins = new ArrayList<>(Arrays.asList(Bukkit.getPluginManager().getPlugins()));
                plugins.sort(Comparator.comparing(Plugin::getName).reversed());
                for (Plugin plugin : plugins) {
                    String name = plugin.getDescription().getName();
                    if (name.equalsIgnoreCase("Tails")) {
                        name = "Core";
                    }
                    String finalName = name;
                    buttons.put(buttons.size(), new Button() {
                        @Override
                        public ItemStack getButtonItem(Player player) {
                            return new ItemBuilder(Material.ENCHANTED_BOOK).name("&6" + finalName).lore("&fVersion: &6" + plugin.getDescription().getVersion()).build();
                        }
                    });
                }
                return buttons;
            }
        };

        menu.openMenu(player);
    }
}
