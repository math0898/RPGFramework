package sugaku.rpg.framework.classes.implementations;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import io.github.math0898.rpgframework.Cooldown;
import sugaku.rpg.framework.classes.AbstractClass;
import sugaku.rpg.framework.classes.Class;
import sugaku.rpg.framework.classes.Classes;
import sugaku.rpg.framework.players.PlayerManager;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.main;

import java.util.Objects;

public class Pyromancer extends AbstractClass implements Class, Listener {

    public Pyromancer(RpgPlayer p) {
        super(p);
        setCooldowns(new Cooldown[]{new Cooldown(3), new Cooldown(5), new Cooldown(10), new Cooldown(15), new Cooldown(180)});
    }

    public Pyromancer() { super(); }

    @Override
    public void damaged(EntityDamageEvent event) {
        getPlayer().getBukkitPlayer().setFireTicks(0);
    }

    @Override
    public void attack(EntityDamageByEntityEvent event) {
        if (getCooldowns()[4].getRemaining() >= 165) {
            event.setDamage(event.getDamage() + 2.5);
            event.getEntity().setFireTicks(5);
        }
    }

    @Override
    public void passive() { }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        EntityEquipment equipment = event.getPlayer().getEquipment();
        if (equipment == null) return;

        if (equipment.getItemInMainHand().getType() == Material.BLAZE_POWDER) {
            switch(event.getAction()){
                case RIGHT_CLICK_AIR: case RIGHT_CLICK_BLOCK: smallFireball(); break;
                case LEFT_CLICK_AIR: case LEFT_CLICK_BLOCK: bigFireball(); break;
            }
        } else if (equipment.getItemInMainHand().getType() == Material.BLAZE_ROD) {
            switch(event.getAction()){
                case RIGHT_CLICK_AIR: case RIGHT_CLICK_BLOCK: smallBarrageFireball(); break;
                case LEFT_CLICK_AIR: case LEFT_CLICK_BLOCK: bigBarrageFireball(); break;
            }
        }
    }

    private void smallFireball() {
        if (getCooldowns()[0].isComplete()) {
            send(ChatColor.GREEN + "You've fired a fireball!");
            getPlayer().getBukkitPlayer().launchProjectile(SmallFireball.class);
            getCooldowns()[0].restart();
        } else onCooldown(0);
    }

    private void bigFireball(){
        if (getCooldowns()[1].isComplete()) {
            send(ChatColor.GREEN + "You've fired a fireball!");
            getPlayer().getBukkitPlayer().launchProjectile(LargeFireball.class);
            getCooldowns()[1].restart();
        } else onCooldown(1);
    }

    private void smallBarrageFireball() {
        if (getCooldowns()[2].isComplete()) {
            send(ChatColor.GREEN + "You've fired a fireball barrage!");
            getPlayer().getBukkitPlayer().launchProjectile(SmallFireball.class);
            Bukkit.getScheduler().runTaskLater(main.plugin, () -> getPlayer().getBukkitPlayer().launchProjectile(SmallFireball.class), 5);
            Bukkit.getScheduler().runTaskLater(main.plugin, () -> getPlayer().getBukkitPlayer().launchProjectile(SmallFireball.class), 10);
            getCooldowns()[2].restart();
        } else onCooldown(2);
    }

    private void bigBarrageFireball() {
        if (getCooldowns()[3].isComplete()) {
            send(ChatColor.GREEN + "You've fired a fireball barrage!");
            getPlayer().getBukkitPlayer().launchProjectile(LargeFireball.class);
            Bukkit.getScheduler().runTaskLater(main.plugin, () -> getPlayer().getBukkitPlayer().launchProjectile(LargeFireball.class), 5);
            Bukkit.getScheduler().runTaskLater(main.plugin, () -> getPlayer().getBukkitPlayer().launchProjectile(LargeFireball.class), 10);
            getCooldowns()[3].restart();
        } else onCooldown(3);
    }

    @Override
    public boolean onDeath() {
        if(getCooldowns()[4].isComplete()) {
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Phoenix Renewal" + ChatColor.GREEN + "!");

            getPlayer().getBukkitPlayer().playSound(getPlayer().getBukkitPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 15*20, 3));

            getCooldowns()[4].restart();
            return false;
        } else onCooldown(4);
        return true;
    }

    /**
     * Event handler for when an entity is damaged. For now used to partially nullify fire based damage for the player.
     * Also used to increase the damage enemies take from fireballs. //TODO: Add metadata to the fireballs and check for them in an event.
     *
     * @param event The entity damaged event.
     */
    @EventHandler
    public static void onEntityDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            assert PlayerManager.getPlayer(player.getUniqueId()) != null;
            if (Objects.requireNonNull(PlayerManager.getPlayer(player.getUniqueId())).getCombatClass() == Classes.PYROMANCER) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                        || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    event.setDamage(event.getDamage() / 2.0);
                    player.setFireTicks(0);
                }
            }
        }
    }
}
