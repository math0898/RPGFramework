package sugaku.rpg.framework.items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sugaku.rpg.mobs.teir1.eiryeras.EiryerasBoss;

import java.util.*;

import static org.bukkit.attribute.AttributeModifier.Operation.*;

public final class ItemsManager {

    /**
     * An arraylist which contains all the boss spawn items.
     */
    private static final ArrayList<ItemStack> bossSpawns = new ArrayList<>();

    /**
     * The rare Dark Helmet custom item.
     */
    public static ItemStack DarkHelm = new ItemStack(Material.LEATHER_HELMET,1);

    /**
     * The rare spawn item for Eiryeras.
     */
    public static ItemStack EiryerasSpawn = new ItemStack(Material.BEEF, 1);

    /**
     * The uncommon axe of Krusk custom item.
     */
    public static ItemStack KruskAxe = new ItemStack(Material.IRON_AXE, 1);

    /**
     * The uncommon boots of Krusk custom item.
     */
    public static ItemStack KruskBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);

    /**
     * The rare Krusk helmet.
     */
    public static ItemStack KruskHelmet = new ItemStack(Material.DIAMOND_HELMET, 1);

    /**
     * The uncommon leggings of Krusk custom item.
     */
    public static ItemStack KruskLeggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);

    /**
     * The rare spawn item for Krusk.
     */
    public static ItemStack KruskSpawn = createItem(Material.WHEAT, 1, ChatColor.BLUE + "Krusk Boss Spawn", new String[]{
            ChatColor.GRAY + "Drop this item anywhere to spawn",
            ChatColor.GREEN + "Krusk, Undead General" + ChatColor.GRAY + " on the spot.",
            ChatColor.GRAY + "Attacking bosses whilst they are in",
            ChatColor.GRAY + "water will cause you to get struck",
            ChatColor.GRAY + "by lighting."});

    /**
     * The legendary spawn item for Feyrith.
     */
    public static ItemStack FeyrithSpawn = createItem(Material.GOLD_INGOT, 1, ChatColor.GOLD + "Feyrith Boss Spawn", new String[]{
            ChatColor.GRAY + "Drop this item anywhere to spawn",
            ChatColor.BLUE + "Feyrith, Apprentice Mage" + ChatColor.GRAY + " on the spot.",
            ChatColor.GRAY + "Attacking bosses whilst they are in",
            ChatColor.GRAY + "water will cause you to get struck",
            ChatColor.GRAY + "by lighting."});

    /**
     * The uncommon undead chestplate custom item.
     */
    public static ItemStack UndeadChestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);

    /**
     * The rare lore of Krusk.
     */
    public static ItemStack KruskLore = createItem(Material.WHEAT, 1, ChatColor.GOLD + "Krusk's Lore", new String[]{
            ChatColor.GREEN + "Krusk " + ChatColor.GRAY + "was a general for a small",
            ChatColor.GRAY + "time noble named " + ChatColor.LIGHT_PURPLE + "Kairon" + ChatColor.GRAY + ". For",
            ChatColor.GRAY + "this noble, " + ChatColor.GREEN + "Krusk " + ChatColor.GRAY + "was given the",
            ChatColor.GRAY + "fairly easy task of defending",
            ChatColor.GRAY + "farmers from stray beasts."});

    /**
     * An arraylist of all the custom items in the game.
     */
    public static ArrayList<ItemStack> items = new ArrayList<>();

    /**
     * Initializes all the custom items so they are good to go for spawning.
     */
    public static void init() {
        createDarkHelm();
        createKruskAxe();
        createKruskBoots();
        createKruskHelmet();
        createKruskLeggings();
        createEiryerasSpawn();
        createUndeadChestplate();

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

        bossSpawns.add(FeyrithSpawn);
        bossSpawns.add(KruskSpawn);
        bossSpawns.add(EiryerasSpawn);
    }

    /**
     * Returns all the items in the ItemsManager.
     */
    public static ArrayList<ItemStack> getItems() { return items; }

    /**
     * Creates the meta behind what makes a Dark Helmet a Dark Helmet.
     */
    private static void createDarkHelm() {

        LeatherArmorMeta meta = (LeatherArmorMeta) DarkHelm.getItemMeta();
        assert meta != null;

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(4, 2), "generic.armor", 0.75, ADD_NUMBER, EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(4,1), "generic.health", 8, ADD_NUMBER, EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new UUID(4,5), "generic.attack.damage", 2, ADD_NUMBER, EquipmentSlot.HEAD));

        meta.setDisplayName(ChatColor.BLUE + "Helmet of Darkness");
        meta.setColor(Color.BLACK);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "The helmet of darkness allows one to");
        lore.add(ChatColor.GRAY + "see into" + ChatColor.BLACK + ChatColor.MAGIC + "The Abyss" + ChatColor.GRAY + ", granting them some");
        lore.add(ChatColor.GRAY + "resistances and strength at a cost.");
        meta.setLore(lore);

        DarkHelm.setItemMeta(meta);
    }

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
        EiryerasSpawn.setItemMeta(meta);
    }

    /**
     * Creates the meta behind what makes a Krusk axe a Krusk axe.
     */
    private static void createKruskAxe() {

        ItemMeta meta = KruskAxe.getItemMeta();
        assert meta != null;

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(new UUID(5, 4), "generic.damage", 12, ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(new UUID(5,5), "generic.attackSpeed", -3, ADD_NUMBER, EquipmentSlot.HAND));

        meta.setDisplayName(ChatColor.GREEN + "Krusk's Axe");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "During life " + ChatColor.GREEN + "Krusk" + ChatColor.GRAY + " was a human general.");
        lore.add(ChatColor.GRAY + "He was not particularly good at what");
        lore.add(ChatColor.GRAY + "he did but now he gives adventurers");
        lore.add(ChatColor.GRAY + "a hard time anyways.");
        meta.setLore(lore);

        KruskAxe.setItemMeta(meta);
    }

    /**
     * Creates the meta behind what makes a Krusk Boot a Krusk Boot.
     */
    public static void createKruskBoots() {
        ItemMeta meta = KruskBoots.getItemMeta();
        assert meta != null;

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(1, 1), "generic.health", 12, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(1,2), "generic.armor", 1, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(new UUID(1,3), "generic.armorToughness", 2, ADD_NUMBER, EquipmentSlot.FEET));

        meta.setDisplayName(ChatColor.GREEN + "Krusk's Boots");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "These boots were given to " + ChatColor.GREEN + "Krusk");
        lore.add(ChatColor.GRAY + "by " + ChatColor.LIGHT_PURPLE + "Krusk's Mom " + ChatColor.GRAY + "to celebrate his");
        lore.add(ChatColor.GRAY + "promotion to general. Wear them");
        lore.add(ChatColor.GRAY + "well.");
        meta.setLore(lore);

        KruskBoots.setItemMeta(meta);
    }

    /**
     * Creates the meta behind what makes Krusk's trusty helmet so trusty.
     */
    public static void createKruskHelmet() {
        ItemMeta meta = KruskHelmet.getItemMeta();
        assert meta != null;

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(4, 1), "generic.health", 16, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(4,2), "generic.armor", 1.25, ADD_NUMBER, EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(new UUID(4,3), "generic.armorToughness", 2.25, ADD_NUMBER, EquipmentSlot.FEET));

        meta.setDisplayName(ChatColor.BLUE + "Krusk's Trusty Helmet");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "This is " + ChatColor.GREEN + "Krusk's " + ChatColor.GRAY + "helmet from life. It");
        lore.add(ChatColor.GRAY + "was his good luck charm. If only he");
        lore.add(ChatColor.GRAY + "was wearing it that fateful day...");
        lore.add(ChatColor.GRAY + "maybe... " + ChatColor.GREEN + "Krusk "+ ChatColor.GRAY + "would've lived long");
        lore.add(ChatColor.GRAY + "enough to save enough pevsar to");
        lore.add(ChatColor.GRAY + "buy his own house.");
        meta.setLore(lore);

        KruskHelmet.setItemMeta(meta);
    }

    /**
     * Creates the meta behind what makes Krusk Leggings, Krusk Leggings.
     */
    public static void createKruskLeggings() {
        ItemMeta meta = KruskLeggings.getItemMeta();
        assert meta != null;

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(2, 1), "generic.health", 5, ADD_NUMBER, EquipmentSlot.LEGS));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(2,2), "generic.armor", 0.75, ADD_NUMBER, EquipmentSlot.LEGS));

        meta.setDisplayName(ChatColor.GREEN + "Krusk's Leggings");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Krusk " + ChatColor.GRAY + "has always been very keen");
        lore.add(ChatColor.GRAY + "on staying flexible. Leather pants");
        lore.add(ChatColor.GRAY + "suited this goal nicely allowing him");
        lore.add(ChatColor.GRAY + "to practice stretches on the job.");
        meta.setLore(lore);

        KruskLeggings.setItemMeta(meta);
    }

    /**
     * Creates the meta behind what makes Krusk Chestplate, Krusk Chestplate.
     */
    public static void createUndeadChestplate() {
        ItemMeta meta = UndeadChestplate.getItemMeta();
        assert meta != null;

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(3, 1), "generic.health", 10, ADD_NUMBER, EquipmentSlot.CHEST));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(3,2), "generic.armor", 1.75, ADD_NUMBER, EquipmentSlot.CHEST));

        meta.setDisplayName(ChatColor.GREEN + "Undead Chestplate");
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "This chestplate, forged from iron,");
        lore.add(ChatColor.GRAY + "will defend the user against most");
        lore.add(ChatColor.GRAY + "attacks they may face.");
        lore.add(ChatColor.GRAY + "");
        lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Issue Date: ");
        lore.add(ChatColor.WHITE + "                I5 - Pretits - Ve");
        meta.setLore(lore);

        UndeadChestplate.setItemMeta(meta);
    }

    /**
     * Frequent effects of custom items.
     */
    public static void updateEffects(Player p) {

        if (p == null) return;

        if (Objects.equals(p.getInventory().getHelmet(), DarkHelm)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 62000, 255, false, true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 62000, 255, false, true));
        } else {
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
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

        switch(m) {
            case LEATHER_BOOTS: case LEATHER_HELMET: case GOLDEN_BOOTS: case CHAINMAIL_BOOTS: return 1;
            case LEATHER_LEGGINGS: case IRON_BOOTS: case GOLDEN_HELMET: case CHAINMAIL_HELMET: case IRON_HELMET: return 2;
            case LEATHER_CHESTPLATE: case GOLDEN_LEGGINGS: case DIAMOND_HELMET: case DIAMOND_BOOTS: case NETHERITE_BOOTS: case NETHERITE_HELMET: return 3;
            case CHAINMAIL_LEGGINGS: return 4;
            case GOLDEN_CHESTPLATE: case CHAINMAIL_CHESTPLATE: case IRON_LEGGINGS: return 5;
            case IRON_CHESTPLATE: case DIAMOND_LEGGINGS: case NETHERITE_LEGGINGS: return 6;
            case DIAMOND_CHESTPLATE: case NETHERITE_CHESTPLATE: return 8;
        }

        return -1;
    }

    /**
     * Calculates the int health value of the piece of armor. This is a helper method.
     */
    private static int calHealth(Material m) {

        switch(m) {
            case LEATHER_HELMET: case LEATHER_BOOTS: case LEATHER_LEGGINGS: return 1;
            case LEATHER_CHESTPLATE: return 2;
            case GOLDEN_BOOTS: case GOLDEN_LEGGINGS: case GOLDEN_CHESTPLATE: case GOLDEN_HELMET: case CHAINMAIL_HELMET: case CHAINMAIL_BOOTS: return 3;
            case CHAINMAIL_LEGGINGS: case CHAINMAIL_CHESTPLATE: return 4;
            case IRON_HELMET: case IRON_BOOTS: return 5;
            case IRON_LEGGINGS: return 6;
            case IRON_CHESTPLATE: return 7;
            case NETHERITE_BOOTS: case DIAMOND_BOOTS: return 8;
            case NETHERITE_HELMET: case DIAMOND_HELMET: return 10;
            case NETHERITE_LEGGINGS: case DIAMOND_LEGGINGS: return 12;
            case NETHERITE_CHESTPLATE: case DIAMOND_CHESTPLATE: return 30;
        }

        return -1;
    }

    /**
     * Calculates the int armor toughness value of the piece of armor. This is a helper method.
     */
    private static int calToughness(Material m) {

        switch(m) {
            case DIAMOND_BOOTS: case DIAMOND_LEGGINGS: case DIAMOND_CHESTPLATE: case DIAMOND_HELMET: return 2;
            case NETHERITE_BOOTS: case NETHERITE_LEGGINGS: case NETHERITE_CHESTPLATE: case NETHERITE_HELMET: return 3;
        }

        return 0;
    }

    /**
     * Calculates the double knockback resistance value of a piece of armor. This is a helper method.
     */
    private static double calKnockback(Material m) {

        switch(m) {
            case NETHERITE_BOOTS: case NETHERITE_LEGGINGS: case NETHERITE_CHESTPLATE: case NETHERITE_HELMET: return 0.1;
        }

        return 0;
    }

    /**
     * Creates an AttributeModifier with a unique UUID
     * @param a The attribute being modified.
     * @param value The desired value.
     * @param slot The slot this should apply to.
     * @return The AttributeModifier with a unique UUID.
     */
    public static AttributeModifier attributeModifier(Attribute a, double value, EquipmentSlot slot) {
        int mod = -1;
        switch (a) {
            case GENERIC_MAX_HEALTH: mod = 1; break;
            case GENERIC_ARMOR: mod = 2; break;
            case GENERIC_ARMOR_TOUGHNESS: mod = 3; break;
            case GENERIC_ATTACK_DAMAGE: mod = 4; break;
            case GENERIC_KNOCKBACK_RESISTANCE: mod = 5; break;
            case GENERIC_MOVEMENT_SPEED: mod = 6; break;
            case GENERIC_LUCK: mod = 7; break;
            case HORSE_JUMP_STRENGTH: mod = 8; break;
            case GENERIC_ATTACK_SPEED: mod = 9; break;
            case GENERIC_ATTACK_KNOCKBACK: mod = 10; break;
            case GENERIC_FLYING_SPEED: mod = 11; break;
            case GENERIC_FOLLOW_RANGE: mod = 12; break;
            case ZOMBIE_SPAWN_REINFORCEMENTS: mod = 13; break;
        }
        int slotN = -1;
        switch (slot) {
            case FEET: slotN = 1; break;
            case LEGS: slotN = 2; break;
            case CHEST: slotN = 3; break;
            case HEAD: slotN = 4; break;
            case HAND: slotN = 5; break;
            case OFF_HAND: slotN = 6; break;
        }
        return new AttributeModifier(new UUID(slotN, mod), a.toString(), value, AttributeModifier.Operation.ADD_NUMBER, slot);
    }

    /**
     * Returns the arraylist of items that are considered boss spawns.
     */
    public static ArrayList<ItemStack> getBossSpawns() { return bossSpawns; }

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
    public static ItemStack createLeatherArmor(Material m, String n, String[] lines, int r, int g, int b, AttributeModifier[] attributes) {
        ItemStack item = createLeatherArmor(m, n, lines, r, g, b);
        if (item == null) return null;
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        for (AttributeModifier a: attributes) meta.addAttributeModifier(Attribute.valueOf(a.getName()), a);
        item.setItemMeta(meta);
        return item;
    }
}
