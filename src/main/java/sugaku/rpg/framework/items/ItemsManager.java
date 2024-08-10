package sugaku.rpg.framework.items;

import io.github.math0898.rpgframework.items.ItemManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sugaku.rpg.mobs.teir1.eiryeras.EiryerasBoss;

import java.util.*;

import static io.github.math0898.rpgframework.RPGFramework.itemManager;

@Deprecated
public final class ItemsManager { // todo: AttributeModifiers should now be constructed with a NamespacedKey rather than UUID.

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
     * A map of items to helpfully convert between vanilla and RPG.
     */
    private static final Map<Material, ItemStack> itemMap = new HashMap<>();

    /**
     * A utility method to populate the ItemMap.
     */
    private static void populateItemMap () {
        ItemManager manager = ItemManager.getInstance();
        Material[] mats = new Material[]{ Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET,
                Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
                Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET,
                Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET,
                Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET};
        ItemStack[] items = new ItemStack[]{manager.getItem("vanilla:LeatherBoots"), manager.getItem("vanilla:LeatherLeggings"), manager.getItem("vanilla:LeatherChestplate"), manager.getItem("vanilla:LeatherHelmet"),
                manager.getItem("vanilla:IronBoots"), manager.getItem("vanilla:IronLeggings"), manager.getItem("vanilla:IronChestplate"), manager.getItem("vanilla:IronHelmet"),
                manager.getItem("vanilla:GoldBoots"), manager.getItem("vanilla:GoldLeggings"), manager.getItem("vanilla:GoldChestplate"), manager.getItem("vanilla:GoldHelmet"),
                manager.getItem("vanilla:DiamondBoots"), manager.getItem("vanilla:DiamondLeggings"), manager.getItem("vanilla:DiamondChestplate"), manager.getItem("vanilla:DiamondHelmet"),
                manager.getItem("vanilla:NetheriteBoots"), manager.getItem("vanilla:NetheriteLeggings"), manager.getItem("vanilla:NetheriteChestplate"), manager.getItem("vanilla:NetheriteHelmet")};
        for (int i = 0; i < mats.length; i++)
            itemMap.put(mats[i], items[i]);
    }

    /**
     * Updates vanilla armor to have the right meta and attributes.
     */
    @Deprecated // todo: Use recipe replacement.
    public static void updateArmor(ItemStack item) {
        if (item.getType().toString().contains("CHAINMAIL")) return;
        if (itemMap.isEmpty()) populateItemMap();
        item.setItemMeta(itemMap.get(item.getType()).getItemMeta());
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
}
