package me.snakeamazing.snakelevels.listeners;

import me.snakeamazing.snakelevels.manager.LevelManager;
import me.snakeamazing.snakelevels.player.LevelPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final LevelManager levelManager;

    public PlayerJoinListener(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            levelManager.addPlayerForFirstTime(player);
        }

        levelManager.addLevelPlayer(player);

    }
}
