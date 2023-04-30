package sugaku.rpg.framework.menus.classes;

import io.github.math0898.rpgframework.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import sugaku.rpg.framework.classes.Classes;
import sugaku.rpg.framework.menus.Menu;
import sugaku.rpg.framework.players.PlayerManager;

import java.util.Objects;

import static sugaku.rpg.framework.menus.ClassesManager.*;
import static sugaku.rpg.framework.menus.ClassesManager.classMenu;

public class BerserkerMenu extends ClassSubmenu implements Menu {

    /**
     * Creates a new class submenu with the berserker name.
     */
    public BerserkerMenu () {
        super("berserker", Classes.BERSERKER);
    }

    @Override
    public void onClick(int clicked, Player player) {
        switch (clicked) {
            case 49 -> classMenu(player, "main");

//            case 12: classMenu(player, "Haste");
//            case 14: classMenu(player, "Rage");
//            case 16: classMenu(player, "Brute Strength");
            case 28 -> {
                Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).joinClass(Classes.BERSERKER);
                classMenu(player, "Berserker");
            }
//            case 31: classMenu(player, "Indomitable Spirit");
//            case 33: classMenu(player, "Towering Form");
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, goBack);

        int classPoints = getClassPoints(Classes.BERSERKER);

        inv.setItem(10, new ItemBuilder(Material.ROTTEN_FLESH, Math.max(1, classPoints), ChatColor.DARK_GREEN + "Class points: " + classPoints).setLore(new String[]{
                ChatColor.GRAY + "Spend class points to upgrade",
                ChatColor.GRAY + "class abilities and passives.",
                ChatColor.GRAY + "One point is given per level."}).build());
        inv.setItem(12, new ItemBuilder(Material.SUGAR, 1, ChatColor.DARK_AQUA + "Haste").setLore(new String[]{
                ChatColor.GRAY + "Will your legs to run faster to",
                ChatColor.GRAY + "catch up to fleeing foes.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 2, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s",
                ChatColor.GRAY + "- Cooldown: 30s"}).build());
        inv.setItem(14, new ItemBuilder(Material.BLAZE_POWDER, 1, ChatColor.DARK_AQUA + "Rage").setLore(new String[]{
                ChatColor.GRAY + "Temporarily rage your attacks to",
                ChatColor.GRAY + "deal significantly more damage.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 2, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s",
                ChatColor.GRAY + "- Cooldown: 60s"}).build());
        inv.setItem(16, new ItemBuilder(Material.IRON_AXE, 1, ChatColor.LIGHT_PURPLE + "Brute Strength").setLore(new String[]{
                ChatColor.GRAY + "Mowing down enemies on the battle",
                ChatColor.GRAY + "field has trained you well with the",
                ChatColor.GRAY + "axe. With it you can swing faster",
                ChatColor.GRAY + "and harder, dealing more damage.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Damage Bonus: " + ChatColor.GREEN + 2 + "hp"/*TODO: add player upgrades*/}).build());

        if (Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass() == Classes.BERSERKER) inv.setItem(28, classLvl);
        else inv.setItem(28, joinClass);


        inv.setItem(31, new ItemBuilder(Material.TOTEM_OF_UNDYING, 1, ChatColor.GOLD + "Indomitable Spirit").setLore(new String[]{
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
                ChatColor.GRAY + "- Lifesteal: " + ChatColor.GREEN + 10 + "%"}).build());
        inv.setItem(33, new ItemBuilder(Material.LEATHER_CHESTPLATE, 1, ChatColor.LIGHT_PURPLE + "Towering Form").setLore(new String[]{
                ChatColor.GRAY + "As a berserker smalls hits are",
                ChatColor.GRAY + "meaningless to your overall well",
                ChatColor.GRAY + "being and recovering from a fight",
                ChatColor.GRAY + "quickly was always a must.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Damage Reduction: " + ChatColor.GREEN + 2 + "hp",
                ChatColor.GRAY + "- Bonus Regeneration: " + ChatColor.GREEN + 20 + "%"/*TODO: add player upgrades*/}).build());
    }
}
