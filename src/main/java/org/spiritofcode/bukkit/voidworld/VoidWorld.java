package org.spiritofcode.bukkit.voidworld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.spiritofcode.bukkit.voidworld.command.CommandManager;
import org.spiritofcode.bukkit.voidworld.config.ConfigManager;
import org.spiritofcode.bukkit.voidworld.permission.PermissionManager;
import org.spiritofcode.bukkit.voidworld.world.WorldManager;

import java.util.logging.Logger;

public class VoidWorld extends JavaPlugin {

    // Plugin instance
    private static VoidWorld instance;
    // Logger instance
    private static Logger logger;
    // Permission manager
    private static PermissionManager permission;
    // World manager
    private static WorldManager world;
    // Config manager
    private static ConfigManager config;
    // Command manager
    private static CommandManager command;

    /**
     * Load the plugin
     */
    @Override
    public void onEnable() {
        instance = this;
        logger = Logger.getLogger("Minecraft");

        // Init all manager
        permission = new PermissionManager();
        world = new WorldManager();
        config = new ConfigManager(getDataFolder());
        command = new CommandManager();

        logger.info("[VoidWorld] Load & Ready !");
    }

    /**
     * Unload the plugin
     */
    @Override
    public void onDisable() {
        logger.info("[VoidWorld] Unload & Clean !");

        // Clean everything
        command.clean();
        config.clean();
        world.clean();
        permission.clean();
    }

    /**
     * Intercept all commands by default
     *
     * @param sender | Player or Console calling the server
     * @param cmd    | Command called => Ignored
     * @param label  | Label called => Ignored
     * @param args   | Argumments for the commands
     * @return | If the commands is handle or not
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return command.dispatch(sender, args);
    }

    /**
     * Get an instance of VoidWorld plugin
     *
     * @return | Instance of this plugin
     */
    public static VoidWorld getInstance() {
        return instance;
    }

    /**
     * Get an instance of Permission manager
     *
     * @return | Instance of permission manager
     */
    public static PermissionManager getPermission() {
        return permission;
    }

    /**
     * Get an instance of Logger
     *
     * @return | Instance of logger
     */
    public static Logger getLogs() {
        return logger;
    }

    /**
     * Get an instance of World manager
     *
     * @return | Instance of world manager
     */
    public static WorldManager getWorldManager() {
        return world;
    }

    /**
     * Get the config instance
     *
     * @return | Instance of config
     */
    public static ConfigManager getConfigManager() {
        return config;
    }

}
