package sugaku.rpg.framework.classes.implementations;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import sugaku.rpg.framework.classes.AbstractClass;
import sugaku.rpg.framework.classes.Class;
import sugaku.rpg.framework.players.RpgPlayer;

public class None extends AbstractClass implements Class {

    public None(RpgPlayer player) { super(player); }

    @Override
    public void damaged(EntityDamageEvent event) { }

    @Override
    public void attack(EntityDamageByEntityEvent event) { }

    @Override
    public void passive() { }

    @Override
    public void onInteract(PlayerInteractEvent event) { }

    @Override
    public boolean onDeath() { return false; }
}
