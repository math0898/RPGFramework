package sugaku.rpg.commands;

import org.bukkit.command.*;
import java.util.List;

public class CommandClasses extends AbstractCommand implements CommandExecutor {

    /**
     * Tab completer which fills in options for this command.
     */
    public TabCompleter autocomplete = new AutocompleteClasses();

    public CommandClasses(String name) { super(name); }

    /**
     * Code to be executed on run.
     * @return Whether the command executed successfully or not
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { return CommandRPG.ClassesSubcommand(sender, command, label, args); }
}

class AutocompleteClasses implements TabCompleter {
    /**
     * Tab completion of the command described above.
     * @return List<String> of options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) { return null; }
}
