package sugaku.rpg.framework;

public class Cooldown {

    private final float duration;

    private long startTime;

    private boolean complete = false;

    public Cooldown(float duration) {
        startTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public float getRemaining() { return duration - Math.floorDiv(System.currentTimeMillis() - startTime, 1000); }

    public boolean isComplete() { return ((getRemaining() < 0) || complete); }

    public void restart() {
        startTime = System.currentTimeMillis();
        complete = false;
    }

    public void setComplete() { complete = true; }
}
