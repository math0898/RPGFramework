package sugaku.rpg.framework.classes.implementations;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import io.github.math0898.rpgframework.Cooldown;
import sugaku.rpg.framework.classes.AbstractClass;
import sugaku.rpg.framework.players.PlayerManager;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.framework.classes.Class;

public class Berserker extends AbstractClass implements Class{

    public Berserker(RpgPlayer p) {
        super(p);
        setCooldowns(new Cooldown[]{new Cooldown(30), new Cooldown(60), new Cooldown(180)});
        PlayerManager.scaleRegen(p.getBukkitPlayer(), 0.8);
    }

    @Override
    public void damaged(EntityDamageEvent event) {
        event.setDamage(event.getDamage() - 2.0);
        if (event.getDamage() >= ((Player) event.getEntity()).getHealth() && getCooldowns()[2].getRemaining() >= 175) event.setCancelled(true);
    }

    @Override
    public void attack(EntityDamageByEntityEvent event) {

        Player attacker = (Player) event.getDamager();
        EntityEquipment equipment = attacker.getEquipment();

        if (equipment != null && isAxe(equipment)) event.setDamage(event.getDamage() + 2.0);
        else send("You aren't receiving your axe dmg bonus.");

        if (getCooldowns()[2].getRemaining() >= 175) attacker.setHealth(Math.min(attacker.getHealth() + (event.getDamage() * 2.0), attacker.getMaxHealth()));
    }

    @Override
    public void passive() { }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        EntityEquipment equipment = event.getPlayer().getEquipment();
        if (equipment == null) return;
        if (equipment.getItemInMainHand().getType() == Material.ROTTEN_FLESH) {
            if (middleLeather(equipment) ) {
                switch(event.getAction()) {
                    case RIGHT_CLICK_AIR: case RIGHT_CLICK_BLOCK: haste(); break;
                    case LEFT_CLICK_AIR: case LEFT_CLICK_BLOCK: rage(); break;
                }
            } else send("Use leather middle pieces to use abilities.");
        }
    }

    private void rage() {
        if(getCooldowns()[1].isComplete()) {

            send(ChatColor.GREEN + "You've used rage!");
            getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10*20, 1));
            getCooldowns()[1].restart();

        } else onCooldown(1);
    }

    private void haste() {
        if(getCooldowns()[0].isComplete()) {

            send(ChatColor.GREEN + "You've used haste!");
            getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 1));
            getCooldowns()[0].restart();

        } else onCooldown(0);
    }

    @Override
    public boolean onDeath() {
        if (getCooldowns()[2].isComplete()) {

            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Indomitable Spirit" + ChatColor.GREEN + "!");
            getPlayer().getBukkitPlayer().playSound(getPlayer().getBukkitPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);
            getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20, 2));
            getCooldowns()[2].restart();

            return false;
        }
        else onCooldown(2);
        return true;
    }

    private boolean isAxe(EntityEquipment equipment) {
        switch(equipment.getItemInMainHand().getType()){
            case WOODEN_AXE: case STONE_AXE: case IRON_AXE: case DIAMOND_AXE: case GOLDEN_AXE: case NETHERITE_AXE: return true;
            default: return false;
        }
    }

    private boolean middleLeather(EntityEquipment equipment) {
        if (equipment == null || equipment.getChestplate() == null || equipment.getLeggings() == null) return false;

        return (equipment.getChestplate().getType() == Material.LEATHER_CHESTPLATE && equipment.getLeggings().getType() == Material.LEATHER_LEGGINGS);
    }
}
