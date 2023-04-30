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
import static sugaku.rpg.framework.menus.ClassesManager.fill;
import static sugaku.rpg.framework.menus.ClassesManager.goBack;
import static sugaku.rpg.framework.menus.ClassesManager.classMenu;

public class AssassinMenu extends ClassSubmenu implements Menu {

    /**
     * Creates a new class submenu with the given assassin name and enum value.
     */
    public AssassinMenu () {
        super("assassin", Classes.ASSASSIN);
    }

    @Override
    public void onClick(int clicked, Player player) {
        switch (clicked) {
            case 49 -> classMenu(player, "main");
//            case 12: classMenu(player, "Invisibility"); break;
//            case 14: classMenu(player, "Poisoned Blade"); break;
//            case 16: classMenu(player, "Swiftness"); break;
            case 28 -> {
                Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).joinClass(Classes.ASSASSIN);
                classMenu(player, "Assassin");
            }
//            case 31: classMenu(player, "Heroic Dodge"); break;
//            case 33: classMenu(player, "Finesse"); break;
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, goBack);

        int classPoints = getClassPoints(Classes.ASSASSIN);

        inv.setItem(10, new ItemBuilder(Material.GHAST_TEAR, Math.max(1, classPoints), ChatColor.DARK_GREEN + "Class points: " + classPoints).setLore(new String[]{
                ChatColor.GRAY + "Spend class points to upgrade",
                ChatColor.GRAY + "class abilities and passives.",
                ChatColor.GRAY + "One point is given per level."}).build());
        inv.setItem(12, new ItemBuilder(Material.GOLDEN_CARROT, 1, ChatColor.DARK_AQUA + "Invisibility").setLore(new String[]{
                ChatColor.GRAY + "Gain invisibility and increase",
                ChatColor.GRAY + "your dodge chance to 50%",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10, //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 30 + "s" /*^^^*/}).build());
        inv.setItem(14, new ItemBuilder(Material.POISONOUS_POTATO, 1, ChatColor.DARK_AQUA + "Poisoned Blade").setLore(new String[]{
                ChatColor.GRAY + "Poison your blade to weaken",
                ChatColor.GRAY + "your foes and deal insane",
                ChatColor.GRAY + "damage.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10, //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 60 + "s",
                ChatColor.GRAY + "- Potency: " + ChatColor.GREEN + 1}).build());
        inv.setItem(16, new ItemBuilder(Material.FEATHER, 1, ChatColor.LIGHT_PURPLE + "Swiftness").setLore(new String[]{
                ChatColor.GRAY + "Evade your opponent's attacks",
                ChatColor.GRAY + "and clear the terrain with",
                ChatColor.GRAY + "unmatched skill.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Evasion: " + ChatColor.GREEN + 10 + "%", //TODO: add player upgrades
                ChatColor.GRAY + "- Swiftness: " + ChatColor.GREEN + 1}).build());

        if (Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass() == Classes.ASSASSIN) inv.setItem(28, classLvl);
        else inv.setItem(28, joinClass);

        inv.setItem(31, new ItemBuilder(Material.TOTEM_OF_UNDYING, 1, ChatColor.GOLD + "Heroic Dodge").setLore(new String[]{
                ChatColor.GRAY + "Pulling off an incredible feat",
                ChatColor.GRAY + "to evade death, you are given",
                ChatColor.GRAY + "renewed stamina and insight",
                ChatColor.GRAY + "into your opponent's attack's",
                ChatColor.GRAY + "for a short time.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s", //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 300 + "s",
                ChatColor.GRAY + "- Swiftness: " + ChatColor.GREEN + 2}).build());
        inv.setItem(33, new ItemBuilder(Material.IRON_SWORD, 1, ChatColor.LIGHT_PURPLE + "Assassin's Finesse").setLore(new String[]{
                ChatColor.GRAY + "Through your many jobs you've",
                ChatColor.GRAY + "learned in the ins and outs of",
                ChatColor.GRAY + "the human body and know how to",
                ChatColor.GRAY + "inflict the most damage.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Damage Bonus: " + ChatColor.GREEN + 2 + "hp" /*TODO: add player upgrades*/}).build());
    }
}
