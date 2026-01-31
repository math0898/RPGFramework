package sugaku.rpg.framework.players;

import io.github.math0898.rpgframework.Cooldown;
import io.github.math0898.rpgframework.RPGFramework;
import io.github.math0898.rpgframework.classes.AbstractClass;
import io.github.math0898.rpgframework.classes.implementations.*;
import io.github.math0898.rpgframework.classes.Class;
import io.github.math0898.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import io.github.math0898.rpgframework.classes.Classes;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sugaku.rpg.main;
import io.github.math0898.rpgframework.parties.Party;
import sugaku.rpg.mobs.CustomMob;

import java.util.*;

public class RpgPlayer {

    /**
     * A static prefix to send to all players.
     */
    private static final String MESSAGE_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

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
     * The player's level, cached, so we don't have to recalculate it.
     */
    private long level = 1;

    /**
     * Default constructor for an RpgPlayer construct just requiring a uuid.
     * @param p The player this construct points to.
     */
    public RpgPlayer (Player p) {
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
     * Sends the given message to this player.
     *
     * @param message The message to send to this player.
     */
    public void sendMessage (String message) {
        sendMessage(message, true);
    }

    /**
     * Sends the given message to this player.
     *
     * @param message The message to send to this player.
     * @param prefix  Whether to include the RPG prefix or not.
     */
    public void sendMessage (String message, boolean prefix) {
        if (prefix) getBukkitPlayer().sendMessage(MESSAGE_PREFIX + message);
        else getBukkitPlayer().sendMessage(message);
    }

    /**
     * Sets the experience that this play has accumulated.
     *
     * @param xp The amount of xp that this player has gotten.
     */
    public void setExperience (long xp) {
        experience = xp;
        calculateLevel();
        refresh(getBukkitPlayer());
    }

    /**
     * Calculates this player's level and updates the cached value.
     */
    private void calculateLevel () {
        long thirties = experience / 30;
        long bar = 1;
        long countingLevel = 0;
        while (thirties > 0) {
            thirties -= bar;
            bar++;
            if (thirties >= 0) countingLevel++;
        }
        level = countingLevel + 1;
    }

    /**
     * Does a small calculation to determine what level this player is currently at.
     *
     * @return This player's current level.
     */
    public long getLevel () {
        return level;
    }

    /**
     * Adds the given experience to this player.
     *
     * @param awarded The amount of awarded experience.
     */
    public void giveExperience (long awarded) {
        long startingLevel = getLevel();
        experience += awarded;
        calculateLevel();
        long finalLevel = getLevel();
        if (startingLevel < finalLevel) levelUp();
    }

    /**
     * Called whenever the player levels up.
     */
    public void levelUp () {
        sugaku.rpg.framework.players.RpgPlayer p = io.github.math0898.rpgframework.PlayerManager.getPlayer(uuid);
        if (p == null) {
            RPGFramework.console("Encountered a strange error where sugaku.rpg has an RpgPlayer object but no io.github.math0898 RpgPlayer was found.", ChatColor.RED);
            return;
        }
        refresh(getBukkitPlayer());
        getBukkitPlayer().playSound(getBukkitPlayer(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1.0f);
        p.sendMessage(ChatColor.GREEN + "You've leveled up! Level: " + getLevel());
        if (level % 5 != 0)
            p.sendMessage(StringUtils.convertHexCodes("#F454DA") + " +5 Health");
        else
            p.sendMessage(StringUtils.convertHexCodes("#D93747") + " +1 Damage");
    }

    /**
     * Refreshes the player's stats.
     */
    public void refresh (Player p) {
        // Every level except lvl 1, and lvls ending in 5/10.
        AttributeModifier healthMod = new AttributeModifier(new UUID(100, 234), "", ((getLevel() - 1) - (getLevel() / 5.0)) * 5, AttributeModifier.Operation.ADD_NUMBER);
        // Every level that ends in 5/10.
        AttributeModifier damageMod = new AttributeModifier(new UUID(100, 235), "", (getLevel() / 5.0), AttributeModifier.Operation.ADD_NUMBER);
        AttributeInstance healthInstance = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance damageInstance = p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (healthInstance != null) {
            Collection<AttributeModifier> modifiers = healthInstance.getModifiers();
            if (!modifiers.isEmpty())
                healthInstance.removeModifier(healthMod);
            healthInstance.addModifier(healthMod);
            p.setHealth(healthInstance.getValue());
            p.setHealthScale(20);
        } else {
            RPGFramework.console("Attempted to update " + p.getName() + "'s health but GENERIC_MAX_HEALTH instance is null.", ChatColor.RED);
        }
        if (damageInstance != null) {
            Collection<AttributeModifier> modifiers = damageInstance.getModifiers();
            if (!modifiers.isEmpty())
                damageInstance.removeModifier(damageMod);
            damageInstance.addModifier(damageMod);
        } else {
            RPGFramework.console("Attempted to update " + p.getName() + "'s damage but GENERIC_ATTACK_DAMAGE instance is null.", ChatColor.RED);
        }
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

    public void joinParty (Party p) {
        party = p;
    }

    public void leaveParty() { party = null; }

    public Party getPendingParty() { return pendingParty; }

    public void setPendingParty(Party p) { pendingParty = p; }

    public String getFormattedClass () {
        return combatClass.getFormattedName();
    }

    /**
     * Accessor method for a formatted version of this player's current health.
     *
     * @return A nicely colored string for this player's health.
     */
    public String getFormattedHealth () {
        double max = getMaxHealth();
        double current = getBukkitPlayer().getHealth();

        ChatColor prefix = ChatColor.GREEN;
        if (current / max < 0.75) prefix = ChatColor.YELLOW;
        else if (current / max < 0.50) prefix = ChatColor.RED;

        return prefix + "" + current;
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
        sugaku.rpg.framework.players.RpgPlayer player = io.github.math0898.rpgframework.PlayerManager.getPlayer(uuid);
        if (player == null) return;
        Party party = player.getParty();
        if (player.getParty() != null) party.setBoss(boss);
        activeBoss = boss;
    }

    /**
     * An accessor method for the boss that is actively fighting this player.
     *
     * @return The boss actively fighting this Party.
     */
    public CustomMob getActiveBossUnsafe () {
        sugaku.rpg.framework.players.RpgPlayer player = io.github.math0898.rpgframework.PlayerManager.getPlayer(uuid);
        if (player == null) return null;
        Party party = player.getParty();
        if (player.getParty() != null) {
            CustomMob candidate = party.getActiveBossUnsafe();
            if (candidate != null)
                return candidate;
        }
        return activeBoss;
    }

    /**
     * An accessor method for the boss that is actively fighting this player. Will validate the boss before returning.
     *
     * @return The boss actively fighting this Party.
     */
    public CustomMob getActiveBoss () {
        sugaku.rpg.framework.players.RpgPlayer player = io.github.math0898.rpgframework.PlayerManager.getPlayer(uuid);
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

    /**
     * A helpful utility method to wrangle all the friendly caster targets. i.e. {@link this} and if present, all party
     * members.
     *
     * @return A list of friendly casting targets.
     */
    public List<RpgPlayer> friendlyCasterTargets () {
        List<RpgPlayer> toReturn = new ArrayList<>();
        if (party != null) toReturn.addAll(party.getRpgPlayers());
        else toReturn.add(this);
        return toReturn;
    }

    /**
     * A helpful utility method to wrangle all nearby enemy caster targets.
     *
     * @param distance The distance from this RpgPlayer to look for targets.
     * @return A list of nearby enemy casting targets.
     */
    public List<LivingEntity> nearbyEnemyCasterTargets (double distance) {
        return nearbyEnemyCasterTargets(distance, distance, distance);
    }

    /**
     * A helpful utility method to wrangle all nearby enemy caster targets.
     *
     * @param dx The distance in the x direction to look for nearby targets.
     * @param dy The distance in the y direction to look for nearby targets.
     * @param dz The distance in the z direction to look for nearby targets.
     * @return A list of nearby enemy casting targets.
     */
    public List<LivingEntity> nearbyEnemyCasterTargets (double dx, double dy, double dz) {
        List<LivingEntity> toReturn = new ArrayList<>();
        List<RpgPlayer> friendlies = friendlyCasterTargets();
        List<LivingEntity> friendlyEntities = new ArrayList<>();
        for (RpgPlayer rpg : friendlies)
            friendlyEntities.add(rpg.getBukkitPlayer());
        List<Entity> entities = getBukkitPlayer().getNearbyEntities(dx, dy, dz);
        for (Entity e : entities)
            if (e instanceof LivingEntity le)
                if (!friendlyEntities.contains(le))
                    toReturn.add(le);
        return toReturn;
    }

    /**
     * Adds the given potion effect to this player.
     *
     * @param type The type of effect to add.
     * @param dur  The duration to add this effect.
     * @param lvl  The level to apply this effect at. (Not amplifier!)
     */
    public void addPotionEffect (PotionEffectType type, int dur, int lvl) {
        addPotionEffect(type, dur, lvl, false, false);
    }

    /**
     * Adds the given potion effect to this player.
     *
     * @param type    The type of effect to add.
     * @param dur     The duration to add this effect.
     * @param lvl     The level to apply this effect at. (Not amplifier!)
     * @param ambient Whether the particles are ambient or not.
     * @param hide    Should the particles be hidden.
     */
    public void addPotionEffect (PotionEffectType type, int dur, int lvl, boolean ambient, boolean hide) {
        getBukkitPlayer().addPotionEffect(new PotionEffect(type, dur, lvl - 1, ambient, hide));
    }

    /**
     * Removes the given potion effects from this RpgPlayer.
     *
     * @param effects The effects to remove from this RpgPlayer.
     */
    public void cleanseEffects (PotionEffectType... effects) {
        if (effects == null) return;
        Player player = getBukkitPlayer();
        for (PotionEffectType type : effects)
            player.removePotionEffect(type);
    }

    //TODO: Implement the damage bonus of the bow
}
