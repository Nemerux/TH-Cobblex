package pl.tryhardujemy.thcobblex;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class CobblexDrop {
    private ItemStack item;
    private int minAmount;
    private int maxAmount;
    private double chance;
    private String displayName;

    CobblexDrop(ConfigurationSection section) {
        this.chance = section.getDouble("chance");
        this.minAmount = section.getInt("amount.min");
        this.maxAmount = section.getInt("amount.max");
        this.displayName = fixColor(section.getString("displayName"));
        this.item = new ItemStack(Material.getMaterial(section.getString("type")));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(fixColor(section.getString("name")));
        itemMeta.setLore(section.getStringList("lore").stream().map(this::fixColor).collect(Collectors.toList()));
        item.setItemMeta(itemMeta);
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getChance() {
        return chance;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    private String fixColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
