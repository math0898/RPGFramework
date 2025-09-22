package io.github.math0898.rpgframework.enemies.abilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.Random;

public class UndeadAura extends Ability {

    @EventHandler
    public void onAttacked (EntityDamageByEntityEvent event) {

        if (event.getEntity().getType() == EntityType.ZOMBIE && event.getEntity().isCustomNameVisible() && Objects.requireNonNull(event.getEntity().getCustomName()).contains("Krusk, Undead General")) {

            if (event.getDamager() instanceof Player player) {
                Random r = new Random();
                if (r.nextDouble() < 0.20) {
                    player.sendMessage(ChatColor.GREEN + "Krusk's " + ChatColor.GRAY + "undead aura weakens your attacks.");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0));
                }
            }
        }
    }
}
