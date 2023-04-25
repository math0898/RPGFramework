package sugaku.rpg.framework.menus.classes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.framework.menus.Menu;
import sugaku.rpg.framework.players.PlayerManager;

import java.util.Objects;

import static sugaku.rpg.main.brackets;
import static sugaku.rpg.framework.menus.ClassesManager.title;
import static sugaku.rpg.framework.menus.ClassesManager.fill;
import static sugaku.rpg.framework.menus.ClassesManager.classMenu;

public class MainMenu implements Menu {

    public MainMenu(){}

    private static final ItemStack classIndicator = ItemsManager.createItem(Material.ENDER_EYE, 1, title, new String[]{
            ChatColor.GRAY + "Welcome to the classes menu!",
            ChatColor.GRAY + "Left clicking a class or archetype",
            ChatColor.GRAY + "will show details such as your",
            ChatColor.GRAY + "current lvl and class abilities."});

    private static final ItemStack assassinIndicator = ItemsManager.createItem(Material.GHAST_TEAR, 1, brackets(ChatColor.GOLD + "Assassin"), new String[]{
            ChatColor.GRAY + "Strike your foes with a " + ChatColor.DARK_AQUA + "poisoned blade",
            ChatColor.GRAY + "before fading into the shadows with your",
            ChatColor.GRAY + "master of " + ChatColor.DARK_AQUA + "stealth" + ChatColor.GRAY + ". Even cowering behind",
            ChatColor.GRAY + "a shield cannot save your enemies."});

    private static final ItemStack bardIndicator = ItemsManager.createItem(Material.NOTE_BLOCK, 1, brackets(ChatColor.GOLD + "Bard"), new String[]{
            ChatColor.GRAY + "The flexibility of bards as fighters",
            ChatColor.GRAY + "cannot be understated as they can use",
            ChatColor.GRAY + "any equipment effectively whilst buffing",
            ChatColor.GRAY + "themselves and their allies with " + ChatColor.DARK_AQUA + "hym" + ChatColor.GRAY + "."});

    private static final ItemStack berserkerIndicator = ItemsManager.createItem(Material.ROTTEN_FLESH, 1, brackets(ChatColor.GOLD + "Berserker"), new String[]{
            ChatColor.GRAY + "Berserkers are fearsome foes that deal",
            ChatColor.GRAY + "staggering damage with axes. Lost in the",
            ChatColor.GRAY + "battle berserkers also take less damage",
            ChatColor.GRAY + "than other classes would."});

    private static final ItemStack paladinIndicator = ItemsManager.createItem(Material.GOLDEN_SHOVEL, 1, brackets(ChatColor.GOLD + "Paladin"), new String[]{
            ChatColor.GRAY + "Paladins are pure warriors who can " + ChatColor.DARK_AQUA + "purify",
            ChatColor.GRAY + "themselves and their allies. Healing magic",
            ChatColor.GRAY + "also comes naturally to paladins resulting",
            ChatColor.GRAY + "in higher max health and " + ChatColor.DARK_AQUA + "regeneration" + "."});

    private static final ItemStack pyromancerIndicator = ItemsManager.createItem(Material.BLAZE_POWDER, 1, brackets(ChatColor.GOLD + "Pyromancer"), new String[]{
            ChatColor.GRAY + "Burn your enemies with the power of the",
            ChatColor.GRAY + "phoenix and call upon the " +ChatColor.DARK_AQUA + "renewal" + ChatColor.GRAY + " powers",
            ChatColor.GRAY + "to heal yourself and resurrect from the grave."});

    private static final ItemStack marksmanIndicator = ItemsManager.createItem(Material.BOW, 1, brackets(ChatColor.GOLD + "Marksman"), new String[]{ChatColor.DARK_RED + "Coming soon."});

    private static final ItemStack fighterIndicator = ItemsManager.createItem(Material.IRON_SWORD, 1, brackets(ChatColor.BLUE + "Fighter"), new String[]{
            ChatColor.GRAY + "Through your years of training in combat",
            ChatColor.GRAY + "you've suffered many injuries and can apply",
            ChatColor.GRAY + "basic " + ChatColor.DARK_AQUA + "first aid " + brackets(ChatColor.GOLD + "" + ChatColor.BOLD + "Dev") + ChatColor.GRAY + " using paper."});

    private static final ItemStack casterIndicator = ItemsManager.createItem(Material.ENCHANTING_TABLE, 1, brackets(ChatColor.BLUE + "Caster"), new String[]{
            ChatColor.GRAY + "Casters have powerful abilities which depend",
            ChatColor.GRAY + "on  the class selected. Through your years",
            ChatColor.GRAY + "of study in getting these skills your muscles",
            ChatColor.GRAY + "have grown weaker and you take additional",
            ChatColor.GRAY + "damage from " + ChatColor.LIGHT_PURPLE + "players" + ChatColor.GRAY + " who are " + ChatColor.BLUE + "fighters" + ChatColor.GRAY + "."
    });

    private static final ItemStack adventurerIndicator = ItemsManager.createItem(Material.LEATHER_BOOTS, 1, brackets(ChatColor.DARK_GREEN + "Adventurer"), new String[]{
            ChatColor.GRAY + "By being an adventurer you",
            ChatColor.GRAY + "gain some general bonuses",
            ChatColor.GRAY + "to various drop rates."});

    private static final ItemStack noneIndicator = ItemsManager.createItem(Material.BARRIER, 1, brackets(ChatColor.RED + "" + ChatColor.BOLD + "None"));

    @Override
    public void onClick(int clicked, Player player) {
        switch(clicked) {
            case 11: classMenu(player, Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).getCombatClassString()); break;
//            case 13: classMenu(player, Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).getArchetype()); break;
//            case 15: classMenu(player, "Adventurer"); break;
//            case 28: classMenu(player, "Fighter"); break;
//            case 37: classMenu(player, "Caster"); break;
            case 30: classMenu(player, "Assassin"); break;
            case 31: classMenu(player, "Bard"); break;
            case 32: classMenu(player, "Berserker"); break;
            case 33: classMenu(player, "Paladin"); break;
            case 34: classMenu(player, "Pyromancer"); break;
//            case 41: classMenu(player, "Marksman"); break;
        }
    }

    @Override
    public void build(Inventory inv, Player p) {

        for (int i = 0; i < 54; i++) inv.setItem(i, fill);

        inv.setItem(45, classIndicator);
        inv.setItem(28, fighterIndicator);
        inv.setItem(37, casterIndicator);
        inv.setItem(30, assassinIndicator);
        inv.setItem(31, bardIndicator);
        inv.setItem(32, berserkerIndicator);
        inv.setItem(33, paladinIndicator);
        inv.setItem(34, pyromancerIndicator);
        inv.setItem(41, marksmanIndicator);

        inv.setItem(11, getClassIndicator(p));
        inv.setItem(13, getArchIndicator(p));
        inv.setItem(15, adventurerIndicator);
    }

    private static ItemStack getClassIndicator(Player p) {
        switch(Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getCombatClass()) {
            case ASSASSIN: return assassinIndicator;
            case BARD: return bardIndicator;
            case BERSERKER: return berserkerIndicator;
            case PALADIN: return paladinIndicator;
            case PYROMANCER: return pyromancerIndicator;
            case MARKSMEN: return marksmanIndicator;
        }
        return noneIndicator;
    }

    private static ItemStack getArchIndicator(Player p) {
        switch(Objects.requireNonNull(PlayerManager.getPlayer(p.getUniqueId())).getArchetype()) {
            case "Fighter": return fighterIndicator;
            case "Caster": return casterIndicator;
        }
        return noneIndicator;
    }
}
