package io.github.math0898.rpgframework.items;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Utility class used to make item creation easier.
 *
 * @author Sugaku
 */
public class ItemUtility {

    /**
     * Applies the given strings to the lore of the given meta.
     *
     * @param m The meta which will have the lore.
     * @param lines The lines of lore.
     */
    public static void setLore (ItemMeta m, String[] lines) {
        ArrayList<String> l = new ArrayList<>();
        Collections.addAll(l, lines);
        m.setLore(l);
    }

    /**
     * Creates a custom item of the given material, name, lore, and unbreakable flag.
     *
     * @param m The material for the item.
     * @param i The number of items in the stack.
     * @param n The name of the item.
     * @param unbreakable Whether this item should be unbreakable or not.
     */
    public static ItemStack createItem (Material m, int i, String n, boolean unbreakable) {
        return createItem(m, i, n, new String[]{}, unbreakable);
    }

    /**
     * Creates a custom item of the given material and name. Used to create items in line.
     *
     * @param m The material for the item.
     * @param i The number of items in the stack.
     * @param n The name of the item.
     */
    public static ItemStack createItem (Material m, int i, String n) { return createItem(m, i, n, new String[]{}); }

    /**
     * Creates a custom item of the given material, name, and lore. Used to created items in line. Using this generally
     * also reduces scope.
     *
     * @param m The material for the item.
     * @param i The number of items in the stack.
     * @param n The name of the item.
     * @param lines The lines of lore.
     */
    public static ItemStack createItem (Material m, int i, String n, String[] lines) {
        return createItem(m, i, n, lines, true);
    }

    /**
     * Creates a custom item of the given material, name, lore, and unbreakable flag. Used to created items in line.
     * Using this generally also reduces scope.
     *
     * @param m The material for the item.
     * @param i The number of items in the stack.
     * @param n The name of the item.
     * @param lines The lines of lore.
     * @param unbreakable Whether this item should be unbreakable or not.
     */
    public static ItemStack createItem (Material m, int i, String n, String[] lines, boolean unbreakable) {
        ItemStack r = new ItemStack(m, i);
        ItemMeta meta = r.getItemMeta();
        assert meta != null;
        setLore(meta, lines);
        meta.setDisplayName(n);
        meta.setUnbreakable(unbreakable);
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
    public static ItemStack createItem (Material m, int i, String n, String[] lines, AttributeModifier[] attributes) {
        ItemStack item = createItem(m, i, n, lines);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        for (AttributeModifier a: attributes) meta.addAttributeModifier(Attribute.valueOf(a.getName()), a);
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
    public static ItemStack createLeatherArmor (Material m, String n, String[] lines, int r, int g, int b) {
        if (m != Material.LEATHER_BOOTS && m != Material.LEATHER_LEGGINGS && m != Material.LEATHER_CHESTPLATE && m != Material.LEATHER_HELMET) return null;
        ItemStack item = createItem(m, 1, n, lines);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(r, g, b));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a leather armor item with the given dyes. This also handles the adding attributes to the leather armor.
     *
     * @param m The material for the item.
     * @param n The name of the item.
     * @param lines The lines of lore.
     * @param r The red of the dye.
     * @param g The green of the dye.
     * @param b The blue of the dye.
     * @param attributes The attributes to be added to the item.
     */
    public static ItemStack createLeatherArmor (Material m, String n, String[] lines, int r, int g, int b, AttributeModifier[] attributes) {
        ItemStack item = createLeatherArmor(m, n, lines, r, g, b);
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        for (AttributeModifier a: attributes) meta.addAttributeModifier(Attribute.valueOf(a.getName()), a);
        item.setItemMeta(meta);
        return item;
    }
}
