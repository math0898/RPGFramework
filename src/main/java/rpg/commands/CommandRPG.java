package sugaku.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import sugaku.rpg.framework.menus.ClassesManager;

import java.util.List;

public class CommandRPG extends AbstractCommand {

    public CommandRPG(String name) { super(name); }

    public static boolean ClassesSubcommand (CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) send(sender, "Please only send this command as a player.");
        else ClassesManager.classMenu((Player) sender);

        return true;
    }
}

class CommandRPGAutocomplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
