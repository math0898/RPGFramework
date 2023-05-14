package io.github.math0898.rpgframework.collections;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;

/**
 * The CollectionListener listens to events that could add to a player's collection. We catalog these events until they
 * can be handled async by the CollectionManager.
 *
 * @author Sugaku
 */
public class CollectionListener implements Listener {

    /**
     * Called whenever a block is broken.
     *
     * @param event The block break event to pass onto the CollectionManager.
     */
    @EventHandler
    public void onBlockBreak (BlockDropItemEvent event) {
        if (event.getItems().size() == 0) return;
        for (Item i : event.getItems()) CollectionManager.getInstance().addEvent(new CandidateCollectionEvent(i.getItemStack().getType(), i.getItemStack().getAmount(),  event.getPlayer().getUniqueId()));
    }
}
