package me.snakeamazing.snakelevels.listeners;

import me.snakeamazing.snakelevels.event.PlayerLevelUpEvent;
import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import me.snakeamazing.snakelevels.manager.LevelManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLevelUpListener implements Listener {

    private final LevelManager levelManager;
    private final YAMLFile rewards;

    public PlayerLevelUpListener(LevelManager levelManager, FileMatcher matcher) {
        this.levelManager = levelManager;
        this.rewards = matcher.getFile("rewards");
    }

    @EventHandler
    public void onPlayerLevelUpListener(PlayerLevelUpEvent event) {
        Player player = event.getPlayer();

        String level = String.valueOf(levelManager.getPlayerLevel(player));

        if (!rewards.contains(level)) {
            return;
        }

        if (rewards.contains(level + ".commands")) {
            for (String command : rewards.getStringList(level + ".commands")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
            }
        }

    }
}
