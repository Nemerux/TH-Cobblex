package pl.tryhardujemy.thcobblex;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class CobblexPlugin extends JavaPlugin implements Listener {
    private static final HashMap<UUID, Integer> craftAmounts = new HashMap<>();
    private static final Random RANDOM = new Random();
    private static final ItemStack COBBLEX_ITEM = new ItemStack(Material.MOSSY_COBBLESTONE, 1);
    private static CobblexPlugin pluginInstance;
    private CobblexDrops drops;

    public static CobblexPlugin getPluginInstance() {
        return pluginInstance;
    }

    @Override
    public void onEnable() {
        pluginInstance = this;
        saveDefaultConfig();

        this.drops = new CobblexDrops(getConfig());

        ItemMeta cobblexItemMeta = COBBLEX_ITEM.getItemMeta();
        cobblexItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Cobblex");
        COBBLEX_ITEM.setItemMeta(cobblexItemMeta);

        Bukkit.getPluginManager().registerEvents(this, this);

        ShapelessRecipe recipe = new ShapelessRecipe(COBBLEX_ITEM);
        recipe.addIngredient(9, Material.COBBLESTONE);
        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onItemPrepare(PrepareItemCraftEvent e) {
        if (isNotCobblexRecipe(e.getInventory().getMatrix(), false)) return;
        if (isNotCobblexRecipe(e.getInventory().getMatrix(), true)) e.getInventory().setResult(null);
        else {
            ItemStack newItem = e.getInventory().getResult();
            if(!craftAmounts.containsKey(e.getView().getPlayer().getUniqueId())) {
                craftAmounts.put(e.getView().getPlayer().getUniqueId(), RANDOM.nextInt(4));
            }

            newItem.setAmount(craftAmounts.get(e.getView().getPlayer().getUniqueId()));
            e.getInventory().setResult(newItem);
        }
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent e) {
        if (isNotCobblexRecipe(e.getInventory().getMatrix(), false)) return;
        e.getInventory().setMatrix(new ItemStack[] {null,null,null,null,null,null,null,null,null});
        craftAmounts.remove(e.getView().getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getItemInHand() == null || !e.getItemInHand().isSimilar(COBBLEX_ITEM)) return;

        if (e.getItemInHand().getAmount() == 1) e.getPlayer().getInventory().setItemInHand(null);
        else {
            ItemStack item = e.getItemInHand();
            item.setAmount(item.getAmount() - 1);
            e.getPlayer().getInventory().setItemInHand(item);
        }

        List<CobblexDrop> drops = this.drops.findRandomItems(RANDOM);

        if (drops.size() != 0) {
            e.getPlayer().sendMessage(ChatColor.GREEN + "Wydropiles: ");
            for (CobblexDrop drop : drops) {
                ItemStack item = drop.getItem();
                item.setAmount(RANDOM.nextInt((drop.getMaxAmount() - drop.getMinAmount()) + 1) + drop.getMinAmount());
                e.getPlayer().getInventory().addItem(item);
                e.getPlayer().sendMessage(ChatColor.GREEN + "- " + drop.getDisplayName() + " x" + item.getAmount());
            }
        } else e.getPlayer().sendMessage(ChatColor.RED + "Nic nie wydropiles biedaku :(");

        shootFirework(e.getBlockPlaced().getLocation());

        e.setCancelled(true);
    }

    private void shootFirework(Location loc) {
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(3);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).build());

        fw.setFireworkMeta(fwm);
    }

    private boolean isNotCobblexRecipe(ItemStack[] itemMatrix, boolean shouldBeStacked) {
        for (int i = 0; i < 9; i++) {
            ItemStack item = itemMatrix[i];
            if (item == null) return true;

            if (item.getType() != Material.COBBLESTONE) return true;
            if (shouldBeStacked) {
                if (item.getAmount() != 64) return true;
            }
        }
        return false;
    }

    public CobblexDrops getDrops() {
        return drops;
    }
}

