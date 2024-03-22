package sugaku.rpg.framework.players;

import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class Party {

    private final ArrayList<RpgPlayer> players = new ArrayList<>();

    public ArrayList<RpgPlayer> getRpgPlayers() { return players; }

    public void applyEffect(PotionEffect effect) {
        for (RpgPlayer p: players) p.getBukkitPlayer().addPotionEffect(effect);
    }
}
