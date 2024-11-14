//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.lugami.tails.util.menu.button;

import dev.lugami.tails.util.menu.Button;
import dev.lugami.tails.util.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class JumpToMenuButton extends Button {
    private Menu menu;
    private ItemStack itemStack;

    public JumpToMenuButton(Menu menu, ItemStack itemStack) {
        this.menu = menu;
        this.itemStack = itemStack;
    }

    public ItemStack getButtonItem(Player player) {
        return this.itemStack;
    }

    public void clicked(Player player, ClickType clickType) {
        this.menu.openMenu(player);
    }
}
