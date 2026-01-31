package io.github.math0898.rpgframework.items.implementations;

import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static io.github.math0898.rpgframework.RPGFramework.itemManager;

/**
 * When right-clicked the WrathOfFeyrith strikes all nearby enemies with lightning.
 *
 * @author Sugaku
 */ // todo: Refactor to a single Interact event that then filters down to specific materials, then specific items.
public class WrathOfFeyrith implements Listener {

    /**
     * The last time in Millis that players have used this item.
     */
    private final Map<UUID, Long> lastUsed = new HashMap<>();

    /**
     * Called whenever a player interacts with the world.
     *
     * @param event The player interact event.
     */
    @EventHandler
    public void onInteract (PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        ItemStack item = event.getItem();
        if (item == null) return;
        if (!item.getType().equals(Material.PRISMARINE_SHARD)) return; // todo: Make Copper Ingot
        if (item.equals(itemManager.getItem("feyrith:WrathOfFeyrith")))
            itemUse(event.getPlayer());
    }

    /**
     * Uses this item on this player.
     *
     * @param player The player to use the event.
     */
    public void itemUse (Player player) {
        Long lng = lastUsed.get(player.getUniqueId());
        if (lng != null)
            if (System.currentTimeMillis() - lng < 60000) {
                player.sendMessage(ChatColor.GRAY + "That is on cooldown for another: " + (60000 + lng - System.currentTimeMillis()) / 1000L + "s.");
                return;
            }
        lastUsed.put(player.getUniqueId(), System.currentTimeMillis());
        final UUID uuid = player.getUniqueId();
        RpgPlayer rpg = sugaku.rpg.framework.players.PlayerManager.getPlayer(uuid);
        if (rpg == null) return;
        rpg.nearbyEnemyCasterTargets(8.0).forEach((e) -> {
            e.getWorld().strikeLightningEffect(e.getLocation());
            e.damage(100.0 / 5.0, player);
        });
    }
}
