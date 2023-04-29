package sugaku.rpg.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.items.ItemsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commandirpg extends AbstractCommand implements CommandExecutor {

    /**
     * Tab completer which fills in options for this command.
     */
    public TabCompleter autocomplete = new Autocompleteirpg();

    public Commandirpg(String name) { super(name); }

    /**
     * Code to be executed on run.
     * @return Whether the command executed successfully or not
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) { send(sender, "Check your usage. Should be /irpg <item>"); return true; }
        else if (!(sender instanceof Player)) { send(sender, "Please only run this command as a player."); return true; }

        Player player = (Player) sender;

        switch(args[0].toLowerCase()) {
            case "darkhelm": give(player, ItemsManager.DarkHelm); break;
            case "kruskaxe": give(player, ItemsManager.KruskAxe); break;
            case "kruskboots": give(player, ItemsManager.KruskBoots); break;
            case "kruskhelmet": give(player, ItemsManager.KruskHelmet); break;
            case "kruskleggings": give(player, ItemsManager.KruskLeggings); break;
            case "krusklore": give(player, ItemsManager.KruskLore); break;
            case "kruskspawn": give(player, ItemsManager.KruskSpawn); break;
            case "eiryerasspawn": give(player, ItemsManager.EiryerasSpawn); break;
            case "feyrithspawn": give(player, ItemsManager.FeyrithSpawn); break;
            case "undeadchestplate": give(player, ItemsManager.UndeadChestplate); break;
            default: send(player, "Sorry but we could not find that item."); return true;
        }

        send(player, "Here is your " + args[0] + ".");
        return true;
    }

    /**
     * Helper method for command execution. Gives the given player the given item.
     * @param p the player who gets the item.
     * @param i the item the player gets.
     */
    private static void give(Player p, ItemStack i) { p.getInventory().addItem(i); }
}

class Autocompleteirpg implements TabCompleter {
    /**
     * Tab completion of the command described above.
     * @return List<String> of options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("irpg")) {
            if (args.length == 1) {
                ArrayList<String> list = new ArrayList<>();
                //Keep this sorted. It saves an O(n log n) sort
                String[] Options = {"DarkHelm", "EiryerasSpawn", "FeyrithSpawn", "KruskAxe", "KruskBoots", "KruskHelmet", "KruskLeggings","KruskLore", "KruskSpawn", "UndeadChestplate"};

                if (!args[0].equals("")) { for (String o: Options) if (o.toLowerCase().startsWith(args[0].toLowerCase())) list.add(o); }
                else list.addAll(Arrays.asList(Options));

                return list;
            }
        }
        return null;
    }
}