package me.snakeamazing.snakelevels.listeners;

import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import me.snakeamazing.snakelevels.manager.LevelManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Locale;

public class EntityDeathListener implements Listener {

    private final LevelManager levelManager;

    private final YAMLFile config;
    private final YAMLFile messages;

    public EntityDeathListener(LevelManager levelManager, FileMatcher fileMatcher) {
        this.levelManager = levelManager;

        this.config = fileMatcher.getFile("config");
        this.messages = fileMatcher.getFile("messages");
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();

        if (player == null) {
            return;
        }

        String path = "settings.entities." + entity.getType().toString().toLowerCase(Locale.ROOT);

        if (config.contains(path)) {
            path = "settings.entities.default";
        }

        levelManager.updatePlayerLevel(player, levelManager.getPlayerMultiplier(player) * config.getInt(path));

        BaseComponent baseComponent = new TextComponent(
                messages.getString("messages.level-update")
                        .replace("%xp%", String.valueOf(levelManager.getPlayerXp(player)))
                        .replace("%next-level-xp%", String.valueOf(levelManager.getPlayerXpToNextLevel(player))));

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent);
    }
}
