package org.spiritofcode.bukkit.voidworld.permission;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.spiritofcode.bukkit.voidworld.VoidWorld;

public class Permission {

    // Permission type for powerfull check
    private PermissionType type;
    // Permission to check
    private String permission;

    /**
     * Build a Generic permission
     *
     * @param permission | Permission to check
     * @param type       | Type of check
     */
    public Permission(String permission, PermissionType type) {
        this.type = type;
        this.permission = permission;
    }

    /**
     * Do check and return status
     *
     * @param sender | Sender of the command
     * @return | Status of check
     */
    public boolean check(CommandSender sender) {
        switch (type) {
            case PLAYER:
                return checkPlayer(sender);
            case PLAYER_CONSOLE:
                return checkConsolePlayer(sender);
            case CONSOLE:
                return checkConsole(sender);
            default:
                return checkPlayer(sender);
        }
    }

    /**
     * Do check console & player
     *
     * @param sender | Sender of the command
     * @return | Status of check
     */
    private boolean checkConsolePlayer(CommandSender sender) {
        return checkConsole(sender) || checkPlayer(sender);
    }

    /**
     * Do check console
     *
     * @param sender | Sender of the command
     * @return | Status of check
     */
    private boolean checkConsole(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(PermissionManager.DENY);
            return true;
        }
        return false;
    }

    /**
     * Do check player
     *
     * @param sender | Sender of the command
     * @return | Status of check
     */
    private boolean checkPlayer(CommandSender sender) {
        if (VoidWorld.getPermission().has(sender, permission)) return false;
        sender.sendMessage(PermissionManager.DENY);
        return true;
    }

}
