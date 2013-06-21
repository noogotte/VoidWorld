package org.spiritofcode.bukkit.voidworld.command;

import org.bukkit.command.CommandSender;
import org.spiritofcode.bukkit.voidworld.VoidWorld;
import org.spiritofcode.bukkit.voidworld.permission.Permission;

import java.util.logging.Logger;

public abstract class GenericCommand {

    // Id refer for a proper log identification
    private String id;
    // Permission checker
    private Permission perm;

    /**
     * Build a Generic command
     *
     * @param id         | Id command
     * @param permission | Permission for this command
     */
    public GenericCommand(String id, Permission permission) {
        this.id = id;
        perm = permission;
    }

    /**
     * Execute the command
     *
     * @param sender | Sender of the command
     * @param args   | Command arguments
     */
    public void execute(CommandSender sender, String[] args) {
        // Get a logger
        Logger logger = VoidWorld.getLogs();

        // Start timer & check
        long start = System.currentTimeMillis();

        logger.info(id + " - " + sender.getName());
        if (perm.check(sender)) {
            logger.info(id + " | Fail (" + (System.currentTimeMillis() - start) + " ms)");
            return;
        }
        logger.info(id + " | " + (onExecute(sender, args) ? "Success" : "Fail") + " (" + (System.currentTimeMillis() - start) + " ms)");
    }

    /**
     * Fired on execute event
     *
     * @param sender | Sender of the command
     * @param args   | Command arguments
     */
    protected abstract boolean onExecute(CommandSender sender, String[] args);

    /**
     * Usage of the command
     *
     * @return | Command usage
     */
    protected abstract String getUsage();

}
