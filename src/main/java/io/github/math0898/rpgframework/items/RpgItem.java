package io.github.math0898.rpgframework.items;

import org.json.JSONObject;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class RpgItem extends ItemStack {

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
    public RpgItem (File file) throws IOException {
        StringBuilder json = new StringBuilder();
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) json.append(s.nextLine());
        JSONObject obj = new JSONObject(json.toString());
        obj.getString("");
    }
}
