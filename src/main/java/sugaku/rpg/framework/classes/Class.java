package sugaku.rpg.framework.classes;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import sugaku.rpg.framework.players.RpgPlayer;

@Deprecated
public interface Class {

    RpgPlayer player = null;
    
    void damaged(EntityDamageEvent event);

    void attack(EntityDamageByEntityEvent event);
    
    void passive();

    void onInteract(PlayerInteractEvent event);

    boolean onDeath();
}
