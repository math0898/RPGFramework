package sugaku.rpg.factions;

public class FactionData {

    /**
     * The reputation of the party with the current faction.
     */
    private int reputation;

    /**
     * The name of the faction represented by this data.
     */
    private final Faction faction;

    /**
     * Builds Faction Data using reputation and faction.
     * @param reputation The current reputation of the player.
     * @param faction The faction this data is on.
     */
    public FactionData(int reputation, Faction faction) {
        this.reputation = reputation;
        this.faction = faction;
    }

    /**
     * Accesses the reputation of the player with the faction and returns it as an int.
     * @return The reputation of the player with the given faction.
     */
    public int getReputation() { return reputation; }

    /**
     * Accesses the faction that this data relates to.
     * @return The Faction enum value this data is on.
     */
    public Faction getFaction() { return faction; }

    /**
     * Changes reputation by the given value. Sets it to zero if reputation would be negative after the change.
     * @param delta The change in reputation of the faction.
     */
    public void addReputation(int delta) {
        reputation += delta;
        if (reputation < 0) reputation = 0;
    }

    /**
     * Sets the reputation to the given value.
     * @param newValue The new value of the reputation.
     */
    public void setReputation(int newValue) { reputation = newValue; }
}
