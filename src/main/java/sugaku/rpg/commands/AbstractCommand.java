package sugaku.rpg.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.PluginsCommand;

public abstract class AbstractCommand extends PluginsCommand {

    /**
     * The prefix sent with every message.
     */
    private static final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    public AbstractCommand(String name) { super(name); }


    public static void send(CommandSender user, String message) { send(user, message, true); }

    /**
     * Sends a message to the given recipient.
     */
    public static void send(CommandSender user, String message, boolean sendPrefix) {
        if (sendPrefix) user.sendMessage(prefix + message);
        else user.sendMessage(ChatColor.GRAY + message);
    }
}
