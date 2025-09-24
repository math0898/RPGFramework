package io.github.math0898.studio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * An RpgItem represents all the data that constructs one in the game.
 * // todo: Does this need to be extracted to some kind of 'core' or 'libs' sub-project?
 * @author Sugaku
 */
@Getter @Setter
public class RpgItem {

    /**
     * The material for this item.
     */
    private String material = "IRON_AXE";

    /**
     * The display name for this item.
     */
    private String name = "Krusk's Axe";

    /**
     * The rarity of this item.
     */
    private Rarities rarities = Rarities.UNCOMMON;

    /**
     * The lore description for this item.
     */
    private List<String> lore = new ArrayList<>();

    /**
     * The damage for this item.
     */
    private int damage = 60;

    /**
     * The attack speed for this item.
     */
    private double attackSpeed = -3;

    /**
     * The health for this item.
     */
    private int health = 7;

    /**
     * The armor for this item.
     */
    private int armor = 10;

    /**
     * The armor toughness for this item.
     */
    private int toughness = 3;
}
