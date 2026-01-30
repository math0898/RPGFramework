package sugaku.rpg.framework.players;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.classes.AbstractClass;
import io.github.math0898.rpgframework.classes.implementations.*;
import io.github.math0898.rpgframework.classes.Class;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import io.github.math0898.rpgframework.classes.Classes;
import sugaku.rpg.main;
import io.github.math0898.rpgframework.parties.Party;
import sugaku.rpg.mobs.CustomMob;

import java.util.UUID;
import java.util.Objects;

public class RpgPlayer {

    /**
     * The time in millis that this player was last fighting.
     */
    private long fighting = 0L;

    /**
     * The amount of time in millis it takes to be considered out of combat.
     */
    private static final long TIME_UNTIL_OUT_OF_COMBAT = 10000;

    /**
     * Whether we have applied effects for leaving combat yet or not.
     */
    private boolean appliedOutCombat = false;

    /**
     * The Entity that last successfully hit this player.
     */
    private Entity lastHitBy = null;

    /**
     * The amount of XP that this player has accumulated.
     */
    private long experience = 0;

    /**
     * Default constructor for an RpgPlayer construct just requiring a uuid.
     * @param p The player this construct points to.
     */
    public RpgPlayer(Player p) throws NullPointerException {
        this.uuid = p.getUniqueId();
        refresh(p);
    }

    /**
     * Uuid of the player this construct points to.
     */
    private final UUID uuid;

    private Classes combatClass = Classes.NONE;

    private Class classObject = new NoneClass(this);

    /**
     * A reference to any active bosses that are fighting this player.
     */
    private CustomMob activeBoss = null;

    /**
     * Returns the uuid of the player this construct points to.
     *
     * @return The uuid of the player.
     */
    public UUID getUuid () {
        return uuid;
    }

    /**
     * Returns the experience that this player has accumulated.
     *
     * @return The amount of experience owned by this player.
     */
    public long getExperience () {
        return experience;
    }

    /**
     * Sets the experience that this play has accumulated.
     *
     * @param xp The amount of xp that this player has gotten.
     */
    public void setExperience (long xp) {
        experience = xp;
    }

    /**
     * Does a small calculation to determine what level this player is currently at.
     *
     * @return This player's current level.
     */
    public long getLevel () {
        return (experience / 300) + 1;
    }

    /**
     * Adds the given experience to this player.
     *
     * @param awarded The amount of awarded experience.
     */
    public void giveExperience (long awarded) {
        long startingLevel = getLevel();
        experience += awarded;
        long finalLevel = getLevel();
        if (startingLevel < finalLevel) levelUp();
    }

    /**
     * Called whenever the player levels up.
     */
    public void levelUp () {
        io.github.math0898.rpgframework.RpgPlayer p = io.github.math0898.rpgframework.PlayerManager.getPlayer(uuid);
        if (p == null) {
            RPGFramework.console("Encountered a strange error where sugaku.rpg has an RpgPlayer object but no io.github.math0898 RpgPlayer was found.", ChatColor.RED);
            return;
        }
        p.sendMessage(ChatColor.GREEN + "You've leveled up! Level: " + getLevel());
    }

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
                            case LEATHER_BOOTS: case LEATHER_LEGGINGS: case LEATHER_CHESTPLATE: case LEATHER_HELMET: runningScore += 5; break;
                            case CHAINMAIL_BOOTS: case CHAINMAIL_LEGGINGS: case CHAINMAIL_CHESTPLATE: case CHAINMAIL_HELMET: runningScore += 6; break;
                            case GOLDEN_BOOTS: case GOLDEN_LEGGINGS: case GOLDEN_CHESTPLATE: case GOLDEN_HELMET: runningScore += 8; break;
                            case IRON_BOOTS: case IRON_LEGGINGS: case IRON_CHESTPLATE: case IRON_HELMET: runningScore += 10; break;
                            case DIAMOND_BOOTS: case DIAMOND_LEGGINGS: case DIAMOND_CHESTPLATE: case DIAMOND_HELMET: runningScore += 13; break;
                            case NETHERITE_BOOTS: case NETHERITE_LEGGINGS: case NETHERITE_CHESTPLATE: case NETHERITE_HELMET: runningScore += 15; break;
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

    public Party getParty() { return null; }

    public Player getBukkitPlayer() { return Bukkit.getPlayer(uuid); }

    public String getName() { return getBukkitPlayer().getName(); }

    public Classes getCombatClass() { return combatClass; }

    public String getCombatClassString() { // todo: This might need to be changed.
        char[] str = combatClass.toString().toLowerCase().toCharArray();
        str[0] = Character.toUpperCase(str[0]);
        return new String(str);
    }

    public void joinClass(Classes c) {
        this.combatClass = c;
        switch (c) {
            case BARD -> classObject = new BardClass(this);
            case ASSASSIN -> classObject = new AssassinClass(this);
            case BERSERKER -> classObject = new BerserkerClass(this);
            case PALADIN -> classObject = new PaladinClass(this);
            case PYROMANCER -> classObject = new PyromancerClass(this);
            case NONE -> classObject = new NoneClass(this);
        }
    }

    public String getArchetype() {
        return switch (combatClass) {
            case ASSASSIN, /*GLADIATOR, MARKSMEN,*/ BERSERKER -> "Fighter";
            case NONE -> "None";
            default -> "Caster";
        };
    }

    public void onInteract(PlayerInteractEvent event) { classObject.onInteract(event); }

    public boolean revive() { return !classObject.onDeath(); }

    /**
     * Used to verify whether a player is in combat.
     *
     * @return Whether the player has been attacked in the last bit of time.
     */
    public boolean inCombat () {
        return System.currentTimeMillis() - fighting < TIME_UNTIL_OUT_OF_COMBAT;
    }

    /**
     * Called whenever this player gets attacked by another entity.
     *
     * @param event The EntityDamageByEntityEvent to consider.
     */
    public void damaged (EntityDamageEvent event) {
        classObject.damaged(event);
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            fighting = System.currentTimeMillis();
            enteringCombat();
            lastHitBy = damageByEntityEvent.getDamager();
        }
    }

    /**
     * Called whenever this player attacks another entity.
     *
     * @param event The EntityDamageByEntityEvent to consider.
     */
    public void attacker (EntityDamageByEntityEvent event) {
        fighting = System.currentTimeMillis();
        enteringCombat();
        classObject.attack(event);
        lastHitBy = event.getEntity();
    }

    public void passive() {
        try {
            classObject.passive();
            Bukkit.getScheduler().runTaskLater(main.plugin, this::passive, 20*20);
            if (!inCombat() && !appliedOutCombat)
                leaveCombat();
        } catch (Exception ignored) { }
    }

    /**
     * Called whenever a player is attacked while in combat.
     */
    public void enteringCombat () {
        if (appliedOutCombat)
            getBukkitPlayer().sendMessage(ChatColor.RED + "Entering Combat");
        PlayerManager.scaleRegen(getBukkitPlayer(), 1.0);
        appliedOutCombat = false;
    }

    /**
     * Called periodically when a player is not in combat.
     */
    public void leaveCombat () {
        if (!appliedOutCombat) {
            getBukkitPlayer().sendMessage(ChatColor.GREEN + "Left Combat");
            getBukkitPlayer().playSound(getBukkitPlayer(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 1.0f);
        }
        appliedOutCombat = true;
        PlayerManager.scaleRegen(getBukkitPlayer(), 0.25);
    }

    /**
     * An accessor method to get the last Entity that attacked this Player.
     *
     * @return The entity that last attacked the Player.
     */
    public Entity getLastHitBy () {
        return lastHitBy;
    }

    /**
     * A Utility method to obtain the max health of this RpgPlayer.
     *
     * @return The maximum health of this player.
     */
    public double getMaxHealth () {
        AttributeInstance instance = getBukkitPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (instance == null) return 20.0;
        return instance.getValue();
    }

    /**
     * Heals the player by the given amount.
     *
     * @param amount The amount to heal the player by.
     */
    public void heal (double amount) {
        getBukkitPlayer().setHealth(Math.min(getBukkitPlayer().getHealth() + amount, getMaxHealth()));
    }

    /**
     * Heals the player to max.
     */
    public void heal () {
        getBukkitPlayer().setHealth(getMaxHealth());
    }

    /**
     * Resets all the cooldowns that are in this RpgPlayer.
     */
    public void resetCooldowns () {
        Class tmp = classObject;
        if (tmp instanceof AbstractClass rpg)
            for (Cooldown cd : rpg.getCooldowns())
                cd.setComplete();
    }

    /**
     * Sets the active boss.
     *
     * @param boss The boss to assign to this player.
     */
    public void setBoss (CustomMob boss) {
        io.github.math0898.rpgframework.RpgPlayer player = io.github.math0898.rpgframework.PlayerManager.getPlayer(uuid);
        if (player == null) return;
        Party party = player.getParty();
        if (player.getParty() != null) party.setBoss(boss);
        activeBoss = boss;
    }

    /**
     * An accessor method for the boss that is actively fighting this player. Will validate the boss before returning.
     *
     * @return The boss actively fighting this Party.
     */
    public CustomMob getActiveBoss () {
        io.github.math0898.rpgframework.RpgPlayer player = io.github.math0898.rpgframework.PlayerManager.getPlayer(uuid);
        if (player == null) return null;
        Party party = player.getParty();
        if (player.getParty() != null) {
            CustomMob candidate = party.getActiveBoss();
            if (candidate != null)
                return candidate;
        }
        if (activeBoss != null)
            if (activeBoss.isSpawned())
                if (!activeBoss.getEntity().isValid() || activeBoss.getEntity().isDead())
                    activeBoss = null;
        return activeBoss;
    }

    //TODO: Implement the damage bonus of the bow
}
