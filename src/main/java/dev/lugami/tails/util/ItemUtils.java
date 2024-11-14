package dev.lugami.tails.util;

import org.bukkit.event.*;
import org.bukkit.inventory.*;
import com.google.common.base.*;
import java.util.*;
import org.bukkit.enchantments.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;

public class ItemUtils implements Listener
{
    private final ItemStack is;
    
    public ItemUtils(final Material mat) {
        this.is = new ItemStack(mat);
    }
    
    public ItemUtils(final ItemStack is) {
        this.is = is;
    }
    
    public ItemUtils(final Material material, final int amount) {
        this(material, amount, (byte)0);
    }
    
    public ItemUtils(final Material material, final int amount, final byte data) {
        Preconditions.checkNotNull((Object)material, (Object)"Material cannot be null");
        Preconditions.checkArgument(amount > 0, (Object)"Amount must be positive");
        this.is = new ItemStack(material, amount, (short)data);
    }
    
    public ItemUtils(final Material material, final int amount, final int data) {
        Preconditions.checkNotNull((Object)material, (Object)"Material cannot be null");
        Preconditions.checkArgument(amount > 0, (Object)"Amount must be positive");
        this.is = new ItemStack(material, amount, (short)data);
    }
    
    public ItemUtils amount(final int amount) {
        this.is.setAmount(amount);
        return this;
    }
    
    public ItemUtils name(final String name) {
        final ItemMeta meta = this.is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.is.setItemMeta(meta);
        return this;
    }
    
    public ItemUtils lore(final String name) {
        final ItemMeta meta = this.is.getItemMeta();
        List<String> lore = (List<String>)meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore((List)lore);
        this.is.setItemMeta(meta);
        return this;
    }
    
    public ItemUtils lore(final List<String> lore) {
        final List<String> toSet = new ArrayList<String>();
        final ItemMeta meta = this.is.getItemMeta();
        for (final String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }
        meta.setLore((List)toSet);
        this.is.setItemMeta(meta);
        return this;
    }
    
    public ItemUtils lore(final String... lore) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta == null) {
            meta = this.is.getItemMeta();
        }
        meta.setLore((List)Arrays.<String>asList(lore));
        return this;
    }
    
    public ItemUtils durability(final int durability) {
        this.is.setDurability((short)durability);
        return this;
    }
    
    public ItemUtils enchantment(final Enchantment enchantment, final int level) {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }
    
    public ItemUtils enchantment(final Enchantment enchantment) {
        this.is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }
    
    public ItemUtils type(final Material material) {
        this.is.setType(material);
        return this;
    }
    
    public ItemUtils clearLore() {
        final ItemMeta meta = this.is.getItemMeta();
        meta.setLore((List)new ArrayList());
        this.is.setItemMeta(meta);
        return this;
    }
    
    public ItemUtils clearEnchantments() {
        for (final Enchantment e : this.is.getEnchantments().keySet()) {
            this.is.removeEnchantment(e);
        }
        return this;
    }
    
    public ItemUtils data(final short data) {
        this.is.setDurability(data);
        return this;
    }
    
    public ItemUtils color(final Color color) {
        if (this.is.getType() == Material.LEATHER_BOOTS || this.is.getType() == Material.LEATHER_CHESTPLATE || this.is.getType() == Material.LEATHER_HELMET || this.is.getType() == Material.LEATHER_LEGGINGS) {
            final LeatherArmorMeta meta = (LeatherArmorMeta)this.is.getItemMeta();
            meta.setColor(color);
            this.is.setItemMeta((ItemMeta)meta);
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor!");
    }
    
    public ItemUtils meta(final ItemMeta itemMeta) {
        this.is.setItemMeta(itemMeta);
        return this;
    }
    
    public ItemStack build() {
        return this.is;
    }
}
