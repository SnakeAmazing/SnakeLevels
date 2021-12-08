package me.snakeamazing.snakelevels.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import me.snakeamazing.snakelevels.manager.LevelManager;
import org.bukkit.entity.Player;

@Command(names = {"snakelevels", "sl"}, permission = "snakelevels.admin")
//@SubCommandClasses({
//        ReloadCommand.class
//})
public class LevelCommand implements CommandClass {

    private final LevelManager levelManager;

    private final YAMLFile messages;

    public LevelCommand(LevelManager levelManager, FileMatcher matcher) {
        this.levelManager = levelManager;

        this.messages = matcher.getFile("messages");
    }

    @Command(names = "")
    public boolean onSnakeLevelsCommand(@Sender Player player) {

        for (String message : messages.getStringList("messages.commands.help")) {
            player.sendMessage(message);
        }

        return true;
    }

    @Command(names = "give")
    public boolean onGiveCommand(@Sender Player player, Player target, int experience) {

        levelManager.updatePlayerLevel(player, experience);
        player.sendMessage(messages.getString("messages.commands.give.sender")
                .replace("%amount%", String.valueOf(experience))
                .replace("%target%", target.getName()));
        target.sendMessage(messages.getString("messages.commands.give.target")
                .replace("%amount%", String.valueOf(experience)));

        return true;
    }

    @Command(names = "remove")
    public boolean onRemoveCommand(@Sender Player player, Player target, int experience) {

        levelManager.updatePlayerLevel(player, -experience);
        player.sendMessage(messages.getString("messages.commands.remove.sender")
                .replace("%amount%", String.valueOf(experience))
                .replace("%target%", target.getName()));
        target.sendMessage(messages.getString("messages.commands.remove.target")
                .replace("%amount%", String.valueOf(experience)));

        return true;
    }

    @Command(names = "set")
    public boolean onResetCommand(@Sender Player player, Player target, int level) {

        levelManager.setPlayerLevel(player, level);
        player.sendMessage(messages.getString("messages.commands.set.sender")
                .replace("%level%", String.valueOf(level))
                .replace("%target%", target.getName()));
        target.sendMessage(messages.getString("messages.commands.set.target")
                .replace("%level%", String.valueOf(level)));

        return true;
    }

}
