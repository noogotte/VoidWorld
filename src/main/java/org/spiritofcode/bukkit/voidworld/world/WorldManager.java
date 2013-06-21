package org.spiritofcode.bukkit.voidworld.world;

import org.bukkit.*;
import org.spiritofcode.bukkit.voidworld.config.WorldConfig;

import java.io.File;

import static org.spiritofcode.bukkit.voidworld.config.WorldConfig.WorldEntry;

public class WorldManager {

    // Ref to config
    private WorldConfig config;

    /**
     * Build World manager
     */
    public WorldManager() {
    }

    /**
     * Do a little clean
     */
    public void clean() {
    }

    /**
     * Check if a world can be load
     *
     * @param worldName | Name of the world
     */
    public WorldStatus check(String worldName, boolean check) {
        Server server = Bukkit.getServer();

        if (server.getWorld(worldName) != null) {
            return WorldStatus.ALREADY_LOAD;
        } else if (check && !(new File(server.getWorldContainer(), worldName).exists())) {
            return WorldStatus.UNKNOWN;
        } else {
            return WorldStatus.READY;
        }
    }

    /**
     * Load a world on start
     *
     * @param worldName | Name of the world
     * @param entry     | Memory entry
     */
    public void load(String worldName, WorldEntry entry) {
        // Check world prerequire
        WorldStatus check = check(worldName, false);
        if (check == WorldStatus.ALREADY_LOAD && config.get(worldName) == null) {
            Location loc = entry.getLocation();
            if (loc == null) {
                entry.setLocation(Bukkit.getWorld(worldName).getSpawnLocation());
            } else {
                loc.setWorld(Bukkit.getWorld(worldName));
            }
            config.create(worldName, entry);
        }
        if (check != WorldStatus.READY) return;

        // Get environment for the world
        World world = Bukkit.getServer().createWorld(new WorldCreator(worldName).environment(entry.getEnvironment()));

        // Get difficulty for the world
        world.setDifficulty(entry.getDifficulty());

        // Get pvp & all datas
        world.setPVP(entry.isPvp());
        world.setSpawnFlags(entry.isAnimals(), entry.isMonsters());

        // Check spawn
        entry.getLocation().setWorld(world);

        config.create(worldName, entry);
    }

    /**
     * Load a world properly
     *
     * @param worldName | Name of the world
     * @param env       | Environment of the world
     * @param seed      | Seed for world creation
     * @return | Status of the world
     */
    public WorldStatus create(String worldName, World.Environment env, Long seed) {
        return load(worldName, env, seed, false);
    }

    /**
     * Load a world properly
     *
     * @param worldName | Name of the world
     * @param env       | Environment of the world
     * @param seed      | Seed for world creation
     * @return | Status of the world
     */
    public WorldStatus load(String worldName, World.Environment env, Long seed) {
        return load(worldName, env, seed, true);
    }

    /**
     * Load a world properly
     *
     * @param worldName | Name of the world
     * @param env       | Environment of the world
     * @param seed      | Seed for world creation
     * @param force     | Force check
     * @return | Status of the world
     */
    public WorldStatus load(String worldName, World.Environment env, Long seed, boolean force) {
        // Check world prerequire
        WorldStatus check = check(worldName, force);
        if (check != WorldStatus.READY) {
            return check;
        }

        // Get world config
        WorldEntry entry = config.get(worldName);
        Server server = Bukkit.getServer();
        World world;

        if (entry != null) {
            // Get environment for the world
            world = server.createWorld(new WorldCreator(worldName).environment(entry.getEnvironment()));

            // Get difficulty for the world
            world.setDifficulty(entry.getDifficulty());

            // Get pvp & all datas
            world.setPVP(entry.isPvp());
            world.setSpawnFlags(entry.isAnimals(), entry.isMonsters());

            // Check spawn
            entry.getLocation().setWorld(world);
        } else {
            // Create world and new entry
            if (env != null && seed != null) {
                world = server.createWorld(new WorldCreator(worldName).environment(env).seed(seed));
            } else if (env != null) {
                world = server.createWorld(new WorldCreator(worldName).environment(env));
            } else {
                world = server.createWorld(new WorldCreator(worldName));
            }
            entry = new WorldEntry();

            // Set entry
            entry.setEnvironment(world.getEnvironment());
            entry.setDifficulty(world.getDifficulty());
            entry.setPvp(world.getPVP());
            entry.setAnimals(world.getAllowAnimals());
            entry.setMonsters(world.getAllowMonsters());

            // Set spawn
            entry.setLocation(world.getSpawnLocation());

            // Add entry
            config.create(worldName, entry);
        }

        return WorldStatus.LOAD;
    }

    /**
     * Unload a world properly
     *
     * @param worldName | Name of the world
     * @return | Status of the process
     */
    public WorldStatus unload(String worldName) {
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            return WorldStatus.UNKNOWN;
        } else if (!world.getPlayers().isEmpty()) {
            return WorldStatus.NOT_EMPTY;
        } else {
            Bukkit.getServer().unloadWorld(worldName, true);
            config.remove(worldName, config.get(worldName));
            return WorldStatus.UNLOAD;
        }
    }

    /**
     * Set config param
     *
     * @param config | World config pointer
     */
    public void setConfig(WorldConfig config) {
        this.config = config;
    }

}
