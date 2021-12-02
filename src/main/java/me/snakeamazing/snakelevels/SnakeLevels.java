package me.snakeamazing.snakelevels;

import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.SimplePartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.snakeamazing.snakelevels.commands.LevelCommand;
import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.handler.SettingsHandler;
import me.snakeamazing.snakelevels.manager.LevelManager;
import me.snakeamazing.snakelevels.listeners.*;
import me.snakeamazing.snakelevels.papi.PlaceholderAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SnakeLevels extends JavaPlugin {

    private LevelManager levelManager;
    private SettingsHandler settingsHandler;

    private FileMatcher fileMatcher;

    @Override
    public void onEnable() {
        fileMatcher = new FileMatcher(this);

        levelManager = new LevelManager(fileMatcher);
        settingsHandler = new SettingsHandler(fileMatcher);

        registerCommands();
        registerListeners(
                new PlayerJoinListener(levelManager),
                new PlayerQuitListener(levelManager),
                new PlayerLevelUpListener(levelManager, fileMatcher),
                new EntityDeathListener(levelManager, fileMatcher),
                new PlayerBlockBreakListener(fileMatcher, settingsHandler, levelManager),
                new PlayerFishListener(levelManager, fileMatcher)
        );

        hookWithPlaceholderAPI();
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommands() {
        PartInjector partInjector = new SimplePartInjector();
        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());

        AnnotatedCommandTreeBuilder treeBuilder = new AnnotatedCommandTreeBuilderImpl(partInjector);
        BukkitCommandManager commandManager = new BukkitCommandManager(this.getName());

        commandManager.registerCommands(treeBuilder.fromClass(new LevelCommand(levelManager, fileMatcher)));
    }

    private void hookWithPlaceholderAPI() {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(levelManager).register();
        }
    }

}