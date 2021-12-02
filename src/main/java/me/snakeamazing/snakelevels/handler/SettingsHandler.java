package me.snakeamazing.snakelevels.handler;

import me.snakeamazing.snakelevels.file.FileMatcher;
import me.snakeamazing.snakelevels.file.YAMLFile;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsHandler {

    private final List<Material> materials;

    private final YAMLFile config;

    public SettingsHandler(FileMatcher fileMatcher) {
        materials = new ArrayList<>();

        this.config = fileMatcher.getFile("config");

        loadSettings();
    }

    public void loadSettings() {
        for (String material : config.getStringList("settings.material")) {
            materials.add(Material.valueOf(material));
        }
    }

    public List<Material> getMaterials() {
        return Collections.unmodifiableList(materials);
    }
}
