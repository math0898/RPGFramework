package sugaku.rpg.framework.items;

import io.github.math0898.rpgframework.items.ItemManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

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
     * Returns the String name that should be used for the given item. This is a helper method.
     */
    public static String genName(char[] name) {

        StringBuilder r = new StringBuilder();

        for (int i = 0; i < name.length; i++) {
            if (name[i] == '_') r.append(' ');
            else if (i == 0) r.append(Character.toUpperCase(name[i]));
            else if (name[i - 1] == '_') r.append(Character.toUpperCase(name[i]));
            else r.append(name[i]);
        }

        return r.toString();
    }

    /**
     * Takes the given string and increases the rarity of the name. Checks to see if it should be increased too.
     * @param s The name to be upgraded.
     * @return The upgraded name.
     */
    public static String increaseRarity(String s) {

        if (s.contains("§k")) return s;

        String r = s;

        r = s.replace("§d", "§c§k-§r§c ");
        r = s.replace("§6", "§d§k-§r§d ");
        r = s.replace("§9", "§6§k-§r§6 ");
        r = s.replace("§a", "§9§k-§r§9 ");

        if (r.equals(s)) r = "§a§k-§r§a " + s;

        r += " §" + r.toCharArray()[1] +"§k-";

        return r;
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

    /**
     * Creates a custom item of the given material, name, lore, and array of attribute modifiers. Used to create items
     * in line. This method can significantly reduce scope.
     *
     * @param m The material for the item.
     * @param i The number of items in the stack.
     * @param n The name of the item.
     * @param lines The lines of lore.
     * @param attributes The attributes to be added to the item.
     */
    @Deprecated
    public static ItemStack createItem(Material m, int i, String n, String[] lines, AttributeModifier[] attributes) {
        ItemStack item = createItem(m, i, n, lines);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        // TODO: This is very specific to make RPG compile and run successfully.
        for (int j = 0; j < attributes.length; j++) {
            meta.addAttributeModifier(j == 0 ? Attribute.GENERIC_ATTACK_DAMAGE : Attribute.GENERIC_MOVEMENT_SPEED, attributes[j]);
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a leather armor item with the given dyes. It's implied that each item stack will only have one item.
     *
     * @param m The material for the item.
     * @param n The name of the item.
     * @param lines The lines of lore.
     * @param r The red of the dye.
     * @param g The green of the dye.
     * @param b The blue of the dye.
     */
    public static ItemStack createLeatherArmor(Material m, String n, String[] lines, int r, int g, int b) {
        if (m != Material.LEATHER_BOOTS && m != Material.LEATHER_LEGGINGS && m != Material.LEATHER_CHESTPLATE && m != Material.LEATHER_HELMET) return null;
        ItemStack item = createItem(m, 1, n, lines);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(r, g, b));
        item.setItemMeta(meta);
        return item;
    }
}
