package sugaku.rpg.framework.items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import sugaku.rpg.mobs.teir1.eiryeras.EiryerasBoss;

import java.util.*;

import static io.github.math0898.rpgframework.RPGFramework.itemManager;
import static org.bukkit.attribute.AttributeModifier.Operation.*;

public final class ItemsManager {

    /**
     * The rare Dark Helmet custom item.
     */
    public static ItemStack DarkHelm = itemManager.getItem("other:HelmetOfDarkness");

    /**
     * The rare spawn item for Eiryeras.
     */
    public static ItemStack EiryerasSpawn = new ItemStack(Material.BEEF, 1);

    /**
     * The uncommon axe of Krusk custom item.
     */
    public static ItemStack KruskAxe = itemManager.getItem("krusk:KruskAxe");

    /**
     * The uncommon boots of Krusk custom item.
     */
    public static ItemStack KruskBoots = itemManager.getItem("krusk:KruskBoots");

    /**
     * The rare Krusk helmet.
     */
    public static ItemStack KruskHelmet =itemManager.getItem("krusk:KruskHelmet");

    /**
     * The uncommon leggings of Krusk custom item.
     */
    public static ItemStack KruskLeggings = itemManager.getItem("krusk:KruskLeggings");

    /**
     * The rare spawn item for Krusk.
     */ // todo: Kept for legacy items.
    public static ItemStack KruskSpawn = createItem(Material.WHEAT, 1, ChatColor.BLUE + "Krusk Boss Spawn", new String[]{
            ChatColor.GRAY + "Drop this item anywhere to spawn",
            ChatColor.GREEN + "Krusk, Undead General" + ChatColor.GRAY + " on the spot.",
            ChatColor.GRAY + "Attacking bosses whilst they are in",
            ChatColor.GRAY + "water will cause you to get struck",
            ChatColor.GRAY + "by lighting."});

    /**
     * The legendary spawn item for Feyrith.
     */ // todo: Kept for legacy items.
    public static ItemStack FeyrithSpawn = createItem(Material.GOLD_INGOT, 1, ChatColor.GOLD + "Feyrith Boss Spawn", new String[]{
            ChatColor.GRAY + "Drop this item anywhere to spawn",
            ChatColor.BLUE + "Feyrith, Apprentice Mage" + ChatColor.GRAY + " on the spot.",
            ChatColor.GRAY + "Attacking bosses whilst they are in",
            ChatColor.GRAY + "water will cause you to get struck",
            ChatColor.GRAY + "by lighting."});

    /**
     * The uncommon undead chestplate custom item.
     */
    public static ItemStack UndeadChestplate = itemManager.getItem("krusk:UndeadChestplate");

    /**
     * The rare lore of Krusk.
     */
    public static ItemStack KruskLore = itemManager.getItem("krusk:Lore");

    /**
     * An arraylist of all the custom items in the game.
     */
    public static ArrayList<ItemStack> items = new ArrayList<>();

    /**
     * Initializes all the custom items so they are good to go for spawning.
     */
    public static void init() {
        createEiryerasSpawn();

        items.add(DarkHelm);
        items.add(EiryerasSpawn);
        items.add(KruskAxe);
        items.add(KruskBoots);
        items.add(KruskHelmet);
        items.add(KruskLeggings);
        items.add(KruskSpawn);
        items.add(FeyrithSpawn);
        items.add(UndeadChestplate);
        items.add(KruskLore);
        items.addAll(EiryerasBoss.getBossItems());
    }

    /**
     * Returns all the items in the ItemsManager.
     */
    public static ArrayList<ItemStack> getItems() { return items; }

    /**
     * Creates the meta behind what makes an Eiryeras Boss spawn a spawn.
     */
    private static void createEiryerasSpawn() {
        ItemMeta meta = EiryerasSpawn.getItemMeta();
        assert meta != null;

        meta.setDisplayName(ChatColor.BLUE + "Eiryeras Boss Spawn");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Drop this item anywhere to spawn");
        lore.add(ChatColor.GREEN + "Eiryeras, Honored Hunter" + ChatColor.GRAY + " on the");
        lore.add(ChatColor.GRAY + "spot. Attacking bosses whilst they");
        lore.add(ChatColor.GRAY + "are in water will cause you to get");
        lore.add(ChatColor.GRAY + "struck by lighting.");

        meta.setLore(lore);
        EiryerasSpawn.setItemMeta(meta); // todo: Kept for legacy items.
    }

    /**
     * Updates vanilla armor to have the right meta and attributes.
     */
    public static void updateArmor(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        ArrayList<String> lore = new ArrayList<>();
        EquipmentSlot slot = EquipmentSlot.HAND;
        int slotUuid = 0;

        meta.setDisplayName(ChatColor.WHITE + genName(item.getType().toString().toLowerCase().toCharArray()));
        if (meta.hasEnchants()) meta.setDisplayName(increaseRarity(meta.getDisplayName()));

        switch(item.getType()) {
            case LEATHER_BOOTS: case IRON_BOOTS: case CHAINMAIL_BOOTS: case GOLDEN_BOOTS: case DIAMOND_BOOTS: case NETHERITE_BOOTS:
                slot = EquipmentSlot.FEET; slotUuid = 1; break;
            case LEATHER_LEGGINGS: case IRON_LEGGINGS: case CHAINMAIL_LEGGINGS: case GOLDEN_LEGGINGS: case DIAMOND_LEGGINGS: case NETHERITE_LEGGINGS:
                slot = EquipmentSlot.LEGS; slotUuid = 2; break;
            case LEATHER_CHESTPLATE: case IRON_CHESTPLATE: case CHAINMAIL_CHESTPLATE: case GOLDEN_CHESTPLATE: case DIAMOND_CHESTPLATE: case NETHERITE_CHESTPLATE:
                slot = EquipmentSlot.CHEST; slotUuid = 3; break;
            case LEATHER_HELMET: case IRON_HELMET: case CHAINMAIL_HELMET: case GOLDEN_HELMET: case DIAMOND_HELMET: case NETHERITE_HELMET:
                slot = EquipmentSlot.HEAD; slotUuid = 4; break;
        }

        meta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
        meta.removeAttributeModifier(Attribute.GENERIC_MAX_HEALTH);
        meta.removeAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS);
        meta.removeAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE);

        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(slotUuid,1), "generic.armor", calArmor(item.getType())/4.0, ADD_NUMBER, slot));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(slotUuid,2), "generic.health", calHealth(item.getType()), ADD_NUMBER, slot));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(new UUID(slotUuid, 3), "generic.armorToughness", calToughness(item.getType()), ADD_NUMBER, slot));
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(new UUID(slotUuid, 4), "generic.knockbackResist", calKnockback(item.getType()), ADD_NUMBER, slot));

        lore.add(ChatColor.GRAY + "Item modified by RPG - 1.2");
        lore.add("");
        lore.add("Modified Vanilla");
        meta.setLore(lore);
        item.setItemMeta(meta);
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
     * Returns the int value of the piece of armor. This is a helper method.
     */
    private static int calArmor(Material m) {
        return switch (m) {
            case LEATHER_BOOTS, LEATHER_HELMET, GOLDEN_BOOTS, CHAINMAIL_BOOTS -> 1;
            case LEATHER_LEGGINGS, IRON_BOOTS, GOLDEN_HELMET, CHAINMAIL_HELMET, IRON_HELMET -> 2;
            case LEATHER_CHESTPLATE, GOLDEN_LEGGINGS, DIAMOND_HELMET, DIAMOND_BOOTS, NETHERITE_BOOTS, NETHERITE_HELMET -> 3;
            case CHAINMAIL_LEGGINGS -> 4;
            case GOLDEN_CHESTPLATE, CHAINMAIL_CHESTPLATE, IRON_LEGGINGS -> 5;
            case IRON_CHESTPLATE, DIAMOND_LEGGINGS, NETHERITE_LEGGINGS -> 6;
            case DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE -> 8;
            default -> -1;
        };
    }

    /**
     * Calculates the int health value of the piece of armor. This is a helper method.
     */
    private static int calHealth(Material m) {
        return switch (m) {
            case LEATHER_HELMET, LEATHER_BOOTS, LEATHER_LEGGINGS -> 1;
            case LEATHER_CHESTPLATE -> 2;
            case GOLDEN_BOOTS, GOLDEN_LEGGINGS, GOLDEN_CHESTPLATE, GOLDEN_HELMET, CHAINMAIL_HELMET, CHAINMAIL_BOOTS -> 3;
            case CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE -> 4;
            case IRON_HELMET, IRON_BOOTS -> 5;
            case IRON_LEGGINGS -> 6;
            case IRON_CHESTPLATE -> 7;
            case NETHERITE_BOOTS, DIAMOND_BOOTS -> 8;
            case NETHERITE_HELMET, DIAMOND_HELMET -> 10;
            case NETHERITE_LEGGINGS, DIAMOND_LEGGINGS -> 12;
            case NETHERITE_CHESTPLATE, DIAMOND_CHESTPLATE -> 30;
            default -> -1;
        };
    }

    /**
     * Calculates the int armor toughness value of the piece of armor. This is a helper method.
     */
    private static int calToughness(Material m) {
        return switch (m) {
            case DIAMOND_BOOTS, DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET -> 2;
            case NETHERITE_BOOTS, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE, NETHERITE_HELMET -> 3;
            default -> 0;
        };
    }

    /**
     * Calculates the double knockback resistance value of a piece of armor. This is a helper method.
     */
    private static double calKnockback(Material m) {
        return switch (m) {
            case NETHERITE_BOOTS, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE, NETHERITE_HELMET -> 0.1;
            default -> 0;
        };
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
            case HORSE_JUMP_STRENGTH -> 8;
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
    public static ItemStack createItem(Material m, int i, String n, String[] lines, AttributeModifier[] attributes) {
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
