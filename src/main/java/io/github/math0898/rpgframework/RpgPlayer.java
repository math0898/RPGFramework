package io.github.math0898.rpgframework;

import io.github.math0898.rpgframework.items.EquipmentSlots;
import io.github.math0898.rpgframework.items.ItemManager;
import io.github.math0898.rpgframework.items.RpgItem;
import io.github.math0898.rpgframework.parties.Party;
import io.github.math0898.utils.items.AttributeBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import io.github.math0898.rpgframework.classes.Classes;
import sugaku.rpg.framework.players.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

public class RpgPlayer { // todo Needs updating for the new framework. Copied from RPG 1.0.

    /**
     * A static prefix to send to all players.
     */
    private static final String MESSAGE_PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    /**
     * A list of artifacts that have been collected by this player.
     * -- GETTER --
     *  Accessor method for the artifact collection of this player.
     *
     * @return The artifact collection.
     */
    @Getter
    private final List<String> artifactCollection = new ArrayList<>();

    /**
     * Default constructor for an RpgPlayer construct just requiring an uuid.
     *
     * @param p The player this construct points to.
     */
    public RpgPlayer (Player p) {
        this.uuid = p.getUniqueId();
        this.name = p.getName();
        refresh();
    }

    /**
     * Uuid of the player this construct points to.
     * -- GETTER --
     *  Returns the uuid of the player this construct points to.
     *
     * @return The uuid of the player.
     */
    @Getter
    private final UUID uuid;

    private final String name;

    private Party party = null;

    private Party pendingParty = null;

    private Classes combatClass = Classes.NONE;

//    private Class classObject = new None(Bukkit.getPlayer(uuid));

    /**
     * Adds a list of artifacts to the list that this Player has already completed.
     *
     * @param collection The artifacts to tag onto the list.
     */
    public void addCollectedArtifacts (List<String> collection) {
        artifactCollection.addAll(collection);
        refresh();
    }

    /**
     * The refresh method recalculates artifact bonuses given to the player, reapplies the attributes bonuses, and heals
     * the player to full.
     */
    public void refresh () {
        Player player = getBukkitPlayer();
        if (!artifactCollection.isEmpty()) {
            ItemManager itemManager = ItemManager.getInstance();
            double healthMod = 0;
            double damageMod = 0;
            double armorMod = 0;
            double toughnessMod = 0;
            double attackSpeedMod = 0;
            for (String a : artifactCollection) {
                RpgItem item = itemManager.getRpgItem(a);
                if (item.getSlot() != EquipmentSlots.ARTIFACT) continue;
                healthMod += item.getHealth();
                damageMod += item.getDamage();
                armorMod += item.getArmor();
                toughnessMod += item.getToughness();
                attackSpeedMod += item.getAttackSpeed();
            }
            AttributeInstance healthInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            AttributeInstance damageInstance = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            AttributeInstance armorInstance = player.getAttribute(Attribute.GENERIC_ARMOR);
            AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            AttributeInstance toughnessInstance = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
            if (healthMod != 0 && healthInstance != null) {
                healthInstance.removeModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_MAX_HEALTH, healthMod / 5.0, null));
                healthInstance.addModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_MAX_HEALTH, healthMod / 5.0, null));
            }
            if (damageMod != 0 && damageInstance != null) { // todo: Requires more testing damage might be bugged or interacting funny.
                damageInstance.removeModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageMod / 5.0, null));
                damageInstance.addModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageMod / 5.0, null));
            }
            if (armorMod != 0 && armorInstance != null) {
                armorInstance.removeModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ARMOR, armorMod, null));
                armorInstance.addModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ARMOR, armorMod, null));
            }
            if (attackSpeedMod != 0 && attackSpeed != null) {
                attackSpeed.removeModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedMod, null));
                attackSpeed.addModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedMod, null));
            }
            if (toughnessMod != 0 && toughnessInstance != null) {
                toughnessInstance.removeModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessMod, null));
                toughnessInstance.addModifier(AttributeBuilder.attributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, toughnessMod, null));
            }
            player.setHealthScale(20);
            PlayerManager.scaleRegen(player, 1);
        }
        heal();
    }

    /**
     * Gets a gear score for the given player.
     * @param player The player we're calculating gear score for.
     */
    public static int getGearScore (Player player) {
        int runningScore = 0;
//        EntityEquipment equipment = player.getEquipment();
//        if (equipment == null) return 0;
//        for (EquipmentSlot slot : EquipmentSlot.values()) {
//            if (slot.equals(EquipmentSlot.OFF_HAND)) continue;
//            ItemStack item = equipment.getItem(slot);
//        }

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
        return combatClass.getFormattedName();
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

    //TODO: Implement the damage bonus of the bow
}
