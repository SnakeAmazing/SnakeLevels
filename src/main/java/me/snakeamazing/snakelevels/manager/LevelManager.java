package me.snakeamazing.snakelevels.manager;

import me.snakeamazing.snakelevels.event.PlayerLevelUpEvent;
import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import me.snakeamazing.snakelevels.player.LevelPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

public class LevelManager {

    private final HashMap<String, LevelPlayer> levelPlayers;

    private final YAMLFile data;
    private final YAMLFile config;
    private final YAMLFile messages;

    private final Random random = new Random();

    public LevelManager(FileMatcher fileMatcher) {
        this.levelPlayers = new HashMap<>();

        this.data = fileMatcher.getFile("data");
        this.config = fileMatcher.getFile("config");
        this.messages = fileMatcher.getFile("messages");

    }

    public void addPlayerForFirstTime(Player player) {
        LevelPlayer levelPlayer = new LevelPlayer(player.getName());
        setNewXpLevel(levelPlayer);

        String path = "data." + player.getName();

        data.set(path + ".level", levelPlayer.getLevel());
        data.set(path + ".xp", levelPlayer.getXp());
        data.set(path + ".xp-to-next-level", levelPlayer.getXpToNextLevel());
        data.set(path + ".multiplier", levelPlayer.getMultiplier());
        data.save();
    }

    public void addLevelPlayer(Player player) {
        if (!data.contains("data." + player.getName())) {
            return;
        }

        String path = "data." + player.getName();

        LevelPlayer levelPlayer = new LevelPlayer(
                player.getName(), data.getInt(path + ".level"), data.getInt(path + ".xp"),
                data.getInt(path + ".xp-to-next-level"),
                data.getDouble(path + ".multiplier"));

        levelPlayers.put(player.getName(), levelPlayer);

    }

    public void removeLevelPlayer(Player player) {
        LevelPlayer levelPlayer = levelPlayers.get(player.getName());
        String path = "data." + player.getName();

        data.set(path + ".level", levelPlayer.getLevel());
        data.set(path + ".xp", levelPlayer.getXp());
        data.set(path + ".xp-to-next-level", levelPlayer.getXpToNextLevel());
        data.set(path + ".multiplier", levelPlayer.getMultiplier());
        data.save();

        levelPlayers.remove(player.getName());
    }

    public void setNewXpLevel(LevelPlayer levelPlayer) {

        if (levelPlayer == null) {
            Bukkit.getLogger().log(Level.WARNING, "Tried to get information of a player that doesn't exist.");
            return;
        }

        int playerLevel = config.getInt("exp.level." + levelPlayer.getLevel());
        levelPlayer.setXpToNextLevel(playerLevel);
        levelPlayers.put(levelPlayer.getName(), levelPlayer);
    }

    public void updatePlayerLevel(Player player, double xp) {
        LevelPlayer levelPlayer = levelPlayers.get(player.getName());

        levelPlayer.addXp(xp);

        if (levelPlayer.getXp() >= levelPlayer.getXpToNextLevel()) {
            levelPlayer.setXp(levelPlayer.getXp() - levelPlayer.getXpToNextLevel());
            levelPlayer.addLevel(1);
            setNewXpLevel(levelPlayer);

            if (levelPlayer.getLevel() > config.getInt("settings.max-level")) {
                levelPlayer.removeLevel(1);
                player.sendMessage(messages.getString("messages.level-up.max-level"));
                return;
            }
            sendLevelUpMessage(player, levelPlayer.getLevel());
            Bukkit.getServer().getPluginManager().callEvent(new PlayerLevelUpEvent(player));
        }

        levelPlayers.put(player.getName(), levelPlayer);
    }

    public void setPlayerMultiplier(Player player, double multiplier) {
        levelPlayers.get(player.getName()).setMultiplier(multiplier);
    }

    public double getPlayerMultiplier(Player player) {
        return levelPlayers.get(player.getName()).getMultiplier();
    }

    public int getPlayerLevel(Player player) {
        return levelPlayers.get(player.getName()).getLevel();
    }

    public double getPlayerXp(Player player) {
        return levelPlayers.get(player.getName()).getXp();
    }

    public int getPlayerXpToNextLevel(Player player) {
        return levelPlayers.get(player.getName()).getXpToNextLevel();
    }

    public double getPlayerRemainingXp(Player player) {
        return levelPlayers.get(player.getName()).getRemainingXp();
    }

    public void setPlayerLevel(Player player, int level) {
        levelPlayers.get(player.getName()).setLevel(level);
    }

    private void sendLevelUpMessage(Player player, int level) {
        if (!messages.contains("messages.level-up.level-" + level)) {
            return;
        }

        for (String message : messages.getStringList("messages.level-up.level-" + level)) {
            player.sendMessage(message);
        }
    }
}
