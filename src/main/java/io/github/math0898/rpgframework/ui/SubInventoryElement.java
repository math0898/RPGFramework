package io.github.math0898.rpgframework.ui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * An element of a UI which opens another inventory.
 *
 * @author Sugaku
 */
public class SubInventoryElement extends UIElement {

    /**
     * The UI which will be displayed when this element is interacted with.
     */
    private final UI toOpen;

    /**
     * Creates a new UIElement which will open the given inventory when clicked.
     *
     * @param item The item that will represent this UIElement.
     * @param ui The UI to open when this item is clicked.
     */
    public SubInventoryElement (ItemStack item, UI ui) {
        super(item);
        this.toOpen = ui;
    }

    /**
     * A method to be called whenever this UIElement is clicked.
     *
     * @param event The event that occurred when this element was clicked.
     */
    @Override
    public void onClick (InventoryClickEvent event) {
        toOpen.open(event.getView());
    }
}
