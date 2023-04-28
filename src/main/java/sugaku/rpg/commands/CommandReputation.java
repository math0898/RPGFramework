package sugaku.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class CommandReputation extends AbstractCommand implements CommandExecutor {

    /**
     * Tab completer which fills in options for this command.
     */
    public TabCompleter autocomplete = new AutocompleteReputation();

    public CommandReputation(String name) {super(name);}

    /**
     * Code to be execute on run.
     * @return Whether or not the command was successful or not.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 2 || args.length > 4) {
            if (sender.hasPermission("rpg.admin")) send(sender, "Check your usage. Should be /reputation <player/leaderboard> <add/remove/set> <amount>");
            else send(sender, "Check your usage. Should be /reputation <player/leaderboard>");

        } else if (sender.hasPermission("rpg.admin")) send(sender, "Command in development, admin.");
        else send(sender, "Command in development, player.");
        //TODO: Implement command.
        return true;
    }
}

class AutocompleteReputation implements TabCompleter{
    /**
     * Tab completion of the command described above.
     * @return List<String> of options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reputation")) {
            if (args.length == 2 && sender.hasPermission("rpg.admin")) {
                ArrayList<String> list = new ArrayList<>();
                //Keep this sorted. It saves an O(n log n) sort
                String[] Options = {"add", "remove", "set"};

                if (!args[1].equals("")) {for (String o: Options) if (o.toLowerCase().startsWith(args[1].toLowerCase())) list.add(o); }
                else list.addAll(Arrays.asList(Options));

                return list;
            } else if (args.length == 3 && sender.hasPermission("rpg.admin")) return new ArrayList<String>(Collections.singleton("<amnt>"));
            else if (args.length != 1) return new ArrayList<String>();
        }
        return null; //TODO: Add player list/leaderboard to autocomplete.
    }
}
