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
import static sugaku.rpg.main.brackets;

public class PaladinMenu extends ClassSubmenu implements Menu {

    @Override
    public void onClick(int clicked, Player player) {
        switch(clicked) {
            case 49: classMenu(player, "main"); break;
//            case 12: classMenu(player, "Mend");
//            case 14: classMenu(player, "Purify");
//            case 16: classMenu(player, "Holy Form");
            case 28: Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).joinClass(Classes.PALADIN); classMenu(player, "Paladin"); break;
//            case 32: classMenu(player, "Protection");
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);
        inv.setItem(49, goBack);

        int classPoints = getClassPoints(Classes.PALADIN);
        int level = getClassLvl(Classes.PALADIN);
        int xp = getClassXp(Classes.PALADIN);

        inv.setItem(10, ItemsManager.createItem(Material.GOLDEN_SHOVEL, Math.max(1, classPoints), ChatColor.DARK_GREEN + "Class points: " + classPoints, new String[]{
                ChatColor.GRAY + "Spend class points to upgrade",
                ChatColor.GRAY + "class abilities and passives.",
                ChatColor.GRAY + "One point is given per level."}));
        inv.setItem(12, ItemsManager.createItem(Material.GLISTERING_MELON_SLICE, 1, ChatColor.DARK_AQUA + "Mend", new String[]{
                ChatColor.GRAY + "Mend your own and your ally's",
                ChatColor.GRAY + "wounds with your mastery of",
                ChatColor.GRAY + "healing magic.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 3, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 15 + "s",
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 30 + "s"}));
        inv.setItem(14, ItemsManager.createItem(Material.GLOWSTONE_DUST, 1, ChatColor.DARK_AQUA + "Purify", new String[]{
                ChatColor.GRAY + "Purify your party from all",
                ChatColor.GRAY + "ailments, heal a significant",
                ChatColor.GRAY + "amount, and become damage",
                ChatColor.GRAY + "immune for a short time.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Strength: " + ChatColor.GREEN + 5, //TODO: add player upgrades
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 5 + "s",
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 60 + "s"}));
        inv.setItem(16, ItemsManager.createItem(Material.GOLDEN_CHESTPLATE, 1, ChatColor.LIGHT_PURPLE + "Holy Form", new String[]{
                ChatColor.GRAY + "Your mastery of healing magic has",
                ChatColor.GRAY + "increased your overall vitality",
                ChatColor.GRAY + "and therefore your maximum health.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Health Bonus: " + ChatColor.GREEN + 10 + "hp"/*TODO: add player upgrades*/,
                brackets(ChatColor.RED + "Disabled") + " -" + ChatColor.RED + " Odd functionality"}));

        if (Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass() == Classes.PALADIN) {
            inv.setItem(28, ItemsManager.createItem(Material.LAPIS_BLOCK, 1, ChatColor.BLUE + "Lvl: " + level, new String[]{
                    ChatColor.GRAY + "Slay bosses and RPG mobs to",
                    ChatColor.GRAY + "gain xp for your active class.", "",
                    ChatColor.GRAY + "Xp until next level: " + ChatColor.GREEN + ((level*300) - xp), /*TODO: check that this equation works*/}));
        } else {
            inv.setItem(28, ItemsManager.createItem(Material.EMERALD_BLOCK, 1, ChatColor.GREEN + "" + ChatColor.BOLD + "Join the class", new String[]{
                    ChatColor.GRAY + "Click me to join the paladin",
                    ChatColor.GRAY + "class! You will leave your",
                    ChatColor.GRAY + "current class."}));
        }

        inv.setItem(32, ItemsManager.createItem(Material.TOTEM_OF_UNDYING, 1, ChatColor.GOLD + "Protection of the Healer", new String[]{
                ChatColor.GRAY + "To prevent death latent healing magic",
                ChatColor.GRAY + "bursts forth from your body healing",
                ChatColor.GRAY + "your entire party, increasing their",
                ChatColor.GRAY + "maximum life, and regeneration for",
                ChatColor.GRAY + "a short time.",
                ChatColor.GRAY + "Current Stats: ",
                ChatColor.GRAY + "- Duration: " + ChatColor.GREEN + 10 + "s", //TODO: add player upgrades
                ChatColor.GRAY + "- Cooldown: " + ChatColor.GREEN + 300 + "s",
                ChatColor.GRAY + "- Instant Heal: " + ChatColor.GREEN + 2,
                ChatColor.GRAY + "- Buffs Strength: " + ChatColor.GREEN + 2}));
    }
}
