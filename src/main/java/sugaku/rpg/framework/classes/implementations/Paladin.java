package sugaku.rpg.framework.classes.implementations;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sugaku.rpg.commands.AbstractCommand;
import sugaku.rpg.framework.Cooldown;
import sugaku.rpg.framework.classes.AbstractClass;
import sugaku.rpg.framework.classes.Class;
import sugaku.rpg.framework.players.RpgPlayer;

public class Paladin extends AbstractClass implements Class {

    public Paladin(RpgPlayer p) {
        super(p);
        setCooldowns(new Cooldown[]{new Cooldown(30), new Cooldown(60), new Cooldown(300)});
    }

    @Override
    public void damaged(EntityDamageEvent event) {
        if (getCooldowns()[1].getRemaining() >= 55) event.setCancelled(true);
    }

    @Override
    public void attack(EntityDamageByEntityEvent event) {

    }

    @Override
    public void passive() {
//        if (goldenArmor(getPlayer().getBukkitPlayer().getEquipment())) getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 31*20, 2));
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        EntityEquipment equipment = event.getPlayer().getEquipment();
        if (equipment == null) return;
        if (equipment.getItemInMainHand().getType() == Material.GOLDEN_SHOVEL) {
            if (goldenArmor(equipment) ) {
                switch(event.getAction()) {
                    case RIGHT_CLICK_AIR: case RIGHT_CLICK_BLOCK: purifyMain(); break;
                    case LEFT_CLICK_AIR: case LEFT_CLICK_BLOCK: mend(); break;
                }
            } else send("Use golden armor to use abilities.");
        }
    }

    private void purifyMain() {
        if(getCooldowns()[1].isComplete()) {
            send(ChatColor.GREEN + "You've used purify!");

            if (getPlayer().getParty() != null) {
                for(RpgPlayer p: getPlayer().getParty().getRpgPlayers()) {
                    if (!p.getName().equals(getPlayer().getName())) AbstractCommand.send(p.getBukkitPlayer(), getPlayer().getName() + " has used purify!");
                    purify(p);
                }
            }
            else purify(getPlayer());
            getCooldowns()[1].restart();
        } else onCooldown(1);
    }

    private void purify(RpgPlayer p) {
        p.getBukkitPlayer().setFireTicks(0);
        for (PotionEffect e: p.getBukkitPlayer().getActivePotionEffects()) p.getBukkitPlayer().removePotionEffect(e.getType());
        p.getBukkitPlayer().setHealth(Math.min(p.getBukkitPlayer().getHealth() + 5.0, p.getBukkitPlayer().getMaxHealth()));
    }

    private void mend() {
        if(getCooldowns()[0].isComplete()) {
            send(ChatColor.GREEN + "You've used mend!");

            PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, 15*20, 2);

            if (getPlayer().getParty() != null) {
                getPlayer().getParty().applyEffect(effect);
                for(RpgPlayer p: getPlayer().getParty().getRpgPlayers()) if (!p.getName().equals(getPlayer().getName())) AbstractCommand.send(p.getBukkitPlayer(), getPlayer().getName() + " has used mend!");
            }
            else getPlayer().getBukkitPlayer().addPotionEffect(effect);
            getCooldowns()[0].restart();
        } else onCooldown(0);
    }

    @Override
    public boolean onDeath() {
        if (getCooldowns()[2].isComplete()) {

            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "Protection of the Healer" + ChatColor.GREEN + "!");
            getPlayer().getBukkitPlayer().playSound(getPlayer().getBukkitPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);

            if (getPlayer().getParty() != null) {
                getPlayer().getParty().applyEffect(new PotionEffect(PotionEffectType.REGENERATION,10*20,4));
                getPlayer().getParty().applyEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST,10*20,1));
                for(RpgPlayer p: getPlayer().getParty().getRpgPlayers()) {
                    if (!p.getName().equals(getPlayer().getName())) AbstractCommand.send(p.getBukkitPlayer(), getPlayer().getName() + " has used Protection of the Healer!");
                    p.getBukkitPlayer().setHealth(Math.min(p.getBukkitPlayer().getHealth() + 2.0, p.getBukkitPlayer().getMaxHealth()));
                }

            }
            else {
                getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,10*20,4));
                getPlayer().getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST,10*20,1));
                getPlayer().getBukkitPlayer().setHealth(Math.min(getPlayer().getBukkitPlayer().getHealth() + 2.0, getPlayer().getBukkitPlayer().getMaxHealth()));
            }

            getCooldowns()[2].restart();
            return false;
        } else onCooldown(2);
        return true;
    }

    private boolean goldenArmor(EntityEquipment equipment) {

        if (equipment == null) return false;
        if (equipment.getHelmet() == null || equipment.getChestplate() == null || equipment.getLeggings() == null || equipment.getBoots() == null) return false;

        return (equipment.getHelmet().getType() == Material.GOLDEN_HELMET && equipment.getChestplate().getType() == Material.GOLDEN_CHESTPLATE
        && equipment.getLeggings().getType() == Material.GOLDEN_LEGGINGS && equipment.getBoots().getType() == Material.GOLDEN_BOOTS);
    }
}
