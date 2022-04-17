package sugaku.rpg.framework.classes;

import sugaku.rpg.framework.Cooldown;
import sugaku.rpg.framework.players.RpgPlayer;
import sugaku.rpg.commands.AbstractCommand;

public class AbstractClass {

    private final RpgPlayer player;

    private Cooldown[] cooldowns;

    public AbstractClass(RpgPlayer p) { player = p; }

    public AbstractClass() { player = null; }

    public RpgPlayer getPlayer() { return player; }

    public void setCooldowns(Cooldown[] cooldowns) { this.cooldowns = cooldowns; }

    public Cooldown[] getCooldowns() { return cooldowns; }

    public void onCooldown(int i) {
        if (player == null) return;
        AbstractCommand.send(player.getBukkitPlayer(), "That ability is on cooldown for another " +  getCooldowns()[i].getRemaining() + "s.");
    }

    public void send(String message) {
        if (player == null) return;
        AbstractCommand.send(player.getBukkitPlayer(), message);
    }
}
