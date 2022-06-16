package me.snakeamazing.snakelevels;

import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
                new PlayerJoinListener(levelManager, fileMatcher),
                new PlayerQuitListener(levelManager),
                new PlayerLevelUpListener(levelManager, fileMatcher),
                new EntityDeathListener(levelManager, fileMatcher),
                new BlockBreakListener(fileMatcher, settingsHandler, levelManager),
                new PlayerFishListener(levelManager, fileMatcher),
                new BlockPlaceListener(fileMatcher, settingsHandler, levelManager)
        );

        hookWithPlaceholderAPI();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            levelManager.removeLevelPlayer(player);
        }
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

//        AnnotatedCommandBuilder builder = AnnotatedCommandBuilder.create(partInjector);
//
//        SubCommandInstanceCreator instanceCreator = (clazz, parent) -> {
//            if (clazz == ReloadCommand.class) {
//                return new ReloadCommand(fileMatcher);
//            }
//
//            return null;
//        };

        AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(partInjector);

        BukkitCommandManager commandManager = new BukkitCommandManager(this.getName());

        commandManager.registerCommands(treeBuilder.fromClass(new LevelCommand(levelManager, fileMatcher)));
    }

    private void hookWithPlaceholderAPI() {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(levelManager).register();
        }
    }

}
