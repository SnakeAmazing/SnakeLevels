package me.snakeamazing.snakelevels.listeners;

import me.snakeamazing.snakelevels.manager.LevelManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final LevelManager levelManager;

    public PlayerQuitListener(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        levelManager.removeLevelPlayer(event.getPlayer());
    }
}
