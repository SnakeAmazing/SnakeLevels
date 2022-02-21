package me.snakeamazing.snakelevels.listeners;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private final YAMLFile config;
    private final YAMLFile messages;

    private final SettingsHandler settingsHandler;
    private final LevelManager levelManager;

    public BlockBreakListener(FileMatcher matcher, SettingsHandler settingsHandler, LevelManager levelManager) {
        this.config = matcher.getFile("config");
        this.messages = matcher.getFile("messages");

        this.settingsHandler = settingsHandler;
        this.levelManager = levelManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        if (!query.testState(localPlayer.getLocation(), localPlayer, Flags.BUILD)) {
            return;
        }

        ApplicableRegionSet regions = query.getApplicableRegions(localPlayer.getLocation());

        for (ProtectedRegion region : regions) {
            if (!region.getId().equals("__global__")) {
                return;
            }
        }

        if (!config.contains("settings.material." + block.getType()))
            return;

        levelManager.updatePlayerLevel(player, levelManager.getPlayerMultiplier(player) * config.getDouble("settings.material." + block.getType()));

        BaseComponent baseComponent = new TextComponent(
                messages.getString("messages.level-update")
                        .replace("%xp%", String.valueOf(levelManager.getPlayerXp(player)))
                        .replace("%next-level-xp%", String.valueOf(levelManager.getPlayerXpToNextLevel(player))));

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, baseComponent);
    }
}
