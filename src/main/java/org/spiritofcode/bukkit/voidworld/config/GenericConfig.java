package org.spiritofcode.bukkit.voidworld.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spiritofcode.bukkit.voidworld.VoidWorld;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class GenericConfig {

    // Instance of config yml
    protected YamlConfiguration config;
    // File (Path) to the config
    protected File path;

    /**
     * Create a GenericConfig
     *
     * @param path | Path to the config directory
     */
    public GenericConfig(File path) {
        this.path = path;
        load();
    }

    /**
     * Load config if exist or create config by default
     */
    public void load() {
        // New config
        config = new YamlConfiguration();

        // Load default config
        defaults();

        // Check config
        check();

        // Call load event
        onLoad();
    }

    /**
     * Unload config properly
     */
    public void unload() {
        // Get the logger
        Logger logger = VoidWorld.getLogs();

        // Call unload event
        onUnload();

        try {
            logger.info("[VoidWorld] " + getName() + " | Save configuration...");
            config.save(path.getAbsoluteFile());
        } catch (IOException ignored) {
            logger.severe("[VoidWorld] " + getName() + " | File configuration can't be save...");
        }

        config = null;
        path = null;
    }

    /**
     * Check & Create if necessary the config file
     */
    private void check() {
        // Check exist
        if (path.exists()) {
            loadConfig();
        } else {
            createConfig();
        }
    }

    /**
     * Create config file
     */
    private void createConfig() {
        // Get the logger
        Logger logger = VoidWorld.getLogs();

        // Create file config
        try {
            logger.info("[VoidWorld] " + getName() + " | Create default configuration...");
            config.save(path.getAbsoluteFile());
        } catch (IOException ex) {
            logger.severe("[VoidWorld] " + getName() + " | File configuration can't be create...");
        }
    }

    /**
     * Load config file
     */
    private void loadConfig() {
        // Get the logguer
        Logger logger = VoidWorld.getLogs();

        // Load all properties
        try {
            logger.info("[VoidWorld] " + getName() + " | Loading configuration...");
            config.load(path.getAbsoluteFile());
        } catch (IOException ignored) {
            logger.info("[VoidWorld] " + getName() + " | Loading configuration failed...");
            createConfig();
        } catch (InvalidConfigurationException ignored) {
            logger.info("[VoidWorld] " + getName() + " | Loading configuration failed...");
            createConfig();
        }
    }

    /**
     * Fired an event config load
     */
    protected void onLoad() {
    }

    /**
     * Fired an event config unload
     */
    protected void onUnload() {
    }

    /**
     * Default value
     */
    protected void defaults() {
    }

    /**
     * Get name of the config
     *
     * @return | Config name
     */
    protected abstract String getName();

}
