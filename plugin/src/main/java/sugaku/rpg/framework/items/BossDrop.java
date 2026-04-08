package sugaku.rpg.framework.items;

import io.github.math0898.rpgframework.Rarity;
import org.bukkit.inventory.ItemStack;

/**
 * Describes the custom items that are dropped by bosses. Mostly used to store both an item rarity and an ItemStack
 * together.
 *
 * @param item   The item that the boss drops.
 * @param rarity The rarity the item is.
 */
public record BossDrop (ItemStack item, Rarity rarity) {
}
