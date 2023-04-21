package io.github.math0898.rpgframework.ui;

/**
 * The UIManager is used to aid in wrapping and managing inventories.
 *
 * @author Sugaku
 */
public class UIManager {

    /**
     * The main instance of the UIManager to be used by the whole program.
     */
    private static final UIManager uiManager = new UIManager();

    /**
     * Accessor method for the single instance of InventoryManager.
     *
     * @return The single instance of InventoryManager to be used by the whole program.
     */
    public static UIManager getInstance () {
        return uiManager;
    }
}
