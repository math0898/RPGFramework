package sugaku.rpg.framework.items;

import io.github.math0898.rpgframework.items.ItemBuilder;
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
    public static ItemStack KruskSpawn = new ItemBuilder(Material.WHEAT, 1, ChatColor.BLUE + "Krusk Boss Spawn").setLore(new String[]{
            ChatColor.GRAY + "Drop this item anywhere to spawn",
            ChatColor.GREEN + "Krusk, Undead General" + ChatColor.GRAY + " on the spot.",
            ChatColor.GRAY + "Attacking bosses whilst they are in",
            ChatColor.GRAY + "water will cause you to get struck",
            ChatColor.GRAY + "by lighting."}).build();

    /**
     * The legendary spawn item for Feyrith.
     */
    public static ItemStack FeyrithSpawn = new ItemBuilder(Material.GOLD_INGOT, 1, ChatColor.GOLD + "Feyrith Boss Spawn").setLore(new String[]{
            ChatColor.GRAY + "Drop this item anywhere to spawn",
            ChatColor.BLUE + "Feyrith, Apprentice Mage" + ChatColor.GRAY + " on the spot.",
            ChatColor.GRAY + "Attacking bosses whilst they are in",
            ChatColor.GRAY + "water will cause you to get struck",
            ChatColor.GRAY + "by lighting."}).build();

    /**
     * The uncommon undead chestplate custom item.
     */
    public static ItemStack UndeadChestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);

    /**
     * The rare lore of Krusk.
     */
    public static ItemStack KruskLore = new ItemBuilder(Material.WHEAT, 1, ChatColor.GOLD + "Krusk's Lore").setLore(new String[]{
            ChatColor.GREEN + "Krusk " + ChatColor.GRAY + "was a general for a small",
            ChatColor.GRAY + "time noble named " + ChatColor.LIGHT_PURPLE + "Kairon" + ChatColor.GRAY + ". For",
            ChatColor.GRAY + "this noble, " + ChatColor.GREEN + "Krusk " + ChatColor.GRAY + "was given the",
            ChatColor.GRAY + "fairly easy task of defending",
            ChatColor.GRAY + "farmers from stray beasts."}).build();

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
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(new UUID(4, 1), "generic.health", 16, ADD_NUMBER, EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(new UUID(4,2), "generic.armor", 1.25, ADD_NUMBER, EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(new UUID(4,3), "generic.armorToughness", 2.25, ADD_NUMBER, EquipmentSlot.HEAD));

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
        lore.add(ChatColor.GRAY.toString());
        lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Issue Date: ");
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

        meta.setDisplayName(genName(item.getType().toString().toLowerCase().toCharArray()));
        if (meta.hasEnchants()) meta.setDisplayName(increaseRarity(meta.getDisplayName()));

        switch (item.getType()) {
            case LEATHER_BOOTS, IRON_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS -> {
                slot = EquipmentSlot.FEET;
                slotUuid = 1;
            }
            case LEATHER_LEGGINGS, IRON_LEGGINGS, CHAINMAIL_LEGGINGS, GOLDEN_LEGGINGS, DIAMOND_LEGGINGS, NETHERITE_LEGGINGS -> {
                slot = EquipmentSlot.LEGS;
                slotUuid = 2;
            }
            case LEATHER_CHESTPLATE, IRON_CHESTPLATE, CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE, DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE -> {
                slot = EquipmentSlot.CHEST;
                slotUuid = 3;
            }
            case LEATHER_HELMET, IRON_HELMET, CHAINMAIL_HELMET, GOLDEN_HELMET, DIAMOND_HELMET, NETHERITE_HELMET -> {
                slot = EquipmentSlot.HEAD;
                slotUuid = 4;
            }
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
}
