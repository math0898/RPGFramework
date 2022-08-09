package io.github.math0898.rpgframework.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RpgItem extends ItemStack {

    /**
     * A constant which specifies the maximum number of characters allowed per line in lore.
     */
    private static final short LINE_CHARACTER_LIMIT = 40;

    /**
     * An internal name to reference this RpgItem by. Used by mobs to equip and drop items.
     */
    private final String internalName;

    /**
     * Creates a new RpgItem by reading the file at the given path.
     *
     * @param path The path to the file to read when creating this item.
     */
    public RpgItem (String path) throws IOException {
        this(new File(path));
    }

    /**
     * Creates a new RpgItem by reading the given file.
     *
     * @param file The file to read when creating this item.
     */
    public RpgItem (File file) throws IOException { // todo MetadataValue <-- special effects, recipe?
        super(Material.DIRT, 1);
        StringBuilder json = new StringBuilder();
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) json.append(s.nextLine());
        JSONObject obj = new JSONObject(json.toString());
        internalName = obj.getString("internal_name");
        Material m = Material.getMaterial(obj.getString("material"));
        if (m != null) setType(m);
        setItemMeta(generateMeta(obj));
    }

    /**
     * Accessor method for the internal name being used by this RpgItem.
     *
     * @return The internal name of this RpgItem.
     */
    public String getInternalName () {
        return internalName;
    }

    /**
     * Assigns the meta of this RpgItem to the values specified in the given JSON object.
     *
     * @param json The JSON object to parse and use to create the ItemMeta.
     * @return The generated ItemMeta.
     */
    public ItemMeta generateMeta (JSONObject json) { // todo ItemFlag, customModelData, unbreakable system
        ItemMeta meta = getItemMeta();
        if (meta == null) Bukkit.getItemFactory().getItemMeta(getType());
        if (meta == null) return null;
        String name = json.getString("name");
        if (name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(generateLore(json));
        generateEnchants(json, meta);
        generateAttributes(json, meta);
        meta.setUnbreakable(json.getBoolean("unbreakable"));
        return meta;
    }

    /**
     * Adds the listed attributes to the given ItemMeta.
     *
     * @param json The JSON object to parse and use to determine attributes.
     * @param meta The ItemMeta to attach the attributes to.
     */
    public void generateAttributes (JSONObject json, ItemMeta meta) {
        JSONArray attributes;
        for (EquipmentSlot e : EquipmentSlot.values()) meta.removeAttributeModifier(e);
        try { attributes = json.getJSONArray("attributes");
        } catch (JSONException exception) { return; }
        for (int i = 0; i < attributes.length(); i++) {
            JSONObject a = attributes.getJSONObject(i);
            JSONObject mod = a.getJSONObject("modifier");
            try { meta.addAttributeModifier(Attribute.valueOf(a.getString("attribute")),
                    new AttributeModifier(UUID.nameUUIDFromBytes(mod.getString("name").getBytes()),
                            mod.getString("name"), mod.getDouble("amount"),
                            AttributeModifier.Operation.valueOf(mod.getString("operation")),
                            EquipmentSlot.valueOf(mod.getString("slot"))));
            } catch (JSONException ignored) { }
        }
    }

    /**
     * Adds the listed enchants to the given ItemMeta.
     *
     * @param json The JSON object to parse and use to determine enchants.
     * @param meta The ItemMeta to attach the enchantments to.
     */
    public void generateEnchants (JSONObject json, ItemMeta meta) {
        JSONArray enchants;
        try { enchants = json.getJSONArray("enchants");
        } catch (JSONException exception) { return; }
        for (int i = 0; i < enchants.length(); i++) {
            JSONObject e = enchants.getJSONObject(i);
            try {
                meta.addEnchant(findEnchantment(e.getString("enchantment")),
                        e.getInt("level"), true);
            } catch (NullPointerException | JSONException ignored) { }
        }
    }

    /**
     * Helpful utility method to get an enchantment from the general name.
     *
     * @param name The name of the enchantment to find.
     */
    private static Enchantment findEnchantment (String name) {
        for (Enchantment e : Enchantment.values()) if (e.getKey().toString().replace("minecraft:", "").equalsIgnoreCase(name)) return e;
        return Enchantment.DURABILITY;
    }

    /**
     * Generates the lore for an RpgItem from the given string.
     *
     * @param json The JSON object to parse and use to create the lore.
     * @return The sentence indented at the appropriate spots for nice display. Will be in List form.
     */
    public List<String> generateLore (JSONObject json) {
        String sentence;
        try { sentence = json.getString("lore");
        } catch (JSONException exception) { return null; }
        if (sentence == null) return null;
        String current = sentence;
        String prefix = "";
        for (int i = 0; i < current.length(); i += 2) {
            if (current.charAt(i) == '&') prefix = current.substring(0, i + 2);
            else break;
        }
        List<String> lore = new ArrayList<>();
        while (current.length() > LINE_CHARACTER_LIMIT) { // indent character amount
            int splice = LINE_CHARACTER_LIMIT - 1;
            for (int i = splice; i > 0; i--) if (current.charAt(i) == ' ' || current.charAt(i) == '\n') {
                splice = i;
                break;
            }
            lore.add(ChatColor.translateAlternateColorCodes('&',prefix + current.substring(0, splice)));
            current = current.substring(splice + 1);
        }
        lore.add(ChatColor.translateAlternateColorCodes('&',prefix + current));
        return lore;
    }
}
