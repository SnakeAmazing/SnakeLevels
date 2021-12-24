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

        switch (entity.getType()) {
            case ZOMBIE:
                updateExp(player, "zombie");
                break;
            case SKELETON:
                updateExp(player, "skeleton");
                break;
            case ENDERMAN:
                updateExp(player, "enderman");
                break;
            case CREEPER:
                updateExp(player, "creeper");
                break;
            case PILLAGER:
                updateExp(player, "pillager");
                break;
            case SILVERFISH:
                updateExp(player, "silverfish");
                break;
            case DROWNED:
                updateExp(player, "drowned-zombie");
                break;
            case BLAZE:
                updateExp(player, "blaze");
                break;
            case CAVE_SPIDER:
                updateExp(player, "cave-spider");
                break;
            case SPIDER:
                updateExp(player, "spider");
                break;
            case GHAST:
                updateExp(player, "ghast");
                break;
            case GUARDIAN:
                updateExp(player, "guardian");
                break;
            case HOGLIN:
                updateExp(player, "hoglin");
                break;
            case MAGMA_CUBE:
                updateExp(player, "magma-cube");
                break;
            case PIGLIN:
                updateExp(player, "piglin");
                break;
            case RAVAGER:
                updateExp(player, "ravager");
                break;
            case SHULKER:
                updateExp(player, "shulker");
                break;
            case SLIME:
                updateExp(player, "slime");
                break;
            case ENDER_DRAGON:
                updateExp(player, "ender-dragon");
                break;
            case STRAY:
                updateExp(player, "stray");
                break;
            case ZOGLIN:
                updateExp(player, "zoglin");
                break;
            case WITHER_SKELETON:
                updateExp(player, "wither-skeleton");
                break;
            case WITCH:
                updateExp(player, "witch");
                break;
            case VINDICATOR:
                updateExp(player, "vindicator");
                break;
            default:
                updateExp(player, "default");
        }

        BaseComponent baseComponent = new TextComponent(
                messages.getString("messages.level-update")
                        .replace("%xp%", String.valueOf(levelManager.getPlayerXp(player)))
                        .replace("%next-level-xp%", String.valueOf(levelManager.getPlayerXpToNextLevel(player))));

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent);
    }

    private void updateExp(Player player, String path) {
        levelManager.updatePlayerLevel(player, levelManager.getPlayerMultiplier(player) * config.getInt("settings.entities." + path));
    }
}
