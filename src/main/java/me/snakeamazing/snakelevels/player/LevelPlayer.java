package me.snakeamazing.snakelevels.player;

public class LevelPlayer {

    private final String name;
    private int level;
    private double xp;
    private int xpToNextLevel;

    private double multiplier;


    public LevelPlayer(String name) {
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.multiplier = 1.0;
    }

    public LevelPlayer(String name, int level, double xp, int xpToNextLevel, double multiplier) {
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.xpToNextLevel = xpToNextLevel;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int amount) {
        this.level += amount;
    }

    public void removeLevel(int amount) {
        level -= amount;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public void addXp(double amount) {
        if (this.xp + amount <= 0) {
            this.xp = 0;
        } else {
            this.xp += amount;
        }
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public void setXpToNextLevel(int xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    public double getRemainingXp() {
        return xpToNextLevel - xp;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
