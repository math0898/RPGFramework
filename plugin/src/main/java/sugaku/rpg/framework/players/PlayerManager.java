package sugaku.rpg.framework.players;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.potion.PotionEffect;
import sugaku.rpg.main;

import java.util.*;

import static sugaku.rpg.framework.players.RpgPlayer.getPlayerRarity;

public class PlayerManager {

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
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + ChatColor.LIGHT_PURPLE + "Legacy PlayMgr" + ChatColor.DARK_GRAY + "] " + color + message);
    }

    /**
     * An array of death flavor messages.
     */
    private static final String[] deathFlavor = {" was slain by ", " met an honorable death by ", " was bested by ", " lost in a fight against ", ", in an epic duel lost to ", " met their fate by the hands of "};

    /**
     * An arraylist holding all the rpg-players currently on the server.
     */
    public static final ArrayList<RpgPlayer> players = new ArrayList<>();

    /**
     * Returns the arraylist of players.
     * @return The arraylist of players.
     */
//    public static ArrayList<RpgPlayer> getPlayers() { return players; }

    public static RpgPlayer getPlayer(UUID uuid) {
        for (RpgPlayer p: players) if (p.getUuid() == uuid) return p;

        return null;
    }

    public static RpgPlayer getPlayer(String name) {
        for (RpgPlayer p: players) if (Objects.requireNonNull(Bukkit.getPlayer(p.getUuid())).getName().equals(name)) return p;
        return null;
    }

    /**
     * Adds a player to the array of players.
     */
    public static void addPlayer(RpgPlayer p) {
        console("Adding player to player list.");
        players.add(p);

        console("Player added to player list.", ChatColor.GREEN);

        Bukkit.getScheduler().runTaskLater(main.plugin, p::passive, 20*20);
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
        p.setSaturatedRegenRate(20 * 4); // Number of ticks to gain 1 hp
        p.setUnsaturatedRegenRate(20 * 4);
    }

    /**
     * Scales the health regeneration of a player based on their max health.
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
    public static void hunger(EntityExhaustionEvent event) {
        event.setCancelled(true);
    }

    /**
     * Runs the player manager when damaged by the environment.
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
        if (killer instanceof Player)
            deathMessage = getPlayerRarity(player) + player.getName() + ChatColor.GRAY + deathFlavor[Math.abs(new Random().nextInt() % deathFlavor.length)] + getPlayerRarity((Player) killer) + killer.getName();
        else if (killer.isCustomNameVisible()) {
            deathMessage = getPlayerRarity(player) + player.getName() + ChatColor.GRAY + deathFlavor[Math.abs(new Random().nextInt() % deathFlavor.length)] + killer.getCustomName();
            player.sendMessage(killer.getCustomName() + ChatColor.GRAY + " has left the fight upon your death");
            killer.remove();
        } else
            deathMessage = getPlayerRarity(player) + player.getName() + ChatColor.GRAY + deathFlavor[Math.abs(new Random().nextInt() % deathFlavor.length)] + killer.getName();
        allDeaths(rpg, deathMessage);
    }

    /**
     * Called whenever a player dies to the elements.
     *
     * @param rpg The RpgPlayer to consider.
     */
    public static void dishonorableDeath (RpgPlayer rpg, EntityDamageEvent.DamageCause cause) {
        Player player = rpg.getBukkitPlayer();
        String deathMessage = getPlayerRarity(player) + player.getName() + ChatColor.GRAY + " died to " + cause.toString().toLowerCase();
        RpgPlayer.dropAll(player);
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
}
