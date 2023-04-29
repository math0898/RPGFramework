package sugaku.rpg.framework.items;

import org.bukkit.inventory.ItemStack;

/**
 * Describes the custom items that are dropped by bosses. Mostly used to store both an item rarity and an ItemStack
 * together.
 */
public class BossDrop {

    /**
     * The actually stored ItemStack.
     */
    private final ItemStack item;

    /**
     * The rarity of the stored item.
     */
    private final Rarity rarity;

    /**
     * Creates a new boss drop object which stores both the given rarity and ItemStack.
     *
     * @param item The item that the boss drops.
     * @param rarity The rarity the item is.
     */
    public BossDrop(ItemStack item, Rarity rarity){
        this.item = item;
        this.rarity = rarity;
    }

    /**
     * Returns the rarity of the boss drop item.
     *
     * @return Rarity enum value of the rarity.
     */
    public Rarity getRarity() { return rarity; }

    /**
     * Returns the boss drop item.
     *
     * @return The ItemStack item that the boss should drop.
     */
    public ItemStack getItem() { return item; }
}
