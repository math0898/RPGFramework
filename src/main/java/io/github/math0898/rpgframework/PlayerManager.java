package io.github.math0898.rpgframework;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import java.util.*;

import static io.github.math0898.rpgframework.main.plugin;

public class PlayerManager implements Listener { // todo needs cleaning. Copied from RPG 1.0.

    /**
     * Initializes the Player Manager to handle events.
     */
    public static void init () {
        Bukkit.getPluginManager().registerEvents(new PlayerManager(), plugin);
    }

    /**
     * Prints the given string into console as ChatColor.GRAY.
     * @param message The message to be sent to console
     */
    private static void console(String message) {
        console(message, ChatColor.GRAY);
    }

    /**
     * Prints the given string into console with the given coloring.
     * @param message The message be the sent to console
     * @param color The color the message should be in
     */
    private static void console(String message, ChatColor color) {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + ChatColor.LIGHT_PURPLE + "PlayMgr" + ChatColor.DARK_GRAY + "] " + color + message);
    }

    /**
     * An arraylist holding all the the userdata that has been currently loaded
     */
//    private static final ArrayList<UserData> userData = new ArrayList<>();

    /**
     * An array of death flavor messages.
     */
    private static final String[] deathFlavor = {" was slain by ", " met an honorable death by ", " was bested by ", " lost in a fight against ", ", in an epic duel lost to ", " met their fate by the hands of "};

    /**
     * Returns the arraylist of userdata.
     * @return The arraylist containing user data.
     */
//    public static ArrayList<UserData> getUserData(){ return userData; }

    /**
     * Returns the individual UserData that belongs to the given uuid.
     *
     * @param uuid The uuid of the play we're trying to access.
     * @return The UserData object that belongs to that player.
     */
//    public static UserData getUserData(UUID uuid) {
//        for (UserData d: userData) if (d.getUuid() == uuid)  return d;
//        return null;
//    }

    /**
     * Adds a user's data to the arraylist.
     */
//    public static void addUserData(UserData element) { userData.add(element); }

    /**
     * Removes the user's data from the arraylist
     */
//    public static void removeUserData(UUID uuid) { userData.removeIf(d -> d.getUuid() == uuid); }

    /**
     * An arraylist holding all the rpg-players currently on the server.
     */
    private static final ArrayList<RpgPlayer> players = new ArrayList<>();

    /**
     * Returns the arraylist of players.
     * @return The arraylist of players.
     */
//    public static ArrayList<RpgPlayer> getPlayers() { return players; }

    public static RpgPlayer getPlayer(UUID uuid) {
        for (RpgPlayer p: players) if (p.getUuid() == uuid) return p;

        return null;
    }

    public static RpgPlayer getPlayer (String name) {
        for (RpgPlayer p: players) if (name.equals(p.getName())) return p;
        return null;
    }

    /**
     * Adds a player to the array of players.
     */
    public static void addPlayer(RpgPlayer p) {
        console("Adding player to player list.");
        players.add(p);

        console("Player added to player list.", ChatColor.GREEN);

//        Bukkit.getScheduler().runTaskLater(main.plugin, p::passive, 20*20);
    }

    /**
     * Removes a player from the array of players.
     */
    public static void removePlayer(UUID uuid) {
        players.removeIf(d -> d.getUuid() == uuid);
    }

    /**
     * Scales the health of a player based on their max health.
     */
    public static void scaleHealth(Player p) {
        double max = Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        p.setHealthScale(max);
        p.setSaturatedRegenRate(((int) max) * 4);
        p.setUnsaturatedRegenRate(((int) max) * 4);
    }

    /**
     * Scales the health regeneration of a player based on their max health.
     * @param p The player who's regen is being scaled.
     * @param mod The multiplicative modifier to health regen.
     */
    public static void scaleRegen(Player p, double mod) {
        double max = Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        p.setSaturatedRegenRate((int)((max) * 4 * mod));
        p.setUnsaturatedRegenRate((int)((max) * 4 * mod));
    }

    /**
     * Heals the player to full
     */
    public static void healPlayer(Player p) { p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()); }

    /**
     * Prevents the loss of hunger due to regeneration.
     */
    public static void hunger(EntityExhaustionEvent event) {
        event.setCancelled(true);
    }

    /**
     * Runs the player manager when damaged.
     */
//    public static void onDamage(EntityDamageByEntityEvent event) {
//
//        Player player = (Player) event.getEntity(); //Check RPGEventListener
//        Entity attacker = event.getDamager();
//
//        Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).damaged(event);
//        if (event.isCancelled()) return;
//
//        if (player.getHealth() <= event.getDamage() ) {
//
//            if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) return;
//
//            String deathMessage;
//
//            event.setCancelled(true);
//
//            if (Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).revive()) return;
//
//            if (attacker instanceof Player) deathMessage = getPlayerRarity(player) + player.getName() + ChatColor.GRAY + deathFlavor[Math.abs(new Random().nextInt() % deathFlavor.length)] + attacker.getName();
//            else if (!attacker.isCustomNameVisible()) {
//                deathMessage = getPlayerRarity(player) + player.getName() + ChatColor.GRAY + deathFlavor[Math.abs(new Random().nextInt() % deathFlavor.length)] + "a " + attacker.getName();
//                RpgPlayer.dropAll(player);
//                player.setExp(player.getExp()/2);
//                player.setLevel(player.getLevel()/2);
//            } else {
//                deathMessage = getPlayerRarity(player) + player.getName() + ChatColor.GRAY + deathFlavor[Math.abs(new Random().nextInt() % deathFlavor.length)] + attacker.getCustomName();
//                player.sendMessage(attacker.getCustomName() + ChatColor.GRAY + " has left the fight upon your death");
//                attacker.remove();
//            }
//
//            Location spawn = player.getBedSpawnLocation();
//            if (spawn == null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " world");
//            else player.teleport(spawn);
//
//            healPlayer(player);
//            player.setFireTicks(0);
//
//            for (PotionEffect p: player.getActivePotionEffects()) player.removePotionEffect(p.getType());
//
//            for (Player p: Bukkit.getOnlinePlayers()) p.sendMessage(deathMessage);
//            console(deathMessage);
//
//            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 0.8f, 2f);
//        }
//    }

    /**
     * Adds the new player to the Player Manager when they join the server.
     *
     * @param event The player join event.
     */
    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        players.add(new RpgPlayer(event.getPlayer()));
    }

    /**
     * Removes the player from the PLayer Manager when they leave the server.
     *
     * @param event The player leave event.
     */
    @EventHandler
    public void onLeave (PlayerQuitEvent event) {
        removePlayer(event.getPlayer().getUniqueId());
    }
}
