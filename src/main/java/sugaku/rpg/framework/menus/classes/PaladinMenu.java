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
import static sugaku.rpg.main.brackets;

public class PaladinMenu extends ClassSubmenu implements Menu {

    /**
     * Creates a new class submenu with the given paladin name and enum value.
     */
    public PaladinMenu () {
        super("paladin", Classes.PALADIN);
    }

    @Override
    public void onClick(int clicked, Player player) {
        switch (clicked) {
            case 49 -> classMenu(player, "main");
//            case 12: classMenu(player, "Mend");
//            case 14: classMenu(player, "Purify");
//            case 16: classMenu(player, "Holy Form");
            case 28 -> {
                Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).joinClass(Classes.PALADIN);
                classMenu(player, "Paladin");
            }
//            case 32: classMenu(player, "Protection");
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, goBack);

        int classPoints = getClassPoints(Classes.PALADIN);

        inv.setItem(10, new ItemBuilder(Material.GOLDEN_SHOVEL, Math.max(1, classPoints), ChatColor.DARK_GREEN + "Class points: " + classPoints).setLore(new String[]{
                ChatColor.GRAY + "Spend class points to upgrade",
                ChatColor.GRAY + "class abilities and passives.",
                ChatColor.GRAY + "One point is given per level."}).build());
        inv.setItem(12, new ItemBuilder(Material.GLISTERING_MELON_SLICE, 1, ChatColor.DARK_AQUA + "Mend").setLore(new String[]{
                ChatColor.GRAY + "Mend your own and your ally's",
                ChatColor.GRAY + "wounds with your mastery of",
                ChatColor.GRAY + "healing magic.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 3, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 15 + "s",
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 30 + "s"}).build());
        inv.setItem(14, new ItemBuilder(Material.GLOWSTONE_DUST, 1, ChatColor.DARK_AQUA + "Purify").setLore(new String[]{
                ChatColor.GRAY + "Purify your party from all",
                ChatColor.GRAY + "ailments, heal a significant",
                ChatColor.GRAY + "amount, and become damage",
                ChatColor.GRAY + "immune for a short time.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 5, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 5 + "s",
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 60 + "s"}).build());
        inv.setItem(16, new ItemBuilder(Material.GOLDEN_CHESTPLATE, 1, ChatColor.LIGHT_PURPLE + "Holy Form").setLore(new String[]{
                ChatColor.GRAY + "Your mastery of healing magic has",
                ChatColor.GRAY + "increased your overall vitality",
                ChatColor.GRAY + "and therefore your maximum health.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Health Bonus: " + ChatColor.GREEN + 10 + "hp"/*TODO: add player upgrades*/,
                brackets(ChatColor.RED + "Disabled") + " -" + ChatColor.RED + " Odd functionality"}).build());

        if (Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass() == Classes.PALADIN) inv.setItem(28, classLvl);
        else inv.setItem(28, joinClass);

        inv.setItem(32, new ItemBuilder(Material.TOTEM_OF_UNDYING, 1, ChatColor.GOLD + "Protection of the Healer").setLore(new String[]{
                ChatColor.GRAY + "To prevent death latent healing magic",
                ChatColor.GRAY + "bursts forth from your body healing",
                ChatColor.GRAY + "your entire party, increasing their",
                ChatColor.GRAY + "maximum life, and regeneration for",
                ChatColor.GRAY + "a short time.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s", //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 300 + "s",
                ChatColor.GRAY + "- Instant Heal: " + ChatColor.GREEN + 2,
                ChatColor.GRAY + "- Buffs Strength: " + ChatColor.GREEN + 2}).build());
    }
}
