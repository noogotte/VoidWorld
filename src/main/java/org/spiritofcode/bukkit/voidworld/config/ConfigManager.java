package org.spiritofcode.bukkit.voidworld.config;

import java.io.File;

public class ConfigManager {

    // World config reference
    private static WorldConfig world;

    /**
     * Build a config manager
     *
     * @param path | Path to all config
     */
    public ConfigManager(File path) {
        world = new WorldConfig(path);
    }

    /**
     * World config of this plugin
     *
     * @return | World config
     */
    public static WorldConfig getWorldConfig() {
        return world;
    }

    /**
     * Do a little clean & save if necessary
     */
    public void clean() {
        world.unload();
    }

}
