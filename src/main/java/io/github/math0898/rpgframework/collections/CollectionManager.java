package io.github.math0898.rpgframework.collections;

import io.github.math0898.rpgframework.ItemCollection;
import io.github.math0898.rpgframework.UserData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import sugaku.rpg.framework.players.PlayerManager;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static io.github.math0898.rpgframework.main.plugin;

/**
 * The CollectionManager manages collections related events and adds them to UserData async.
 *
 * @author Sugaku
 */
public class CollectionManager {

    /**
     * A stack of CandidateCollectionEvents that need to be handled.
     */
    private final Stack<CandidateCollectionEvent> events = new Stack<>();

    /**
     * A single instance of CollectionManager that should be used across the plugin.
     */
    private static final CollectionManager collectionManager = new CollectionManager();

    /**
     * The materials that collections are on for.
     */
    private static final List<Material> ALLOWED_MATERIALS = Arrays.asList(Material.CARROT, Material.DIAMOND, Material.RAW_IRON, Material.RAW_COPPER, Material.RAW_GOLD, Material.COAL, Material.WHEAT, Material.POTATO, Material.COCOA, Material.COBBLED_DEEPSLATE, Material.COBBLESTONE, Material.BIRCH_LOG, Material.DARK_OAK_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.LAPIS_LAZULI);

    /**
     * Accessor method for the singleton instance of the CollectionManager.
     *
     * @return The singleton instance of the CollectionManager.
     */
    public static CollectionManager getInstance () {
        return collectionManager;
    }

    /**
     * Adds an event to the queue of events.
     *
     * @param event The event to add to the events.
     */
    public void addEvent (CandidateCollectionEvent event) {
        this.events.add(event);
    }

    /**
     * Runs the CollectionManager.
     */
    public void run () {
        while (!events.empty()) {
            CandidateCollectionEvent e = events.pop();
            if (!ALLOWED_MATERIALS.contains(e.type())) continue;
            UserData data = PlayerManager.getUserData(e.player());
            if (data == null) continue;
            String name = e.type().toString();
            if (data.getCollection(name) == null) data.addCollection(name, new ItemCollection(e.amount()));
            else data.getCollection(name).add(e.amount());
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::run, 15 * 20);
    }
}
