package sugaku.rpg.framework.classes.implementations;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sugaku.rpg.framework.Cooldown;
import sugaku.rpg.framework.classes.AbstractClass;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.framework.classes.Class;

import java.util.Random;

public class Assassin extends AbstractClass implements Class {

    public Assassin(RpgPlayer p) {
        super(p);
        setCooldowns(new Cooldown[]{new Cooldown(30), new Cooldown(60), new Cooldown(300)});
    }

    @Override
    public void damaged(EntityDamageEvent event) {
        double roll = new Random().nextDouble();
        if (getCooldowns()[2].getRemaining() >= 290) event.setCancelled(true);
        else if (getCooldowns()[0].getRemaining() >= 20) event.setCancelled(true);
        else if (roll < 0.10) event.setCancelled(true);
    }

    @Override
    public void attack(EntityDamageByEntityEvent event) {

        LivingEntity target = (LivingEntity) event.getEntity();
        if (target instanceof Player) event.setDamage(event.getDamage() + 1.0);
        else event.setDamage(event.getDamage() + 2.0);

        if (getCooldowns()[1].getRemaining() >= 50) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 0));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*20, 0));
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10*20, 0));
        }
    }

    @Override
    public void passive() {
        EntityEquipment equipment = getPlayer().getBukkitPlayer().getEquipment();
        if (equipment == null) return;
        if (checkLeather(equipment)) getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 21*20, 0));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {

        if (event.getPlayer().getEquipment() == null) return;
        ItemStack item = event.getPlayer().getEquipment().getItemInMainHand();

        if (item.getType() == Material.GHAST_TEAR) {
            if (checkLeather(event.getPlayer().getEquipment())) {
                switch(event.getAction()){
                    case RIGHT_CLICK_AIR: case RIGHT_CLICK_BLOCK: invisibility(); break;
                    case LEFT_CLICK_AIR: case LEFT_CLICK_BLOCK: poisonedBlade(); break;
                }
            } else send("Please wear full leather armor to use abilities.");
        }
    }

    private void invisibility() {
        if (getCooldowns()[0].isComplete()) {

            send(ChatColor.GREEN + "You've used invisibility!");

            getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10*20, 0));

            getCooldowns()[0].restart();

        } else onCooldown(0);
    }

    private void poisonedBlade() {
        if (getCooldowns()[1].isComplete()) {

            send(ChatColor.GREEN + "You've used poisoned blade!");

            getCooldowns()[1].restart();

        } else onCooldown(1);
    }

    @Override
    public boolean onDeath() {
        if (getCooldowns()[2].isComplete()) {

            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Heroic Dodge" + ChatColor.GREEN + "!");

            getPlayer().getBukkitPlayer().playSound(getPlayer().getBukkitPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);

            getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 1));

            getCooldowns()[2].restart();

            return false;
        } else onCooldown(2);
        return true;
    }

    private boolean checkLeather(EntityEquipment equipment) {

        if (equipment.getHelmet() == null || equipment.getChestplate() == null || equipment.getLeggings() == null || equipment.getBoots() == null) return false;

        return equipment.getHelmet().getType() == Material.LEATHER_HELMET && equipment.getChestplate().getType() == Material.LEATHER_CHESTPLATE &&
                equipment.getLeggings().getType() == Material.LEATHER_LEGGINGS && equipment.getBoots().getType() == Material.LEATHER_BOOTS;
    }
}
