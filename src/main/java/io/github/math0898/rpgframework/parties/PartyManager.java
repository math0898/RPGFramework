package io.github.math0898.rpgframework.parties;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

/**
 * The party manager handles things relating to parties in general. This includes things like party chat.
 *
 * @author Sugaku
 */
public class PartyManager implements Listener {

    /**
     * The list of players who are currently using party chat.
     */
    private ArrayList<Player> partyChatPlayers = new ArrayList<>();

    /**
     * The list of currently active players. When the last player leaves a party it is removed from this list.
     */
    private ArrayList<Party> parties = new ArrayList<>();

    /**
     * Finds the party that a player is currently in.
     *
     * @param player The player to locate the party of.
     * @return The party this player is a member of. Null otherwise.
     */
    public Party findParty (Player player) {
        for (Party p : parties) if (p.hasMember(player)) return p;
        return null;
    }

    /**
     * Handles the even when a player tries to chat. If they have party chat enabled the message will be sent to their
     * party.
     *
     * @param event The chat event.
     */
    @EventHandler
    public void onChat (AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (partyChatPlayers.contains(p)) {
            event.setCancelled(true);
            // todo add in class coloring.
            findParty(p).sendAll(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "None" + ChatColor.DARK_GRAY + "] "
                    + ChatColor.GREEN + p.getName() + ChatColor.DARK_GRAY + " > " + ChatColor.LIGHT_PURPLE + event.getMessage());
        }
    }
}
