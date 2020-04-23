import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

public class CraftDupa {
    @Test
    public void prepareItemCraft() {
        ItemStack cobblestoneItem = new ItemStack(Material.COBBLESTONE, 64);
        ItemStack[] matrix = new ItemStack[]{cobblestoneItem, cobblestoneItem, cobblestoneItem, cobblestoneItem, cobblestoneItem, cobblestoneItem, cobblestoneItem, cobblestoneItem, cobblestoneItem};
        System.out.println(isCobblexRecipe(matrix, false) + ":" + isCobblexRecipe(matrix, true));
    }

    private boolean isCobblexRecipe(ItemStack[] itemMatrix, boolean shouldBeStacked) {
        for (ItemStack item : itemMatrix) {
            if (item == null) return false;

            if (item.getType() != Material.COBBLESTONE) return false;
            if (shouldBeStacked) {
                if (item.getAmount() != 64) return false;
            }
        }
        return true;
    }
}
