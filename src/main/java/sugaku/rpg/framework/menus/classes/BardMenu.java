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

public class BardMenu extends ClassSubmenu implements Menu {

    @Override
    public void onClick(int clicked, Player player) {
        switch(clicked) {
            case 49: classMenu(player, "main"); break;
//            case 12: classMenu(player, "Swiftness");
//            case 14: classMenu(player, "Regeneration");
//            case 16: classMenu(player, "Strength");
            case 28: Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).joinClass(Classes.BARD); classMenu(player, "Bard"); break;
//            case 31: classMenu(player, "A Life of Music");
//            case 33: classMenu(player, "Hym");
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, goBack);

        int classPoints = getClassPoints(Classes.BARD);
        int level = getClassLvl(Classes.BARD);
        int xp = getClassXp(Classes.BARD);

        inv.setItem(10, new ItemBuilder(Material.NOTE_BLOCK, Math.max(1, classPoints), ChatColor.DARK_GREEN + "Class points: " + classPoints).setLore(new String[]{
                ChatColor.GRAY + "Spend class points to upgrade",
                ChatColor.GRAY + "class abilities and passives.",
                ChatColor.GRAY + "One point is given per level."}).build());
        inv.setItem(12, new ItemBuilder(Material.SUGAR, 1, ChatColor.DARK_AQUA + "Swiftness").setLore(new String[]{
                ChatColor.GRAY + "Gain swiftness to outrun your",
                ChatColor.GRAY + "foes.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 1, //TODO: add player upgrades
                ChatColor.GRAY + "- Buff Duration: " + ChatColor.GREEN + 45 + "s" /*^^^*/}).build());
        inv.setItem(14, new ItemBuilder(Material.GLISTERING_MELON_SLICE, 1, ChatColor.DARK_AQUA + "Regeneration").setLore(new String[]{
                ChatColor.GRAY + "Heal your wounds with a relaxing",
                ChatColor.GRAY + "melody.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 1, //TODO: add player upgrades
                ChatColor.GRAY + "- Buff Duration: " + ChatColor.GREEN + 45 + "s"}).build());
        inv.setItem(16, new ItemBuilder(Material.BLAZE_POWDER, 1, ChatColor.DARK_AQUA + "Strength").setLore(new String[]{
                ChatColor.GRAY + "Strengthen your bones and muscles",
                ChatColor.GRAY + "with a grand epic to strike down",
                ChatColor.GRAY + "your enemies.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 1, //TODO: add player upgrades
                ChatColor.GRAY + "- Buff Duration: " + ChatColor.GREEN + 45 + "s"}).build());

        if (Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass() == Classes.BARD) {
            inv.setItem(28, new ItemBuilder(Material.LAPIS_BLOCK, 1, ChatColor.BLUE + "Lvl: " + level).setLore(new String[]{
                    ChatColor.GRAY + "Slay bosses and RPG mobs to",
                    ChatColor.GRAY + "gain xp for your active class.", "",
                    ChatColor.GRAY + "Xp until next level: " + ChatColor.GREEN + ((level*300) - xp), /*TODO: check that this equation works*/}).build());
        } else {
            inv.setItem(28, new ItemBuilder(Material.EMERALD_BLOCK, 1, ChatColor.GREEN.toString() + ChatColor.BOLD + "Join the class").setLore(new String[]{
                    ChatColor.GRAY + "Click me to join the bard",
                    ChatColor.GRAY + "class! You will leave your",
                    ChatColor.GRAY + "current class."}).build());
        }

        inv.setItem(31, new ItemBuilder(Material.TOTEM_OF_UNDYING, 1, ChatColor.GOLD + "A Life of Music").setLore(new String[]{
                ChatColor.GRAY + "Through your travels you've practiced",
                ChatColor.GRAY + "and practiced the musical instrument.",
                ChatColor.GRAY + "After countless hours you've mastered",
                ChatColor.GRAY + "it, and then went beyond. Instead of",
                ChatColor.GRAY + "dying the music from your life bursts",
                ChatColor.GRAY + "forth greatly improving your strength.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s", //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 300 + "s",
                ChatColor.GRAY + "- Instant Healing: " + ChatColor.GREEN + 10,
                ChatColor.GRAY + "- Buffs Bonus: " + ChatColor.GREEN + 1}).build());
        inv.setItem(33, new ItemBuilder(Material.IRON_SWORD, 1, ChatColor.LIGHT_PURPLE + "Hym").setLore(new String[]{
                ChatColor.GRAY + "It takes time to play each hym",
                ChatColor.GRAY + "which gives you and your allies",
                ChatColor.GRAY + "a buff. Through practice you can",
                ChatColor.GRAY + "increase the tempo of each hym.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Buff Cooldown: " + ChatColor.GREEN + 30 + "s" /*TODO: add player upgrades*/}).build());
    }
}
