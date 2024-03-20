package io.github.math0898.rpgframework;

import io.github.math0898.rpgframework.parties.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import sugaku.rpg.framework.classes.Classes;

import java.util.UUID;
import java.util.Objects;

public class RpgPlayer { // todo Needs updating for the new framework. Copied from RPG 1.0.

    /**
     * Default constructor for an RpgPlayer construct just requiring an uuid.
     *
     * @param p The player this construct points to.
     */
    public RpgPlayer(Player p) throws NullPointerException {
        this.uuid = p.getUniqueId();
        this.name = p.getName();
        refresh(p);
    }

    /**
     * Uuid of the player this construct points to.
     */
    private final UUID uuid;

    private final String name;

    private Party party = null;

    private Party pendingParty = null;

    private Classes combatClass = Classes.NONE;

//    private Class classObject = new None(Bukkit.getPlayer(uuid));

    /**
     * Returns the uuid of the player this construct points to.
     * @return The uuid of the player.
     */
    public UUID getUuid() { return uuid; }

    /**
     * Refreshes the player's stats.
     */
    public void refresh(Player p) throws NullPointerException {
        p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

    /**
     * Gets a gear score for the given player.
     * @param player The player we're calculating gear score for.
     */
    public static int getGearScore(Player player) {

        int runningScore = 0;

        ItemStack[] collection = player.getInventory().getArmorContents();

        for (ItemStack item: collection) {
            if (item != null) {
                if (item.getEnchantments().size() > 1) runningScore += 15;

                try {
                    if (Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getLore()).contains("Modified Vanilla")) {
                        switch (item.getType()) {
                            case LEATHER_BOOTS, LEATHER_LEGGINGS, LEATHER_CHESTPLATE, LEATHER_HELMET -> runningScore += 5;
                            case CHAINMAIL_BOOTS, CHAINMAIL_LEGGINGS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET -> runningScore += 6;
                            case GOLDEN_BOOTS, GOLDEN_LEGGINGS, GOLDEN_CHESTPLATE, GOLDEN_HELMET -> runningScore += 8;
                            case IRON_BOOTS, IRON_LEGGINGS, IRON_CHESTPLATE, IRON_HELMET -> runningScore += 10;
                            case DIAMOND_BOOTS, DIAMOND_LEGGINGS, DIAMOND_CHESTPLATE, DIAMOND_HELMET -> runningScore += 13;
                            case NETHERITE_BOOTS, NETHERITE_LEGGINGS, NETHERITE_CHESTPLATE, NETHERITE_HELMET -> runningScore += 15;
                        }
                    } else runningScore += 20;
                } catch (NullPointerException ignored) { }
            }
        }

        return runningScore;
    }

    /**
     * Drops the entire player's inventory at their current location.
     * @param player The player who's inventory is being dropped.
     */
    public static void dropAll(Player player) {

        World world = player.getWorld();

        for (ItemStack e: player.getInventory()) {
            try {
                world.dropItem(player.getLocation(), e);
            } catch (IllegalArgumentException ignored) { }
        }

        player.getInventory().clear();
    }

    /**
     * Returns the ChatColor rarity of the player given, based on gear score.
     * @param player The player who's rarity is being determined.
     * @return ChatColor the color rarity of the player.
     */
    public static ChatColor getPlayerRarity(Player player) {

        int gearScore = getGearScore(player);

        if (gearScore <= 100) return ChatColor.WHITE;
        else if (gearScore <= 200) return ChatColor.GREEN;
        else if (gearScore <= 300) return ChatColor.BLUE;
        else if (gearScore <= 400) return ChatColor.GOLD;
        else if (gearScore <= 500) return ChatColor.LIGHT_PURPLE;
        else return ChatColor.RED;
    }

    public ChatColor getPlayerRarity() {
        return getPlayerRarity(this.getBukkitPlayer());
    }

    public static String getFormattedHealth (Player player) {
        AttributeInstance instance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double max;
        if (instance == null) max = 100.0;
        else max = instance.getValue() * 5.0;
        double current = player.getHealth();
        ChatColor prefix = ChatColor.GREEN;
        if (current / max < 0.75) prefix = ChatColor.YELLOW;
        else if (current / max < 0.50) prefix = ChatColor.RED;
        return prefix + "" + current;
    }

    public String getFormattedHealth () {
        return getFormattedHealth(getBukkitPlayer());
    }

    public Party getParty() {
        return party;
    }

    public void joinParty (Party p) {
        party = p;
    }

    public void leaveParty() { party = null; }

    public Party getPendingParty() { return pendingParty; }

    public void setPendingParty(Party p) { pendingParty = p; }

    public Player getBukkitPlayer() { return Bukkit.getPlayer(uuid); }

    public String getName() {
        return name;
    }

    public String getFormattedClass() {
//        return ChatColor.GRAY + "None";
        return switch (combatClass) {
            case GLADIATOR -> Classes.classColor(combatClass) + "Gladiator";
            case BERSERKER -> Classes.classColor(combatClass) + "Berserker";
            case NONE -> Classes.classColor(combatClass) + "None";
            case MARKSMEN -> Classes.classColor(combatClass) + "Marksmen";
            case ASSASSIN -> Classes.classColor(combatClass) + "Assassin";
            case PALADIN -> Classes.classColor(combatClass) + "Paladin";
            case BARD -> Classes.classColor(combatClass) + "Bard";
            case PYROMANCER -> Classes.classColor(combatClass) + "Pyromancer";
        };
    }

//    public Classes getCombatClass() { return combatClass; }
//
//    public String getCombatClassString() {
//        switch(combatClass) {
//            case ASSASSIN: return "Assassin";
//            case BARD: return "Bard";
//            case BERSERKER: return "Berserker";
//            case NONE: return "None";
//            case MARKSMEN: return "Marksmen";
//            case GLADIATOR: return "Gladiator";
//            case PALADIN: return "Paladin";
//            case PYROMANCER: return "Pyromancer";
//        }
//        return null;
//    }
//
//    public void joinClass(Classes c) {
//        this.combatClass = c;
//        switch (c) {
//            case BARD: classObject = new Bard(this); break;
//            case ASSASSIN: classObject = new Assassin(this); break;
//            case BERSERKER: classObject = new Berserker(this); break;
//            case PALADIN: classObject = new Paladin(this); break;
//            case PYROMANCER: classObject = new Pyromancer(this); break;
//            case NONE: default: classObject = new None(this); break;
//        }
//    }
//
//    public String getArchetype() {
//        switch (combatClass) {
//            case ASSASSIN: case GLADIATOR: case MARKSMEN: case BERSERKER: return "Fighter";
//            case NONE: return "None";
//            default: return "Caster";
//        }
//    }
//
//    public void onInteract(PlayerInteractEvent event) { classObject.onInteract(event); }
//
//    public boolean revive() { return classObject.onDeath(); }
//
//    public void damaged(EntityDamageByEntityEvent event) { classObject.damaged(event); }
//
//    public void attacker(EntityDamageByEntityEvent event) { classObject.attack(event); }
//
//    public void passive() {
//        try {
//            classObject.passive();
//            Bukkit.getScheduler().runTaskLater(main.plugin, this::passive, 20*20);
//        } catch (Exception ignored) { }
//    }

    //TODO: Implement the damage bonus of the bow
}
