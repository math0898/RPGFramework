package io.github.math0898.rpgframework.commands.stats;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import io.github.math0898.utils.StringUtils;
import io.github.math0898.utils.gui.AbstractGUI;
import io.github.math0898.utils.items.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;

import static io.github.math0898.rpgframework.RpgPlayer.*;

/**
 * The StatsGUI shows information about a player.
 *
 * @author Sugaku
 */
public class StatsGUI extends AbstractGUI {

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     */
    @Override
    public void openInventory (Player player) {
        openInventory(player, player.getName());
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     * @param params Any parameters to add to the inventory when to open.
     */
    @Override
    public void openInventory (Player player, String... params) {
        if (params.length < 1) return; // No player provided.
        openInventory(player, PlayerManager.getPlayer(params[0]));
    }

    /**
     * Opens this GUI to the given player.
     *
     * @param player The player to open the GUI to.
     * @param rpgPlayer The RpgPlayer to open stats on and about.
     */
    public void openInventory (Player player, RpgPlayer rpgPlayer) {
        if (rpgPlayer == null) return;
        Inventory inv = Bukkit.createInventory(player, 45, getTitle());
        ItemStack fill = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build();
        for (int i = 0; i < 45; i++)
            inv.setItem(i, fill);
        updateActive(inv, player, rpgPlayer);
        // todo: Boss kill statistics.
        player.openInventory(inv);
    }

    /**
     * Updates the active portion of this StatsGUI. This includes the player's live stat-line as well as talent point
     * selections.
     */
    private void updateActive (Inventory inv, Player viewer, RpgPlayer target) {
        inv.setItem(13, new ItemBuilder(Material.PLAYER_HEAD)
                .setOwningPlayer(target.getUuid())
                .setDisplayName(target.getPlayerRarity() + target.getName())
                .setLore(new String[] { // todo: Make these colors match item colors.
                        StringUtils.convertHexCodes("#F454DAHealth: " + (long) (target.getCurrentHealth() * 5.0) + " / " + (long) (target.getMaxHealth() * 5.0)),
                        StringUtils.convertHexCodes("#D93747Damage: " + (long) (target.getCurrentDamage() * 5.0)),
                        StringUtils.convertHexCodes("#CCCCCCClass: " + target.getCombatClass().getFormattedName()),
                        ChatColor.AQUA + "Current Level: " + target.getLevel() + " (" + target.getExperience() + ")",
                        StringUtils.convertHexCodes("#F2D951Gear Score: " + target.getGearScore())
                }).build());
        // todo: Ascending costs?
        if (viewer.getUniqueId() == target.getUuid()) {
            inv.setItem(29, new ItemBuilder(Material.IRON_CHESTPLATE).setDisplayName(StringUtils.convertHexCodes("#F454DATenacity"))
                    .setLore(new String[]{
                            StringUtils.convertHexCodes("#CCCCCC" + "Gain health per point spent on tenacity."),
                            StringUtils.convertHexCodes("#CCCCCC" + "Current Bonus:#F454DA +" + target.getHealthTalentPoints() * HEALTH_PER_POINT),
                            StringUtils.convertHexCodes("#CCCCCC" + "Available Points: " + ChatColor.DARK_AQUA + target.getPointsUnallocated())
                    }).build());
            inv.setItem(30, new ItemBuilder(Material.FEATHER).setDisplayName(ChatColor.AQUA + "Swiftness")
                    .setLore(new String[]{
                            StringUtils.convertHexCodes("#CCCCCC" + "Gain movement speed per point spent on swiftness."),
                            StringUtils.convertHexCodes("#CCCCCC" + "Current Bonus:" + ChatColor.AQUA + " +" + String.format("%.1f", target.getMovementSpeedTalentPoints() * MOVEMENT_SPEED_PER_POINT * 100)),
                            StringUtils.convertHexCodes("#CCCCCC" + "Spent Points: " + ChatColor.DARK_AQUA + target.getMovementSpeedTalentPoints() + " / " + MOVEMENT_SPEED_MAX_POINTS),
                            StringUtils.convertHexCodes("#CCCCCC" + "Available Points: " + ChatColor.DARK_AQUA + target.getPointsUnallocated())
                    }).build());
            inv.setItem(32, new ItemBuilder(Material.WOODEN_SWORD).setDisplayName(ChatColor.YELLOW + "Weak Point Targeting")
                    .setLore(new String[]{
                            StringUtils.convertHexCodes("#CCCCCC" + "Gain critical strike chance per point spent on"),
                            StringUtils.convertHexCodes("#CCCCCC" + "weak point targeting."),
                            StringUtils.convertHexCodes("#CCCCCC" + "Current Bonus: " + ChatColor.YELLOW + String.format("%.1f", (target.getCritChanceTalentPoints() * CRIT_CHANCE_PER_POINT) * 100) + "%"),
                            StringUtils.convertHexCodes("#CCCCCC" + "Spent Points: " + ChatColor.DARK_AQUA + target.getCritChanceTalentPoints() + " / " + CRIT_CHANCE_MAX_POINTS),
                            StringUtils.convertHexCodes("#CCCCCC" + "Available Points: " + ChatColor.DARK_AQUA + target.getPointsUnallocated())
                    }).build());
            inv.setItem(33, new ItemBuilder(Material.IRON_SWORD).setDisplayName(StringUtils.convertHexCodes("#D93747Power"))
                    .setLore(new String[]{
                            StringUtils.convertHexCodes("#CCCCCC" + "Gain base damage per point spent on power."),
                            StringUtils.convertHexCodes("#CCCCCC" + "Current Bonus:#D93747 +" + target.getDamageTalentPoints() * DAMAGE_PER_POINT),
                            StringUtils.convertHexCodes("#CCCCCC" + "Available Points: " + ChatColor.DARK_AQUA + target.getPointsUnallocated())
                    }).build());
            inv.setItem(40, new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Reset Points")
                    .setLore(new String[]{
                            StringUtils.convertHexCodes("#CCCCCC" + "You will gain " + ChatColor.DARK_AQUA + target.getLevel() + "#CCCCCC points.")
                    }).build());
        }
    }

    /**
     * Called whenever this GUI is clicked.
     *
     * @param event The inventory click event.
     */
    @Override
    public void onClick (InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getClickedInventory() == null) return;
        Inventory inv = event.getClickedInventory();
        if (inv.getHolder() instanceof Player player) {
            RpgPlayer rpgPlayer = PlayerManager.getPlayer(player.getUniqueId());
            if (rpgPlayer == null) return;
            switch (event.getSlot()) { // todo: These should be inactive if player is not the player who's stats are showing.
                case 29 -> rpgPlayer.allocatePoint("health");
                case 30 -> rpgPlayer.allocatePoint("movement_speed");
                case 32 -> rpgPlayer.allocatePoint("critical_chance");
                case 33 -> rpgPlayer.allocatePoint("damage");
                case 40 -> rpgPlayer.resetPoints();
            }
            // todo: This might cause issues when viewing another player.
            updateActive(inv, player, rpgPlayer);
        }
    }

    /**
     * Called whenever this GUI is closed.
     *
     * @param event The inventory close event.
     */
    @Override
    public void onClose (InventoryCloseEvent event) {

    }

    /**
     * Gets the title of this GUI. Used by the GUIManager to route InventoryClickEvents.
     *
     * @return The title of this GUI.
     */
    @Override
    public String getTitle () {
        return "Player Stats";
    }
}
