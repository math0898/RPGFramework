package io.github.math0898.rpgframework.items;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.github.math0898.rpgframework.RPGFramework.itemManager;

/**
 * The give command is used to give items registered in the ItemManager. Requires admin permissions.
 */
public class GiveCommand implements CommandExecutor {

    /**
     * The TabCompleter for the give command.
     */
    public static TabCompleter autocomplete = new TabCompleter() {

        /**
         * Requests a list of possible completions for a command argument.
         *
         * @param sender Source of the command. For players tab-completing a command inside a command block, this will
         *               be the player, not the command block.
         * @param command Command which was executed.
         * @param alias The alias used.
         * @param args The arguments passed to the command, including final partial argument to be completed and command
         *             label.
         * @return A List of possible completions for the final argument, or null to default to the command executor
         */
        @Override
        public List<String> onTabComplete (@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
            if (command.getName().equalsIgnoreCase("rpg-give")) {
                ArrayList<String> list = new ArrayList<>();
                if (args.length == 1) {
                    List<String> options = itemManager.getItemNames();
                    options.sort(String::compareTo);
                    if (!args[0].equals("")) { for (String o: options) if (o.toLowerCase().startsWith(args[0].toLowerCase())) list.add(o); }
                    else list.addAll(options);
                }
                return list;
            }
            return null;
        }
    };

    /**
     * Executes the given command, returning its success.
     * If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the player.
     *
     * @param sender Source of the command.
     * @param command Command which was executed.
     * @param label Alias of the command which was used.
     * @param args Passed command arguments.
     * @return True if a valid command, otherwise false.
     */
    public boolean onCommand (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
//        if (sender instanceof Player p && sender.hasPermission("rpg.admin")) p.getInventory().addItem(itemManager.getItem(args[0]));
        return true;
    }
}
