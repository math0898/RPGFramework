package sugaku.rpg.framework.menus.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import io.github.math0898.rpgframework.classes.Classes;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.menus.Menu;
import sugaku.rpg.framework.players.PlayerManager;

import java.util.Objects;

import static sugaku.rpg.framework.menus.ClassesManager.*;
import static sugaku.rpg.framework.menus.ClassesManager.classMenu;

public class PyromancerMenu extends ClassSubmenu implements Menu {

    @Override
    public void onClick(int clicked, Player player) {
        switch(clicked) {
            case 49: classMenu(player, "main"); break;
//            case 12: classMenu(player, "Haste");
//            case 14: classMenu(player, "Rage");
//            case 16: classMenu(player, "Brute Strength");
            case 28: Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).joinClass(Classes.PYROMANCER); classMenu(player, "Pyromancer"); break;
//            case 32: classMenu(player, "Indomitable Spirit");
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, goBack);

        int classPoints = getClassPoints(Classes.PYROMANCER);
        int level = getClassLvl(Classes.PYROMANCER);
        int xp = getClassXp(Classes.PYROMANCER);

        inv.setItem(10, ItemsManager.createItem(Material.BLAZE_POWDER, Math.max(1, classPoints), ChatColor.DARK_GREEN + "Class points: " + classPoints, new String[]{
                ChatColor.GRAY + "Spend class points to upgrade",
                ChatColor.GRAY + "class abilities and passives.",
                ChatColor.GRAY + "One point is given per level."}));
        inv.setItem(12, ItemsManager.createItem(Material.FIRE_CHARGE, 1, ChatColor.DARK_AQUA + "Fireball", new String[]{
                ChatColor.GRAY + "Fire a fireball, small or large.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Small Strength: " + ChatColor.GREEN + 1, //TODO: add player upgrades
                ChatColor.GRAY + "- Small Cooldown: " + ChatColor.GREEN + 3 + "s",
                ChatColor.GRAY + "- Big Strength: " + ChatColor.GREEN + 3,
                ChatColor.GRAY + "- Big Cooldown: " + ChatColor.GREEN + 5 + "s"}));
        inv.setItem(14, ItemsManager.createItem(Material.BLAZE_ROD, 1, ChatColor.DARK_AQUA + "Fireball Barrage", new String[]{
                ChatColor.GRAY + "Fire an onslaught of fireballs.",
                ChatColor.GRAY + "Uses the strength of a single",
                ChatColor.GRAY + "fireball attack.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Small Count: " + ChatColor.GREEN + 3,
                ChatColor.GRAY + "- Small Cooldown: " + ChatColor.GREEN + 10 + "s", //TODO: add player upgrades
                ChatColor.GRAY + "- Big Count: " + ChatColor.GREEN + 3,
                ChatColor.GRAY + "- Big Cooldown: " + ChatColor.GREEN + 15 + "s"}));
        inv.setItem(16, ItemsManager.createItem(Material.MAGMA_CREAM, 1, ChatColor.LIGHT_PURPLE + "Bathed in Fire", new String[]{
                ChatColor.GRAY + "Spending soo much time around the",
                ChatColor.GRAY + "flame you've developed a strong",
                ChatColor.GRAY + "resistance to it."}));

        if (Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass() == Classes.PYROMANCER) {
            inv.setItem(28, ItemsManager.createItem(Material.LAPIS_BLOCK, 1, ChatColor.BLUE + "Lvl: " + level, new String[]{
                    ChatColor.GRAY + "Slay bosses and RPG mobs to",
                    ChatColor.GRAY + "gain xp for your active class.", "",
                    ChatColor.GRAY + "Xp until next level: " + ChatColor.GREEN + ((level*300) - xp), /*TODO: check that this equation works*/}));
        } else {
            inv.setItem(28, ItemsManager.createItem(Material.EMERALD_BLOCK, 1, ChatColor.GREEN + "" + ChatColor.BOLD + "Join the class", new String[]{
                    ChatColor.GRAY + "Click me to join the pyromancer",
                    ChatColor.GRAY + "class! You will leave your",
                    ChatColor.GRAY + "current class."}));
        }

        inv.setItem(32, ItemsManager.createItem(Material.TOTEM_OF_UNDYING, 1, ChatColor.GOLD + "Phoenix Renewal", new String[]{
                ChatColor.GRAY + "Call upon the power of the phoenix",
                ChatColor.GRAY + "to burn your enemies and revitalize",
                ChatColor.GRAY + "yourself.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 15 + "s", //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 180 + "s",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 5}));
    }
}
