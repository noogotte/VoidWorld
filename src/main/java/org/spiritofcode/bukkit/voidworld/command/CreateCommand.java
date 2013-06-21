package org.spiritofcode.bukkit.voidworld.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.spiritofcode.bukkit.voidworld.VoidWorld;
import org.spiritofcode.bukkit.voidworld.permission.Permission;
import org.spiritofcode.bukkit.voidworld.permission.PermissionType;
import org.spiritofcode.bukkit.voidworld.world.WorldManager;

import java.io.File;

public class CreateCommand extends GenericCommand {

    // World manager
    private WorldManager manager;

    /**
     * Build command
     */
    public CreateCommand() {
        super("VoidWorld | Create", new Permission("voidworld.create", PermissionType.PLAYER));
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
                return create(sender, args);
            case 2:
                return createType(sender, args);
            case 3:
                return createFull(sender, args);
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
        return ChatColor.AQUA + "Utilisation: /voidworld create <monde> [type] [seed]";
    }

    /**
     * Implementation
     *
     * @return | Success or Fail
     */
    private boolean create(CommandSender sender, String[] args) {
        if (new File(Bukkit.getServer().getWorldContainer(), args[0]).exists()) {
            sender.sendMessage(ChatColor.RED + "Le monde \"" + args[0] + "\" existe déjà.");
            return false;
        }

        manager.create(args[0], null, null);
        sender.sendMessage(ChatColor.GREEN + "Vous avez créé avec succès le monde \"" + args[0] + "\".");
        return true;
    }

    /**
     * Implementation
     *
     * @return | Success or Fail
     */
    private boolean createType(CommandSender sender, String[] args) {
        if (new File(Bukkit.getServer().getWorldContainer(), args[0]).exists()) {
            sender.sendMessage(ChatColor.RED + "Le monde \"" + args[0] + "\" existe déjà.");
            return false;
        }

        if (args[1].equals("normal")) {
            manager.create(args[0], World.Environment.NORMAL, null);
        } else if (args[1].equals("nether")) {
            manager.create(args[0], World.Environment.NETHER, null);
        } else if (args[1].equals("end")) {
            manager.create(args[0], World.Environment.THE_END, null);
        } else {
            sender.sendMessage(getUsage());
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Vous avez créé avec succès le monde \"" + args[0] + "\".");
        return true;
    }

    /**
     * Implementation
     *
     * @return | Success or Fail
     */
    private boolean createFull(CommandSender sender, String[] args) {
        if (new File(Bukkit.getServer().getWorldContainer(), args[0]).exists()) {
            sender.sendMessage(ChatColor.RED + "Le monde \"" + args[0] + "\" existe déjà.");
            return false;
        }

        long seed;
        try {
            seed = Long.parseLong(args[2]);
        } catch (Exception ignore) {
            sender.sendMessage(getUsage());
            return false;
        }

        if (args[1].equals("normal")) {
            manager.create(args[0], World.Environment.NORMAL, seed);
        } else if (args[1].equals("nether")) {
            manager.create(args[0], World.Environment.NETHER, seed);
        } else if (args[1].equals("end")) {
            manager.create(args[0], World.Environment.THE_END, seed);
        } else {
            sender.sendMessage(getUsage());
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Vous avez créé avec succès le monde \"" + args[0] + "\".");
        return true;
    }

}