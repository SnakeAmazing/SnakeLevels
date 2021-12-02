package me.snakeamazing.snakelevels.player;

public class LevelPlayer {

    private final String name;
    private int level;
    private int xp;
    private int xpToNextLevel;

    private double multiplier;


    public LevelPlayer(String name) {
        this.name = name;
        this.level = 1;
        this.xp = 0;
        this.multiplier = 1;
    }

    public LevelPlayer(String name, int level, int xp, int xpToNextLevel, double multiplier) {
        this.name = name;
        this.level = level;
        this.xp = xp;
        this.xpToNextLevel = xpToNextLevel;
        this.multiplier = 1;
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

    public void addLevel(double amount) {
        this.level += level;
    }

    public void removeLevel(int amount) {
        level -= amount;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXp(double amount) {
        this.xp += amount;
    }

    public int getXpToNextLevel() {
        return xpToNextLevel;
    }

    public void setXpToNextLevel(int xpToNextLevel) {
        this.xpToNextLevel = xpToNextLevel;
    }

    public int getRemainingXp() {
        return xpToNextLevel - xp;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
}
