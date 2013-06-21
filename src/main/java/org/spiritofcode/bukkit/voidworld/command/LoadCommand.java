package org.spiritofcode.bukkit.voidworld.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.spiritofcode.bukkit.voidworld.VoidWorld;
import org.spiritofcode.bukkit.voidworld.permission.Permission;
import org.spiritofcode.bukkit.voidworld.permission.PermissionType;
import org.spiritofcode.bukkit.voidworld.world.WorldManager;
import org.spiritofcode.bukkit.voidworld.world.WorldStatus;

public class LoadCommand extends GenericCommand {

    // World manager
    private WorldManager manager;

    /**
     * Build command
     */
    public LoadCommand() {
        super("VoidWorld | Load", new Permission("voidworld.load", PermissionType.PLAYER));
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
                return load(sender, args);
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
        return ChatColor.AQUA + "Utilisation: /voidworld load <monde>";
    }

    /**
     * Implementation
     *
     * @return | Success or Fail
     */
    private boolean load(CommandSender sender, String[] args) {
        WorldStatus status = manager.load(args[0], null, null);
        switch (status) {
            case ALREADY_LOAD:
                sender.sendMessage(ChatColor.RED + "Le monde \"" + args[0] + "\" est déjà chargé.");
                return false;
            case UNKNOWN:
                sender.sendMessage(ChatColor.RED + "Le monde \"" + args[0] + "\" n'existe pas.");
                return false;
            default:
                sender.sendMessage(ChatColor.GREEN + "Vous avez chargé avec succès le monde \"" + args[0] + "\".");
                return true;
        }
    }
}
