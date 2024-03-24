package sugaku.rpg.framework.classes;

import io.github.math0898.rpgframework.Cooldown;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.commands.AbstractCommand;

@Deprecated
public class AbstractClass {

    private final RpgPlayer player;

    private Cooldown[] cooldowns;

    public AbstractClass(RpgPlayer p) { player = p; }

    protected AbstractClass() { player = null; }

    protected RpgPlayer getPlayer() { return player; }

    protected void setCooldowns(Cooldown[] cooldowns) { this.cooldowns = cooldowns; }

    protected Cooldown[] getCooldowns() { return cooldowns; }

    protected void onCooldown(int i) {
        if (player == null) return;
        AbstractCommand.send(player.getBukkitPlayer(), "That ability is on cooldown for another " +  getCooldowns()[i].getRemaining() + "s.");
    }

    protected void send(String message) {
        if (player == null) return;
        AbstractCommand.send(player.getBukkitPlayer(), message);
    }
}
