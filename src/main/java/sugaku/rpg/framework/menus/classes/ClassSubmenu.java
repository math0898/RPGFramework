package sugaku.rpg.framework.menus.classes;

import io.github.math0898.rpgframework.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.classes.Classes;

public abstract class ClassSubmenu {

    /**
     * The name of the class this submenu is for.
     */
    protected final String className;

    /**
     * The block to display when there is an option to join a class.
     */
    protected final ItemStack joinClass;

    /**
     * The block to display when the player is part of this class. Includes information on xp.
     */
    protected final ItemStack classLvl;

    /**
     * Creates a new class submenu with the given class name and enum value.
     *
     * @param className The name of the class this submenu is for.
     * @param c The class enum value of this class.
     */
    public ClassSubmenu (String className, Classes c) {
        this.className = className; // TODO: These menus require a refactor to involve player xp levels. We will likely need to call a function with RpgPlayer parameter which constructs these.
        joinClass = new ItemBuilder(Material.EMERALD_BLOCK, ChatColor.GREEN.toString() + ChatColor.BOLD + "Join the class").setLore(new String[]{
                ChatColor.GRAY + "Click me to join the " + className,
                ChatColor.GRAY + "class! You will leave your",
                ChatColor.GRAY + "current class."}).build();
        classLvl = new ItemBuilder(Material.LAPIS_BLOCK, ChatColor.BLUE + "Lvl: " + getClassLvl(c)).setLore(new String[]{
                ChatColor.GRAY + "Slay bosses and RPG mobs to",
                ChatColor.GRAY + "gain xp for your active class.", "",
                ChatColor.GRAY + "Xp until next level: " + ChatColor.GREEN + ((getClassLvl(c) * 300) - getClassXp(c)), /*TODO: check that this equation works*/}).build();
    }

    public int getClassXp(Classes c) {
        return 0; //TODO
    }

    public int getClassPoints(Classes c) {
        return 0; //TODO
    }

    public int getClassLvl(Classes c) {
        return 1; //TODO
    }
}
