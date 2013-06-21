package org.spiritofcode.bukkit.voidworld.permission;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.spiritofcode.bukkit.voidworld.VoidWorld;

public class PermissionManager {

    // Permission deny pre format string
    public static String DENY;
    // Reference to Vault bridge
    private Permission permission = null;

    /**
     * Build a permission manager
     */
    public PermissionManager() {
        if (setupPermissions()) {
            VoidWorld.getLogs().info("Bridge for Vault succesfully load.");
        } else {
            VoidWorld.getLogs().info("Bridge for Vault fail load.");
        }

        DENY = ChatColor.RED + "[VoidWorld] Vous n'avez pas la permission pour cela.";
    }

    /**
     * Get if the permission manager is ready or not
     *
     * @return | Ready or dead
     */
    public boolean isReady() {
        return permission != null;
    }

    /**
     * Do a little clean
     */
    public void clean() {
        DENY = null;
        permission = null;
    }

    /**
     * Check if the player has a given permission
     *
     * @param sender | Sender command
     * @param perm   | Permission to check
     * @return | Has or not
     */
    public boolean has(CommandSender sender, String perm) {
        return permission.has(sender, perm);
    }

    /**
     * Attempts to get vault permission bridge
     *
     * @return | If the setup is a success or a fail
     */
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

}
