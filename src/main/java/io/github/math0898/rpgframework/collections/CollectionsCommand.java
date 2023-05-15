package io.github.math0898.rpgframework.collections;

import io.github.math0898.rpgframework.UserData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sugaku.rpg.framework.players.PlayerManager;

import java.util.List;

/**
 * The CollectionsCommand allows admins to view and modify player's collection values, as well as players to see their
 * own collections and ranking.
 *
 * @author Sugaku
 */
public class CollectionsCommand implements TabCompleter, CommandExecutor {

    @Override
    public boolean onCommand (@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!commandSender.hasPermission("rpg.collections")) return true;
        if (commandSender instanceof Player player) {
            UserData data = PlayerManager.getUserData(player.getUniqueId());
            if (data == null) return true;
            for (String s : data.registeredCollections()) player.sendMessage(ChatColor.GRAY + "> " + s + ": " + data.getCollection(s).get());
        } else commandSender.sendMessage("Command only runnable as player.");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete (@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
