package org.spiritofcode.bukkit.voidworld.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.spiritofcode.bukkit.voidworld.VoidWorld;
import org.spiritofcode.bukkit.voidworld.permission.Permission;
import org.spiritofcode.bukkit.voidworld.permission.PermissionType;
import org.spiritofcode.bukkit.voidworld.world.WorldManager;
import org.spiritofcode.bukkit.voidworld.world.WorldStatus;

public class UnloadCommand extends GenericCommand {

    // World manager
    private WorldManager manager;

    /**
     * Build command
     */
    public UnloadCommand() {
        super("VoidWorld | Unload", new Permission("voidworld.unload", PermissionType.PLAYER));
        manager = VoidWorld.getWorldManager();
    }

    /**
     * Execute event
     *
     * @param sender | Sender of the command
     * @param args   | Command arguments
     */
    @Override
    protected boolean onExecute(CommandSender sender, String[] args) {
        switch (args.length) {
            case 1:
                return unload(sender, args);
            default:
                sender.sendMessage(getUsage());
                return false;
        }
    }

    /**
     * Usage of the command
     *
     * @return | Command usage
     */
    @Override
    protected String getUsage() {
        return ChatColor.AQUA + "Utilisation: /voidworld unload <monde>";
    }

    /**
     * Implementation
     *
     * @return | Success or Fail
     */
    private boolean unload(CommandSender sender, String[] args) {
        WorldStatus status = manager.unload(args[0]);
        switch (status) {
            case UNKNOWN:
                sender.sendMessage(ChatColor.RED + "Le monde \"" + args[0] + "\" n'est pas chargé.");
                return false;
            case NOT_EMPTY:
                sender.sendMessage(ChatColor.RED + "Il y a encore des joueurs dans le monde \"" + args[0] + "\".");
                return false;
            default:
                sender.sendMessage(ChatColor.GREEN + "Vous avez déchargé avec succès le monde \"" + args[0] + "\".");
                return true;
        }
    }
}

