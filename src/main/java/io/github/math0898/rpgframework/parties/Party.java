package io.github.math0898.rpgframework.parties;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Describes a singular party that may, unless the leader is an admin, contain up to 6 players. This class has some
 * useful features such as sending messages to all players along with potion effects. Players in the same party cannot
 * hit one another.
 *
 * @author Sugaku
 */
public class Party {

    /**
     * An ArrayList of players that are currently in this party.
     */
    private final ArrayList<Player> players = new ArrayList<>();

    /**
     * This is a reference to the leader of the party. They should also be contained within {@link #players}.
     */
    private Player leader;

    /**
     * Creates a new party with the given player as the leader.
     *
     * @param l The leader for this party.
     */
    public Party (Player l) {
        leader = l;
        players.add(l);
    }

    /**
     * Accessor method for all the players currently in this party.
     *
     * @return The ArrayList of players currently in the party. Do not modify.
     */
    public ArrayList<Player> getPlayers () {
        return players;
    }

    /**
     * Returns a collection of the RPG version of the player objects stored in this party.
     *
     * @return The Collection of RpgPlayers.
     */
    public Collection<RpgPlayer> getRpgPlayers () {
        List<RpgPlayer> playerList = new ArrayList<>();
        for (Player p : players) playerList.add(PlayerManager.getPlayer(p.getUniqueId()));
        return playerList;
    }

    /**
     * Accessor method for the party leader.
     *
     * @return The leader of the party.
     */
    public Player getLeader () {
        return leader;
    }

    /**
     * Adds a player to the party. This makes a check to see if the party has hit max capacity or if an admin leads the
     * party. The return boolean represents the success of this method.
     *
     * @param p The player to add into the party.
     * @return True if the player was added to this party. False otherwise.
     */
    public boolean addPlayer (Player p) {
        if (players.size() < 6) players.add(p);
        else if (leader.hasPermission("rpg.parties.maxbypass")) players.add(p);
        else return false;
        return true;
    }

    /**
     * Removes a player from the party.
     *
     * @param p The player to remove from the party.
     */
    public void removePlayer (Player p) {
        players.remove(p);
        if (p.equals(getLeader()) && getPlayers().size() > 0) promote(getPlayers().get(0));
        if (getPlayers().size() == 0) PartyManager.removeParty(this);
    }

    /**
     * Checks if this party has the given player.
     *
     * @param player The player to look for.
     * @return True if and only if this party contains the given player.
     */
    public boolean hasMember (Player player) {
        return players.contains(player);
    }

    /**
     * Promotes the given player to party leader. Will take no action if the player is not currently part of the party.
     *
     * @param player The player to attempt to make party leader.
     */
    public void promote (Player player) {
        leader = player;
    }

    /**
     * Sends the given string to all players in the party.
     *
     * @param m The message to send to all players.
     */
    public void sendAll (String m) {
        players.forEach((p) -> p.sendMessage(m));
    }

    /**
     * Applies a potion effect to all players in the party.
     *
     * @param effect The potion effect to apply to all players.
     */
    public void applyEffect (PotionEffect effect) {
        players.forEach((p) -> p.addPotionEffect(effect));
    }

    /**
     * Used to determine whether two parties are equal or not.
     *
     * @param o The object to compare with *this*.
     * @return True if and only if the pass object and *this* are equal.
     */
    @Override
    public boolean equals (Object o) {
        if (o == null) return false;
        else if (o instanceof Party rhs) {
            return this.leader.equals(rhs.leader);
        } else return false;
    }
}
