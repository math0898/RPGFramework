package sugaku.rpg.framework.players;

//import org.bukkit.entity.Player;

import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class Party {

    private final ArrayList<RpgPlayer> players = new ArrayList<>();

    public void addPlayer(RpgPlayer p) { players.add(p); }

    public void removePlayer(RpgPlayer p) { players.remove(p); }

    public ArrayList<RpgPlayer> getRpgPlayers() { return players; }

//    public ArrayList<Player> getBukkitPlayers() { return null; }

    public void sendAll(String message) { for (RpgPlayer p: players) p.getBukkitPlayer().sendMessage(message); }

    public void applyEffect(PotionEffect effect) {
        for (RpgPlayer p: players) p.getBukkitPlayer().addPotionEffect(effect);
    }
}
