package sugaku.rpg.framework.menus.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import sugaku.rpg.framework.classes.Classes;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.menus.Menu;
import sugaku.rpg.framework.players.PlayerManager;

import java.util.Objects;

import static sugaku.rpg.framework.menus.ClassesManager.*;
import static sugaku.rpg.framework.menus.ClassesManager.classMenu;

public class BerserkerMenu extends ClassSubmenu implements Menu {

    @Override
    public void onClick(int clicked, Player player) {
        switch(clicked) {
            case 49: classMenu(player, "main"); break;
//            case 12: classMenu(player, "Haste");
//            case 14: classMenu(player, "Rage");
//            case 16: classMenu(player, "Brute Strength");
            case 28: Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).joinClass(Classes.BERSERKER); classMenu(player, "Berserker"); break;
//            case 31: classMenu(player, "Indomitable Spirit");
//            case 33: classMenu(player, "Towering Form");
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, goBack);

        int classPoints = getClassPoints(Classes.BERSERKER);
        int level = getClassLvl(Classes.BERSERKER);
        int xp = getClassXp(Classes.BERSERKER);

        inv.setItem(10, ItemsManager.createItem(Material.ROTTEN_FLESH, Math.max(1, classPoints), ChatColor.DARK_GREEN + "Class points: " + classPoints, new String[]{
                ChatColor.GRAY + "Spend class points to upgrade",
                ChatColor.GRAY + "class abilities and passives.",
                ChatColor.GRAY + "One point is given per level."}));
        inv.setItem(12, ItemsManager.createItem(Material.SUGAR, 1, ChatColor.DARK_AQUA + "Haste", new String[]{
                ChatColor.GRAY + "Will your legs to run faster to",
                ChatColor.GRAY + "catch up to fleeing foes.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 2, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s",
                ChatColor.GRAY + "- Cooldown: 30s"}));
        inv.setItem(14, ItemsManager.createItem(Material.BLAZE_POWDER, 1, ChatColor.DARK_AQUA + "Rage", new String[]{
                ChatColor.GRAY + "Temporarily rage your attacks to",
                ChatColor.GRAY + "deal significantly more damage.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 2, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s",
                ChatColor.GRAY + "- Cooldown: 60s"}));
        inv.setItem(16, ItemsManager.createItem(Material.IRON_AXE, 1, ChatColor.LIGHT_PURPLE + "Brute Strength", new String[]{
                ChatColor.GRAY + "Mowing down enemies on the battle",
                ChatColor.GRAY + "field has trained you well with the",
                ChatColor.GRAY + "axe. With it you can swing faster",
                ChatColor.GRAY + "and harder, dealing more damage.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Damage Bonus: " + ChatColor.GREEN + 2 + "hp"/*TODO: add player upgrades*/}));

        if (Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass() == Classes.BERSERKER) {
            inv.setItem(28, ItemsManager.createItem(Material.LAPIS_BLOCK, 1, ChatColor.BLUE + "Lvl: " + level, new String[]{
                    ChatColor.GRAY + "Slay bosses and RPG mobs to",
                    ChatColor.GRAY + "gain xp for your active class.", "",
                    ChatColor.GRAY + "Xp until next level: " + ChatColor.GREEN + ((level*300) - xp), /*TODO: check that this equation works*/}));
        } else {
            inv.setItem(28, ItemsManager.createItem(Material.EMERALD_BLOCK, 1, ChatColor.GREEN + "" + ChatColor.BOLD + "Join the class", new String[]{
                    ChatColor.GRAY + "Click me to join the berserker",
                    ChatColor.GRAY + "class! You will leave your",
                    ChatColor.GRAY + "current class."}));
        }

        inv.setItem(31, ItemsManager.createItem(Material.TOTEM_OF_UNDYING, 1, ChatColor.GOLD + "Indomitable Spirit", new String[]{
                ChatColor.GRAY + "As a berserker near death experiences",
                ChatColor.GRAY + "are little more than part of the job.",
                ChatColor.GRAY + "During these times however you still",
                ChatColor.GRAY + "gain significant bonuses and resistance.",
                ChatColor.GRAY + "Berserkers also gain some life steal",
                ChatColor.GRAY + "while in this state.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s", //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 180 + "s",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 2,
                ChatColor.GRAY + "- Lifesteal: " + ChatColor.GREEN + 10 + "%"}));
        inv.setItem(33, ItemsManager.createItem(Material.LEATHER_CHESTPLATE, 1, ChatColor.LIGHT_PURPLE + "Towering Form", new String[]{
                ChatColor.GRAY + "As a berserker smalls hits are",
                ChatColor.GRAY + "meaningless to your overall well",
                ChatColor.GRAY + "being and recovering from a fight",
                ChatColor.GRAY + "quickly was always a must.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Damage Reduction: " + ChatColor.GREEN + 2 + "hp",
                ChatColor.GRAY + "- Bonus Regeneration: " + ChatColor.GREEN + 20 + "%"/*TODO: add player upgrades*/}));
    }
}
