package io.github.math0898.rpgframework;

import io.github.math0898.rpgframework.parties.Party;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;
import sugaku.rpg.main;

import java.util.*;

import static io.github.math0898.rpgframework.RPGFramework.plugin;

/**
 * The PlayerManager does some listening to events and managing interactions with players. It's main purpose is to track
 * and provide the RpgPlayer equivalents to Bukkit's Player.
 * // todo: Update to utilize singleton design pattern.
 *
 * @author Sugaku
 */
public class PlayerManager implements Listener {

    /**
     * An array of death flavor messages.
     */
    private static final String[] DEATH_FLAVOR = {" was slain by ", " met an honorable death by ", " was bested by ", " lost in a fight against ", ", in an epic duel lost to ", " met their fate by the hands of "};

    /**
     * An arraylist holding all the rpg-players currently on the server.
     */
    private static final HashMap<UUID, RpgPlayer> players = new HashMap<>();

    /**
     * Initializes the Player Manager to handle events.
     */
    public static void init () {
        Bukkit.getPluginManager().registerEvents(new PlayerManager(), plugin);
        // Runs once every 5 minutes. 20 Ticks -> 1 Second, 60 Seconds -> 1 Minute, 5 Minutes.
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, PlayerManager::saveAllPlayers, 60 * 5 * 20, 60 * 5 * 20);
    }

    /**
     * Prints the given string into console as ChatColor.GRAY.
     * @param message The message to be sent to console
     */
    private static void console (String message) {
        console(message, ChatColor.GRAY);
    }

    /**
     * Prints the given string into console with the given coloring.
     * @param message The message be sent to console
     * @param color The color the message should be in
     */
    private static void console (String message, ChatColor color) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + ChatColor.LIGHT_PURPLE + "PlayMgr" + ChatColor.DARK_GRAY + "] " + color + message);
    }

    /**
     * Pulls a specific player from the player's list. Runs in constant time as promised by Java's HashMaps.
     *
     * @param uuid The uuid of the player to grab.
     * @return Returns the found player. Potentially null if no such player.
     */
    public static RpgPlayer getPlayer (UUID uuid) {
        return players.get(uuid);
    }

    /**
     * Pulls a specific player depending on their name. This takes O(n) since our HashMap is mapped by UUID not names.
     *
     * @param name The name of the player to find.
     * @return Returns the found player, or null.
     */
    public static RpgPlayer getPlayer (String name) {
        for (RpgPlayer p: players.values())
            if (name.equals(p.getName()))
                return p;
        return null;
    }

    /**
     * Adds a player to the array of players.
     *
     * @param p The RpgPlayer to add to the list.
     */
    public static void addPlayer (RpgPlayer p) {
        console("Adding player to player list.");
        players.put(p.getUuid(), p);
        console("Player added to player list.", ChatColor.GREEN);
        Bukkit.getScheduler().runTaskLater(main.plugin, p::passive, 20*20);
    }

    /**
     * Removes a player from the array of players.
     *
     * @param player The player to remove from the PlayerManager.
     */
    public static void removePlayer (@Nullable RpgPlayer player) {
        if (player == null) return;
        removePlayer(player.getUuid());
    }

    /**
     * Removes a player from the array of players.
     *
     * @param uuid The uuid of the player to remove from the PlayerManager.
     */
    public static void removePlayer (@Nullable UUID uuid) {
        players.remove(uuid);
    }

    /**
     * Scales the health of a player based on their max health.
     *
     * @param p The player who needs to have their health scaled.
     */
    public static void scaleHealth (Player p) {
        double max = Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        p.setHealthScale(max);
        p.setSaturatedRegenRate(20 * 4); // Number of ticks to gain 1 hp
        p.setUnsaturatedRegenRate(20 * 4);
    }

    /**
     * Scales the health regeneration of a player based on our re-balancing, and a modifier.
     *
     * @param p The player who's regen is being scaled.
     * @param mod The multiplicative modifier to health regen.
     */
    public static void scaleRegen (Player p, double mod) {
        p.setSaturatedRegenRate((int) (20 * 4 * mod)); // Number of ticks to gain 1 hp.
        p.setUnsaturatedRegenRate((int) (20 * 4 * mod));
    }

    /**
     * Prevents the loss of hunger due to regeneration.
     */
    public static void hunger (EntityExhaustionEvent event) {
        event.setCancelled(true);
    }

    /**
     * Runs the player manager when damaged by the environment.
     *
     * @param event The damage event to consider.
     */ // todo: This should probably be considered at the end of Advanced Damage.
    public static void environmentalDamage (EntityDamageEvent event) {
        Player player = (Player) event.getEntity();
        if (player.getHealth() <= event.getDamage()) {
            if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) return;
            RpgPlayer rpg = getPlayer(player.getUniqueId());
            if (rpg == null) return;
            if (rpg.inCombat()) {
                rpg.damaged(event);
                if (event.isCancelled()) return;
                if (player.getHealth() > event.getDamage()) return;
                event.setCancelled(true);
                if (rpg.revive()) return;
                honorableDeath(rpg);
            } else {
                event.setCancelled(true);
                dishonorableDeath(rpg, event.getCause());
            }
        }
    }

    /**
     * Runs the player manager when damaged.
     *
     * @param event The damage event to be consdiered.
     */ // todo: This should probably be considered at the end of Advanced Damage.
    public static void onDamage (EntityDamageByEntityEvent event) {

        Player player = (Player) event.getEntity(); //Check RPGEventListener
        RpgPlayer rpg = PlayerManager.getPlayer(player.getUniqueId());
        if (rpg == null) return;
        rpg.damaged(event);
        if (event.isCancelled()) return;
        if (player.getHealth() > event.getDamage()) return;

        if (player.getHealth() <= event.getDamage() ) {
            if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) return;
            event.setCancelled(true);
            if (rpg.revive()) return;
            honorableDeath(rpg);
        }
    }

    /**
     * Called whenever a player dies honorably.
     *
     * @param rpg The RpgPlayer to consider.
     */
    public static void honorableDeath (RpgPlayer rpg) {
        Entity killer = rpg.getLastHitBy();
        Player player = rpg.getBukkitPlayer();
        String deathMessage;
        if (killer instanceof Player) {
            RpgPlayer rpgKiller = PlayerManager.getPlayer(killer.getUniqueId());
            if (rpgKiller != null) deathMessage = rpg.getPlayerRarity() + player.getName() + ChatColor.GRAY + DEATH_FLAVOR[Math.abs(new Random().nextInt() % DEATH_FLAVOR.length)] + rpgKiller.getPlayerRarity() + killer.getName();
            else deathMessage = rpg.getPlayerRarity() + player.getName() + ChatColor.GRAY + DEATH_FLAVOR[Math.abs(new Random().nextInt() % DEATH_FLAVOR.length)] + ChatColor.BLACK + killer.getName();
        } else if (killer.isCustomNameVisible()) {
            deathMessage = rpg.getPlayerRarity() + player.getName() + ChatColor.GRAY + DEATH_FLAVOR[Math.abs(new Random().nextInt() % DEATH_FLAVOR.length)] + killer.getCustomName();
            player.sendMessage(killer.getCustomName() + ChatColor.GRAY + " has left the fight upon your death");
            killer.remove();
        } else
            deathMessage = rpg.getPlayerRarity() + player.getName() + ChatColor.GRAY + DEATH_FLAVOR[Math.abs(new Random().nextInt() % DEATH_FLAVOR.length)] + killer.getName();
        allDeaths(rpg, deathMessage);
    }

    /**
     * Called whenever a player dies to the elements.
     *
     * @param rpg The RpgPlayer to consider.
     */
    public static void dishonorableDeath (RpgPlayer rpg, EntityDamageEvent.DamageCause cause) {
        Player player = rpg.getBukkitPlayer();
        String deathMessage = rpg.getPlayerRarity() + player.getName() + ChatColor.GRAY + " died to " + cause.toString().toLowerCase();
        rpg.dropAll();
        player.setExp(player.getExp()/2);
        player.setLevel(player.getLevel()/2);
        allDeaths(rpg, deathMessage);
    }

    /**
     * Called whenever a player dies to teleport them to spawn and send the death message.
     *
     * @param rpg The RpgPlayer to return to their spawn.
     * @param msg The death message to send into console.
     */
    public static void allDeaths (RpgPlayer rpg, String msg) {
        Player player = rpg.getBukkitPlayer();
        Location spawn = player.getBedSpawnLocation();
        console("Death location: " + rpg.getBukkitPlayer().getLocation());
        if (spawn == null) player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        else player.teleport(spawn);

        rpg.heal();
        player.setFireTicks(0);
        for (PotionEffect p: player.getActivePotionEffects()) player.removePotionEffect(p.getType());

        for (Player p: Bukkit.getOnlinePlayers()) p.sendMessage(msg);
        console(msg);

        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.8f, 2f);
    }

    /**
     * Adds the new player to the Player Manager when they join the server.
     *
     * @param event The player join event.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onJoin (PlayerJoinEvent event) {
        console("Adding player to player list.");
        RpgPlayer player = new RpgPlayer(event.getPlayer());
        players.put(player.getUuid(), player);
        Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> player.heal(), 5);
        DataManager.getInstance().load(player);
        console("Player added to player list.", ChatColor.GREEN);
    }

    /**
     * Removes the player from the PLayer Manager when they leave the server.
     *
     * @param event The player leave event.
     */
    @EventHandler
    public void onLeave (PlayerQuitEvent event) {
        RpgPlayer rpgPlayer = getPlayer(event.getPlayer().getUniqueId());
        DataManager.getInstance().save(rpgPlayer);
        if (rpgPlayer != null) {
            Party party = rpgPlayer.getParty();
            if (party != null) party.removePlayer(rpgPlayer.getBukkitPlayer());
            rpgPlayer.setParty(null);
        }
        removePlayer(rpgPlayer);
    }

    /**
     * Called periodically while the server runs to update and save player's files. This helps to reduce the rollback
     * time in the event of a hard crash.
     */
    public static void saveAllPlayers () {
        if (players.isEmpty()) return;
        DataManager dataManager = DataManager.getInstance();
        for (RpgPlayer player : players.values())
            dataManager.save(player);
    }
}
