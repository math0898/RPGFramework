package io.github.math0898.rpgframework.ui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 /**
 * A UIElement is a single element of a greater UI that has a callback tree.
 *
 * @author Sugaku
 */
public abstract class UIElement {

    /**
     * The ItemStack that represents this UIElement.
     */
    private final ItemStack item;

    /**
     * The internal protected constructor for implementing classes to utilize.
     *
     * @param item The item that will represent this element.
     */
    protected UIElement (ItemStack item) {
        this.item = item;
    }

    /**
     * Accessor method for the ItemStack representing this element.
     *
     * @return The ItemStack that represents this element.
     */
    public ItemStack getItem () {
        return item;
    }

    /**
     * A method to be called whenever this UIElement is clicked.
     *
     * @param event The event that occurred when this element was clicked.
     */
    public abstract void onClick (InventoryClickEvent event);
}
