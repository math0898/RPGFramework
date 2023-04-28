package sugaku.rpg.framework;

import sugaku.rpg.factions.FactionData;

import java.util.UUID;

public class UserData {

    /**
     * The data relating to the abyss for the player.
     */
    private FactionData AbyssData;

    /**
     * The data relating to the elementals for the player.
     */
    private FactionData ElementalData;

    /**
     * The UUID of the player who's data is loaded.
     */
    private final UUID uuid;

    /**
     * The xp the player has with each class.
     */
    private int[] xp;

    /**
     * Creates UserData with the given faction data.
     */
    public UserData(FactionData[] factionData, UUID uuid) {
        for (FactionData f : factionData) {
            switch(f.getFaction()) {
                case ABYSS: AbyssData = f;
                case ELEMENTAL: ElementalData = f;
            }
        }
        this.uuid = uuid;
    }

    /**
     * Creates UserData with the given faction data and class xp data.
     *
     * @param factionData The faction data array.
     * @param uuid        The uuid of the player who's data this is.
     * @param classXp     The xp for each class the player has joined.
     */
    public UserData(FactionData[] factionData, UUID uuid, int[] classXp) {
        for (FactionData f : factionData) {
            switch(f.getFaction()) {
                case ABYSS: AbyssData = f;
                case ELEMENTAL: ElementalData = f;
            }
        }
        this.uuid = uuid;
        xp = classXp;
    }

    /**
     * Accesses the data on the Abyss faction.
     * @return The data on the abyss faction.
     */
    public FactionData getAbyssData() { return AbyssData; }

    /**
     * Access the data on the Elementals for the player.
     * @return The data on the elemental faction.
     */
    public FactionData getElementalData() { return ElementalData; }

    /**
     * Access the uuid of the player this data is on and return it.
     * @return The uuid of the player.
     */
    public UUID getUuid() { return uuid; }

    /**
     * Access the xp of the player's classes and returns it.
     *
     * @return The int array which holds the xp for all of the classes.
     */
    public int[] getXp() { return xp; }
}
