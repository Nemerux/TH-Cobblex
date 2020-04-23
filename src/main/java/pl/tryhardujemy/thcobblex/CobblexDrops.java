package pl.tryhardujemy.thcobblex;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class CobblexDrops {
    private List<CobblexDrop> dropList;

    public CobblexDrops(Configuration config) {
        ConfigurationSection section = config.getConfigurationSection("cobblex.drops");
        this.dropList = section.getKeys(false).stream()
                .map(section::getConfigurationSection)
                .map(CobblexDrop::new)
                .collect(Collectors.toList());
    }

    public List<CobblexDrop> findRandomItems(Random random) {
        return dropList.stream().filter(entry -> random.nextInt(100) < entry.getChance()).collect(Collectors.toList());
    }

    public List<CobblexDrop> getDropList() {
        return dropList;
    }
}
