package me.snakeamazing.snakelevels.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.snakeamazing.snakelevels.manager.LevelManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final LevelManager levelManager;

    public PlaceholderAPIHook(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "snakelevels";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SnakeAmazing";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("player_level")) {
            return String.valueOf(levelManager.getPlayerLevel((Player) player));
        }

        if (params.equalsIgnoreCase("player_xp")) {
            return String.valueOf(levelManager.getPlayerXp((Player) player));
        }

        if (params.equalsIgnoreCase("player_multiplier")) {
            return String.valueOf(levelManager.getPlayerMultiplier((Player) player));
        }

        if (params.equalsIgnoreCase("player_next_level")) {
            return String.valueOf(levelManager.getPlayerXpToNextLevel((Player) player));
        }

        if (params.equalsIgnoreCase("player_remaining_xp")) {
            return String.valueOf(levelManager.getPlayerRemainingXp((Player) player));
        }

        return null;
    }
}
