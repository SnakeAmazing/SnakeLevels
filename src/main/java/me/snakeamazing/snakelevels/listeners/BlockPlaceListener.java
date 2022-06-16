package me.snakeamazing.snakelevels.listeners;

import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import me.snakeamazing.snakelevels.handler.SettingsHandler;
import me.snakeamazing.snakelevels.manager.LevelManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final YAMLFile config;

    private final YAMLFile messages;

    private final SettingsHandler settingsHandler;

    private final LevelManager levelManager;

    public BlockPlaceListener(FileMatcher matcher, SettingsHandler settingsHandler, LevelManager levelManager) {
        this.config = matcher.getFile("config");
        this.messages = matcher.getFile("messages");
        this.settingsHandler = settingsHandler;
        this.levelManager = levelManager;
    }


    @EventHandler
    public void onBlockPlaceListener(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!config.contains("settings.material." + block.getType()))
            return;

        levelManager.updatePlayerLevel(player, -1 * levelManager.getPlayerMultiplier(player) * config.getInt("settings.material." + block.getType()));
        TextComponent textComponent = new TextComponent(messages.getString("messages.level-update")
                .replace("%xp%", String.valueOf(levelManager.getPlayerXp(player))).replace("%next-level-xp%", String.valueOf(levelManager.getPlayerXpToNextLevel(player))));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponent);
    }
}
