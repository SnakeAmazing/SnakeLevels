package me.snakeamazing.snakelevels.listeners;

import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import me.snakeamazing.snakelevels.manager.LevelManager;
import me.snakeamazing.snakelevels.player.LevelPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final LevelManager levelManager;
    private final YAMLFile data;
    private final YAMLFile rewards;

    public PlayerJoinListener(LevelManager levelManager, FileMatcher matcher) {
        this.levelManager = levelManager;

        this.data = matcher.getFile("data");
        this.rewards = matcher.getFile("rewards");
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!data.contains("data." + player.getName())) {
            levelManager.addPlayerForFirstTime(player);
        }

        levelManager.addLevelPlayer(player);


        for (String string : rewards.getStringList("new-rewards")) {
            if (levelManager.getPlayerLevel(player) < Integer.parseInt(string)) {
                return;
            }

            for (String reward : rewards.getStringList("new-wards." + string)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
            }
        }
    }
}
