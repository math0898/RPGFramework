package io.github.math0898.rpgframework;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import sugaku.rpg.factions.Faction;
import sugaku.rpg.factions.FactionData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserData {

    /**
     * The UUID of the player who's data is loaded.
     */
    private final UUID uuid;

    /**
     * The username of the player whom this data belongs to.
     */
    private final String username;

    /**
     * A map of the collections that this player has. Some may be null.
     */
    private final Map<String, ItemCollection> collections = new HashMap<>();

    /**
     * Creates a new empty UserData which data can be assigned to.
     *
     * @param uuid The uuid of the player this data is for.
     */
    public UserData (UUID uuid) {
        this.uuid = uuid;
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) username = p.getName();
        else username = "Unknown";
    }

    /**
     * Adds a collection to this UserData if not present.
     *
     * @param name The name of the collection to add.
     * @param collection The ItemCollection object to assign.
     */
    public void addCollection (String name, ItemCollection collection) {
        collections.put(name, collection);
    }

    /**
     * Accessor method for collections if they are present in this UserData.
     *
     * @param name The name of the collection to grab.
     * @return The ItemCollection that may or may not have been found.
     */
    public ItemCollection getCollection (String name) {
        return collections.get(name);
    }

    /**
     * Accessor method for the list of collections that are present in this UserData.
     *
     * @return The String set of collections that exist.
     */
    public Set<String> registeredCollections () {
        return collections.keySet();
    }

    /**
     * Assigns the important values of this UserData to the given ConfigurationSection.
     *
     * @param sec The configuration section to assign the important values to.
     */
    public void toConfigurationSection (ConfigurationSection sec) {
        sec.set("username", username);
        if (!collections.isEmpty()) {
            ConfigurationSection col = sec.createSection("collections");
            for (String c : collections.keySet())
                collections.get(c).toConfigurationSection(col.createSection(c));
        }
    }

    /**
     * Accesses the data on the Abyss faction.
     * @return The data on the abyss faction.
     */
    @Deprecated
    public FactionData getAbyssData() { return new FactionData(0, Faction.ABYSS); }

    /**
     * Access the data on the Elementals for the player.
     * @return The data on the elemental faction.
     */
    @Deprecated
    public FactionData getElementalData() { return new FactionData(0, Faction.ELEMENTAL); }

    /**
     * Access the uuid of the player this data is on and return it.
     * @return The uuid of the player.
     */
    public UUID getUuid() { return uuid; }
}
