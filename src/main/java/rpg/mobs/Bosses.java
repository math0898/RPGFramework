package sugaku.rpg.mobs;

import sugaku.rpg.mobs.teir1.eiryeras.EiryerasBoss;
import sugaku.rpg.mobs.teir1.feyrith.FeyrithBoss;
import sugaku.rpg.mobs.teir1.krusk.KruskBoss;

public enum Bosses {

    /**
     * All of the bosses currently in the game.
     */
    EIRYERAS, KRUSK, FEYRITH;

    /**
     * Returns the formatted name of the given boss.
     *
     * @param b The boss who's formatted name is being determined.
     * @return The string formatted name.
     */
    public static String getFormattedName(Bosses b) { //TODO: Implement the actual color formatting
        switch (b) {
            case EIRYERAS: return EiryerasBoss.getName();
            case KRUSK: return KruskBoss.getName();
            case FEYRITH: return FeyrithBoss.getName();
        }
        return "";
    }
}
