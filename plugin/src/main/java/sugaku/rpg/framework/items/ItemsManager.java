package sugaku.rpg.framework.items;

import io.github.math0898.rpgframework.items.ItemManager;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Use {@link io.github.math0898.rpgframework.items.ItemManager}
 */
@Deprecated(forRemoval = true)
public final class ItemsManager {

    /**
     * The rare spawn item for Eiryeras.
     */
    public static ItemStack EiryerasSpawn = ItemManager.getInstance().getItem("eiryeras:Spawn");

    /**
     * The uncommon axe of Krusk custom item.
     */
    public static ItemStack KruskAxe = ItemManager.getInstance().getItem("krusk:KruskAxe");

    /**
     * The uncommon boots of Krusk custom item.
     */
    public static ItemStack KruskBoots = ItemManager.getInstance().getItem("krusk:KruskBoots");

    /**
     * The rare Krusk helmet.
     */
    public static ItemStack KruskHelmet = ItemManager.getInstance().getItem("krusk:KruskHelmet");

    /**
     * The uncommon leggings of Krusk custom item.
     */
    public static ItemStack KruskLeggings = ItemManager.getInstance().getItem("krusk:KruskLeggings");

    /**
     * The uncommon undead chestplate custom item.
     */
    public static ItemStack UndeadChestplate = ItemManager.getInstance().getItem("krusk:UndeadChestplate");

    /**
     * The rare lore of Krusk.
     */
    public static ItemStack KruskLore = ItemManager.getInstance().getItem("krusk:Lore");

    /**
     * Gives the correct RPG item to correspond with the given vanilla item.
     */
    public static ItemStack updateArmor (Material material) {
        String itemId = "vanilla:";
        switch (material) {
            case GOLDEN_BOOTS, GOLDEN_LEGGINGS, GOLDEN_CHESTPLATE, GOLDEN_HELMET -> itemId += "Gold";
            case LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET -> itemId += "Leather";
            case IRON_BOOTS, IRON_LEGGINGS, IRON_CHESTPLATE, IRON_HELMET -> itemId += "Iron";
            case CHAINMAIL_BOOTS, CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET -> itemId += "Mail";
            case DIAMOND_BOOTS, DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET -> itemId += "Diamond";
            case NETHERITE_BOOTS, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE, NETHERITE_HELMET -> itemId += "Netherite";
        }
        switch (material) {
            case GOLDEN_BOOTS, IRON_BOOTS, CHAINMAIL_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS -> itemId += "Boots";
            case LEATHER_LEGGINGS,  IRON_LEGGINGS,  CHAINMAIL_LEGGINGS,  GOLDEN_LEGGINGS,  DIAMOND_LEGGINGS,  NETHERITE_LEGGINGS -> itemId += "Leggings";
            case LEATHER_CHESTPLATE,  IRON_CHESTPLATE,  CHAINMAIL_CHESTPLATE,  GOLDEN_CHESTPLATE,  DIAMOND_CHESTPLATE,  NETHERITE_CHESTPLATE -> itemId += "Chestplate";
            case LEATHER_HELMET,  IRON_HELMET,  CHAINMAIL_HELMET,  GOLDEN_HELMET,  DIAMOND_HELMET,  NETHERITE_HELMET -> itemId += "Helmet";
        }
        return ItemManager.getInstance().getItem(itemId);
    }

    /**
     * Creates an AttributeModifier with a unique UUID
     * @param a The attribute being modified.
     * @param value The desired value.
     * @param slot The slot this should apply to.
     * @return The AttributeModifier with a unique UUID.
     */
    public static AttributeModifier attributeModifier(Attribute a, double value, EquipmentSlot slot) {
        int mod = switch (a) {
            case GENERIC_MAX_HEALTH -> 1;
            case GENERIC_ARMOR -> 2;
            case GENERIC_ARMOR_TOUGHNESS -> 3;
            case GENERIC_ATTACK_DAMAGE -> 4;
            case GENERIC_KNOCKBACK_RESISTANCE -> 5;
            case GENERIC_MOVEMENT_SPEED -> 6;
            case GENERIC_LUCK -> 7;
//            case HORSE_JUMP_STRENGTH -> 8;
            case GENERIC_ATTACK_SPEED -> 9;
            case GENERIC_ATTACK_KNOCKBACK -> 10;
            case GENERIC_FLYING_SPEED -> 11;
            case GENERIC_FOLLOW_RANGE -> 12;
            case ZOMBIE_SPAWN_REINFORCEMENTS -> 13;
            default -> 0;
        };
        int slotN = switch (slot) {
            case FEET -> 1;
            case LEGS -> 2;
            case CHEST -> 3;
            case HEAD -> 4;
            case HAND -> 5;
            case OFF_HAND -> 6;
            case BODY -> 7;
        };
        return new AttributeModifier(new UUID(slotN, mod), a.toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
    }

    /**
     * Applies the given strings to the lore of the given meta.
     * @param m The meta which will have the lore.
     * @param lines The lines of lore.
     */
    public static void setLore(ItemMeta m, String[] lines) {
        ArrayList<String> l = new ArrayList<>();
        Collections.addAll(l, lines);
        m.setLore(l);
    }

    /**
     * Creates a custom item of the given material and name. Used to create items in line.
     *
     * @param m The material for the item.
     * @param i The number of items in the stack.
     * @param n The name of the item.
     */
    public static ItemStack createItem(Material m, int i, String n) { return createItem(m, i, n, new String[]{}); }

    /**
     * Creates a custom item of the given material, name, and lore. Used to created items in line. Using this generally
     * also reduces scope.
     *
     * @param m The material for the item.
     * @param i The number of items in the stack.
     * @param n The name of the item.
     * @param lines The lines of lore.
     */
    public static ItemStack createItem(Material m, int i, String n, String[] lines) {
        ItemStack r = new ItemStack(m, i);
        ItemMeta meta = r.getItemMeta();
        assert meta != null;
        setLore(meta, lines);
        meta.setDisplayName(n);
        meta.setUnbreakable(true);
        r.setItemMeta(meta);
        return r;
    }
}
