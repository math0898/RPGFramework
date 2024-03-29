package sugaku.rpg.framework.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.menus.classes.*;

import static sugaku.rpg.main.plugin;

/**
 * The classes manager describes the menus used in the /classes or /rpg classes command. Submenus are described under
 * the classes package in the same directory.
 */
public class ClassesManager {

    /**
     * The title of the inventory and the name of the helper item in the main inventory. Additionally serves as the
     * prefix for other submenus.
     */
    public static final String title = ChatColor.BLUE + "" + ChatColor.BOLD + "Classes Menu";

    public  static final ItemStack goBack = ItemsManager.createItem(Material.ARROW, 1, ChatColor.RED + "" + ChatColor.BOLD + "Go back");

    public static final ItemStack fill = ItemsManager.createItem(Material.BLACK_STAINED_GLASS_PANE, 1, " ");

    private static final Menu MainMenu = new MainMenu();

    private static final Menu AssassinMenu = new AssassinMenu();

    private static final Menu BardMenu = new BardMenu();

    private static final Menu BerserkerMenu = new BerserkerMenu();

    private static final Menu PaladinMenu = new PaladinMenu();

    private static final Menu PyromancerMenu = new PyromancerMenu();

    public static void classMenu(Player p) { classMenu(p, "main"); }

    public static void classMenu (Player p, String menu) {

        Inventory i;

        if (!menu.equals("main") && !menu.equals("None")) i = plugin.getServer().createInventory(p.getPlayer(), 54, title + ChatColor.DARK_GRAY + ": " + menu);
        else i = plugin.getServer().createInventory(p.getPlayer(), 54, title);

        switch(menu) {
            case "main": case "None": MainMenu.build(i, p); break;
            case "Assassin": AssassinMenu.build(i, p); break;
            case "Bard": BardMenu.build(i, p); break;
            case "Berserker": BerserkerMenu.build(i, p); break;
            case "Paladin": PaladinMenu.build(i, p); break;
            case "Pyromancer": PyromancerMenu.build(i, p); break;
        }

        p.openInventory(i);
    }

    public static void classClicked (InventoryClickEvent event) {

        int clicked = event.getSlot();
        String current = event.getView().getTitle();

        Player player = (Player) event.getWhoClicked();

        if (current.equals(title)) MainMenu.onClick(clicked, player);
        else if (current.contains("Assassin")) AssassinMenu.onClick(clicked, player);
        else if (current.contains("Bard")) BardMenu.onClick(clicked, player);
        else if (current.contains("Berserker")) BerserkerMenu.onClick(clicked, player);
        else if (current.contains("Paladin")) PaladinMenu.onClick(clicked, player);
        else if (current.contains("Pyromancer")) PyromancerMenu.onClick(clicked, player);

        event.setCancelled(true);
    }
}
