package sugaku.rpg.framework.menus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface Menu {

    void onClick(int clicked, Player player);

    void build(Inventory inv, Player p);
}
