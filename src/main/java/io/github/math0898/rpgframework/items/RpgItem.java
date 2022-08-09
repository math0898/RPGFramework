package io.github.math0898.rpgframework.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONObject;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public ItemMeta generateMeta (JSONObject json) { // todo enchant, attribute, ItemFlag, customModelData, unbreakable system
        ItemMeta meta = getItemMeta();
        if (meta == null) Bukkit.getItemFactory().getItemMeta(getType());
        if (meta == null) return null;
        String name = json.getString("name");
        if (name != null) meta.setDisplayName(name);
        meta.setLore(generateLore(json.getString("lore"))); // todo json.get will throw exception if not found... not null.
        meta.setUnbreakable(json.getBoolean("unbreakable"));
        return meta;
    }

    /**
     * Generates the lore for an RpgItem from the given string.
     *
     * @param sentence The sentence to indent to create into a List<String> for lore.
     * @return The sentence indented at the appropriate spots for nice display. Will be in List form.
     */
    public List<String> generateLore (String sentence) {
        if (sentence == null) return null;
        String current = sentence;
        List<String> lore = new ArrayList<>();
        while (current.length() > LINE_CHARACTER_LIMIT) { // indent character amount
            int splice = LINE_CHARACTER_LIMIT - 1;
            for (int i = splice; i > 0; i--) if (current.charAt(i) == ' ') {
                splice = i;
                break;
            }
            lore.add(current.substring(0, splice));
            current = current.substring(splice);
        }
        lore.add(current);
        return lore;
    }
}
