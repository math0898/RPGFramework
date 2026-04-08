package io.github.math0898.rpgframework.hooks;

/**
 * The HookManager handles hooking into other plugins to provide essential and optional addons.
 *
 * @author Sugaku
 */
public class HookManager {

    /**
     * The active HookManager instance.
     */
    private static HookManager instance;

    /**
     * Creates a new HookManager.
     */
    private HookManager () {
        // Optional hooks are initialized lazily by their own feature entry points.
    }

    /**
     * An accessor method for the active HookManager instance.
     *
     * @return The active HookManager instance.
     */
    public static HookManager getInstance () {
        if (instance == null) instance = new HookManager();
        return instance;
    }
}
