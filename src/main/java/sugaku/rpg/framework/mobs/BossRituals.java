package sugaku.rpg.framework.mobs;

import io.github.math0898.rpgframework.RPGFramework;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sugaku.rpg.framework.items.ItemsManager;
import sugaku.rpg.main;
import sugaku.rpg.mobs.Bosses;
import sugaku.rpg.mobs.teir1.eiryeras.EiryerasBoss;
import sugaku.rpg.mobs.teir1.feyrith.FeyrithBoss;
import sugaku.rpg.mobs.teir1.krusk.KruskBoss;

import java.util.Objects;

public class BossRituals {

    /**
     * The prefix sent with every message.
     */
    private static final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    /**
     * Sends a message to the given recipient.
     */
    public static void send(CommandSender user, String message) {
        user.sendMessage(prefix + message);
    }

    public static void general(PlayerDropItemEvent event) { ritualStart(event); }

    public static void ritualStart(PlayerDropItemEvent event) {

        Item drop = event.getItemDrop();
        Player player = event.getPlayer();
        ItemStack stack = drop.getItemStack();
        if (stack.equals(ItemsManager.KruskSpawn)
                || stack.equals(RPGFramework.itemManager.getItem("krusk:Spawn"))
                || stack.equals(RPGFramework.itemManager.getItem("krusk:LegacySpawn"))) {
            drop.setPickupDelay(60);
            send(player, "You are summoning " + ChatColor.GREEN + "Krusk" + ChatColor.GRAY + ", one of the Undead Generals.");
            Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> ritual(drop, player.getName(), Bosses.KRUSK), 40);
        } else if (stack.equals(ItemsManager.EiryerasSpawn)
                || stack.equals(RPGFramework.itemManager.getItem("eiryeras:Spawn"))) {
            drop.setPickupDelay(60);
            send(player, "You are summoning " + ChatColor.GREEN + "Eiryeras" + ChatColor.GRAY + ", honored hunter of the Agloytan area.");
            Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> ritual(drop, player.getName(), Bosses.EIRYERAS), 40);
        } else if (drop.getItemStack().equals(ItemsManager.FeyrithSpawn)
                || stack.equals(RPGFramework.itemManager.getItem("feyrith:Spawn"))) {
            drop.setPickupDelay(60);
            send(player, "You are summoning " + ChatColor.BLUE + "Feyrith" + ChatColor.GRAY + ", an apprentice mage of the castle.");
            Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> ritual(drop, player.getName(), Bosses.FEYRITH), 40);
        }
    }

    public static void ritual(Item drop, String player, Bosses b) {
        Location location = drop.getLocation();
        assert location.getWorld() != null;
        SummoningParticles(Particle.LAVA, new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ()), 159);
        SummoningParticles(Particle.ENCHANTMENT_TABLE, new Location(location.getWorld(), location.getX(), location.getY() + 0.5, location.getZ()), 159);
        send(Bukkit.getServer().getConsoleSender(), Bosses.getFormattedName(b) + ChatColor.GRAY + "is being spawned at: " + ChatColor.GRAY + location + ChatColor.GRAY + " - " + player);
        switch(b) {
            case EIRYERAS: Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> MobManager.addMob(new EiryerasBoss(location)), 160); break;
            case FEYRITH: Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> MobManager.addMob(new FeyrithBoss(location)), 160); break;
            case KRUSK: Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> MobManager.addMob(new KruskBoss(location)), 160); break;
        }
        drop.remove();
        Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> location.getWorld().playSound(location, "entity.wither.spawn", 0.8f, 1), 160);
        new BukkitRunnable() { @Override public void run() { if(location.getWorld().getBlockAt(location).getType() == Material.FIRE) location.getWorld().getBlockAt(location).setType(Material.AIR); } }.runTaskLater(main.plugin, 150);
        Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> location.getWorld().strikeLightningEffect(location), 140);
        Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> location.getWorld().strikeLightningEffect(location), 120);
        Bukkit.getServer().getScheduler().runTaskLater(main.plugin, () -> location.getWorld().strikeLightningEffect(location), 100);
    }

    private static void SummoningParticles(Particle p, Location l, int delay) { for (int i = 1; i < delay; i++) Bukkit.getScheduler().runTaskLater(main.plugin, () -> Objects.requireNonNull(l.getWorld()).spawnParticle(p, l, 1), i); }
}
