package me.snakeamazing.snakelevels.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;

@Command(names = "reload")
public class ReloadCommand implements CommandClass {

    private final YAMLFile messages;
    private final YAMLFile config;

    public ReloadCommand(FileMatcher matcher) {
        this.messages = matcher.getFile("messages");
        this.config = matcher.getFile("config");
    }

    @Command(names = "")
    public boolean onReloadCommand() {

        messages.reload();
        config.reload();

        return true;
    }
}
