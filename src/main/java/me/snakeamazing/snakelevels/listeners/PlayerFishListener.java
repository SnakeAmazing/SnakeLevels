package me.snakeamazing.snakelevels.listeners;

import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import me.snakeamazing.snakelevels.manager.LevelManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerFishListener implements Listener {

    private final LevelManager levelManager;

    private final YAMLFile config;
    private final YAMLFile messages;

    public PlayerFishListener(LevelManager levelManager, FileMatcher fileMatcher) {
        this.levelManager = levelManager;

        this.config = fileMatcher.getFile("config");
        this.messages = fileMatcher.getFile("messages");
    }

    @EventHandler
    public void onPlayerFishEvent(PlayerFishEvent event) {
        Player player = event.getPlayer();

        levelManager.updatePlayerLevel(player,levelManager.getPlayerMultiplier(player) * config.getDouble("settings.fishing-xp"));

        BaseComponent baseComponent = new TextComponent(
                messages.getString("messages.level-update")
                        .replace("%xp%", String.valueOf(levelManager.getPlayerXp(player)))
                        .replace("%next-level-xp%", String.valueOf(levelManager.getPlayerXpToNextLevel(player))));

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent);
    }
}
