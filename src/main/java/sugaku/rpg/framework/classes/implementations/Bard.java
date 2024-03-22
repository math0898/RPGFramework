package sugaku.rpg.framework.classes.implementations;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sugaku.rpg.commands.AbstractCommand;
import io.github.math0898.rpgframework.Cooldown;
import sugaku.rpg.framework.classes.AbstractClass;
import sugaku.rpg.framework.classes.Class;
import sugaku.rpg.framework.players.RpgPlayer;

import static sugaku.rpg.main.brackets;

public class Bard extends AbstractClass implements Class {

    private BardBuffs currentBuff = BardBuffs.REGENERATION;

    enum BardBuffs{ REGENERATION, SWIFTNESS, STRENGTH }

    public Bard(RpgPlayer player) {
        super(player);
        setCooldowns(new Cooldown[]{new Cooldown(30), new Cooldown(300)});
    }

    @Override
    public void damaged(EntityDamageEvent event) { }

    @Override
    public void attack(EntityDamageByEntityEvent event) { }

    @Override
    public void passive() { }

    @Override
    public void onInteract(PlayerInteractEvent event) {

        if (event.getPlayer().getEquipment() == null) return;
        ItemStack item = event.getPlayer().getEquipment().getItemInMainHand();

        if (item.getType() == Material.NOTE_BLOCK) {
            switch (event.getAction()) {
                case LEFT_CLICK_AIR: case LEFT_CLICK_BLOCK: bardLeftClick(); break;
                case RIGHT_CLICK_BLOCK:
                    if(!event.getPlayer().isSneaking()) { event.setCancelled(true); send("Shift + right click if you are trying to place that block."); }
                    else return;
                case RIGHT_CLICK_AIR: bardRightClick();
            }
        }
    }

    @Override
    public boolean onDeath() {
        if(getCooldowns()[1].isComplete()) {
            send(ChatColor.GREEN + "You've used " + ChatColor.GOLD + "A Life of Music" + ChatColor.GREEN + "!");
            RpgPlayer rpg = getPlayer();
            Player player = rpg.getBukkitPlayer();
            player.playSound(player, Sound.ITEM_TOTEM_USE, 0.8f, 1.0f);

            rpg.heal(10.0); //Instant heal

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10*20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10*20, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10*20, 1));

            getCooldowns()[1].restart();

            return false;
        } else onCooldown(1);
        return true;
    }

    private void bardLeftClick() {
        if(getCooldowns()[0].isComplete()) {
            send(ChatColor.GREEN + "You've used hym!");

            PotionEffect effect = new PotionEffect(getEffect(), 45*20, 0);

            if (getPlayer().getParty() != null) {
                getPlayer().getParty().applyEffect(effect);
                for(RpgPlayer p: getPlayer().getParty().getRpgPlayers()) if (!p.getName().equals(getPlayer().getName())) AbstractCommand.send(p.getBukkitPlayer(), getPlayer().getName() + " has used hym!");
            }
            else getPlayer().getBukkitPlayer().addPotionEffect(effect);
            getCooldowns()[0].restart();
        } else onCooldown(0);
    }

    private void bardRightClick() {
        switch(currentBuff) {
            case REGENERATION: send(brackets(ChatColor.AQUA + "Swiftness")); currentBuff = BardBuffs.SWIFTNESS; break;
            case SWIFTNESS: send(brackets(ChatColor.RED + "Strength")); currentBuff = BardBuffs.STRENGTH; break;
            case STRENGTH: send(brackets(ChatColor.LIGHT_PURPLE + "Regeneration")); currentBuff = BardBuffs.REGENERATION; break;
        }
    }

    private PotionEffectType getEffect() {
        switch (currentBuff) {
            case REGENERATION: return PotionEffectType.REGENERATION;
            case STRENGTH: return PotionEffectType.INCREASE_DAMAGE;
            case SWIFTNESS: return PotionEffectType.SPEED;
        }
        return PotionEffectType.ABSORPTION;
    }
}
