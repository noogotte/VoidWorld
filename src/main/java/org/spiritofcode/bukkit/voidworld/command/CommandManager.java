package org.spiritofcode.bukkit.voidworld.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandManager {

    // Refer empty args
    private String[] NULLED = new String[0];
    // Reference to load command
    private LoadCommand load;
    // Reference to unload command
    private UnloadCommand unload;
    // Reference to create command
    private CreateCommand create;

    /**
     * Build a Command manager
     */
    public CommandManager() {
        load = new LoadCommand();
        unload = new UnloadCommand();
        create = new CreateCommand();
    }

    /**
     * Handle the event command
     *
     * @param sender | Player or Console calling the server
     * @param args   | Argumments for the commands
     * @return | If the commands is handle or not
     */
    public boolean dispatch(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(getUsage());
            return true;
        }

        String command = args[0];
        if (command.equals("load")) {
            load.execute(sender, getArgs(args));
        } else if (command.equals("unload")) {
            unload.execute(sender, getArgs(args));
        } else if (command.equals("create")) {
            create.execute(sender, getArgs(args));
        } else {
            sender.sendMessage(getUsage());
        }
        return true;
    }

    /**
     * Do a little clean
     */
    public void clean() {
        create = null;
        unload = null;
        load = null;
        NULLED = null;
    }

    /**
     * Get usage for dispatcher
     *
     * @return | Global usage
     */
    private String getUsage() {
        return ChatColor.AQUA + "[VoidWorld] Utilisation: /voidworld <load | unload>";
    }

    /**
     * Arguments utils
     *
     * @param args | Arguments to copy
     * @return | Copy arguments but decaled by one
     */
    private String[] getArgs(String[] args) {
        switch (args.length) {
            case 1:
                return NULLED;
            default:
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, newArgs.length);
                return newArgs;
        }
    }

}
