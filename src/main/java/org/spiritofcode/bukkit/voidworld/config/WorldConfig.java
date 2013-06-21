package org.spiritofcode.bukkit.voidworld.config;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.spiritofcode.bukkit.voidworld.VoidWorld;
import org.spiritofcode.bukkit.voidworld.world.WorldManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.World.Environment;

public class WorldConfig extends GenericConfig {

    // Instance of World manager
    private WorldManager manager;
    // All kits entry
    private Map<String, WorldEntry> worlds;

    /**
     * Create a WorldConfig
     */
    public WorldConfig(File path) {
        super(new File(path, "config.yml"));
    }

    /**
     * Load event
     */
    @Override
    protected void onLoad() {
        manager = VoidWorld.getWorldManager();
        manager.setConfig(this);
        worlds = new HashMap<String, WorldEntry>(16);

        // Get list of all kits availables
        List<String> load = config.getStringList("load-worlds");
        if (load == null) {
            VoidWorld.getLogs().severe("Can't load worlds !");
            return;
        }

        // Setup & load all entry
        for (String world : load) {
            parse(world);
        }
    }

    @Override
    protected void onUnload() {
        // Get loaded worlds
        List<World> loaded = Bukkit.getWorlds();
        List<String> saved = new ArrayList<String>(16);

        // Save all world loaded
        String worldName;
        WorldEntry entry;
        for (World world : loaded) {
            worldName = world.getName();
            entry = worlds.get(worldName);
            saved.add(worldName);
            if (entry != null) save(worldName, entry);
        }

        config.set("load-worlds", saved);
        manager = null;
    }

    /**
     * Get name of the config
     *
     * @return | Config name
     */
    @Override
    protected String getName() {
        return "Config";
    }

    /**
     * Get a WorldEntry or Null
     *
     * @param world | Name of the world
     * @return | WorldEntry or Null
     */
    public WorldEntry get(String world) {
        return worlds.get(world);
    }

    /**
     * Put an entry into worlds
     *
     * @param world | Name of the world
     * @param entry | Complete entry
     */
    public void create(String world, WorldEntry entry) {
        worlds.put(world, entry);
    }

    /**
     * Remove an entry into worlds
     * @param world | Name of the world
     * @param entry | Entry
     */
    public void remove(String world, WorldEntry entry) {
        save(world, entry);
        worlds.remove(world);
    }

    /**
     * Parse data for a given world
     *
     * @param world | Name of the world
     */
    private void parse(String world) {
        WorldEntry entry = new WorldEntry();
        entry.setPvp(config.getBoolean(world + ".pvp", true));
        entry.setAnimals(config.getBoolean(world + ".animals", true));
        entry.setMonsters(config.getBoolean(world + ".monsters", true));
        entry.setDifficulty(getDifficulty(world));
        entry.setEnvironment(getEnvironment(world));
        entry.setLocation(getLocation(world));
        entry.setGamemode(config.getBoolean(world + ".gamemode", false));
        manager.load(world, entry);
    }

    /**
     * Save a world
     *
     * @param world | Name of the world
     * @param entry | All data for this world
     */
    private void save(String world, WorldEntry entry) {
        config.set(world + ".pvp", entry.isPvp());
        config.set(world + ".animals", entry.isAnimals());
        config.set(world + ".monsters", entry.isMonsters());
        config.set(world + ".difficulty", getDifficulty(entry.getDifficulty()));
        config.set(world + ".environment", getEnvironment(entry.getEnvironment()));
        setLocation(world, entry.getLocation());
        config.set(world + ".gamemode", entry.isGamemode());
    }

    /**
     * Get difficulty
     *
     * @return | World difficulty
     */
    private Difficulty getDifficulty(String world) {
        int value = config.getInt(world + ".difficulty", 2);
        switch (value) {
            case 0:
                return Difficulty.PEACEFUL;
            case 1:
                return Difficulty.EASY;
            case 2:
                return Difficulty.NORMAL;
            case 3:
                return Difficulty.HARD;
            default:
                return Difficulty.NORMAL;
        }
    }

    /**
     * Get difficulty
     *
     * @param difficulty | Difficulty
     * @return | World difficulty int
     */
    private int getDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case PEACEFUL:
                return 0;
            case EASY:
                return 1;
            case NORMAL:
                return 2;
            case HARD:
                return 3;
            default:
                return 2;
        }
    }

    /**
     * Get Environment
     *
     * @return | World Environment
     */
    private Environment getEnvironment(String world) {
        int value = config.getInt(world + ".environment", 0);
        switch (value) {
            case 0:
                return Environment.NORMAL;
            case 1:
                return Environment.NETHER;
            case 2:
                return Environment.THE_END;
            default:
                return Environment.NORMAL;
        }
    }

    /**
     * Get Environment
     *
     * @param environment | Environment
     * @return | World Environment int
     */
    private int getEnvironment(Environment environment) {
        switch (environment) {
            case NORMAL:
                return 0;
            case NETHER:
                return 1;
            case THE_END:
                return 2;
            default:
                return 0;
        }
    }

    /**
     * Convert resultset to location
     *
     * @return | New location
     */
    private Location getLocation(String world) {
        if (config.isDouble(world + ".spawn.x") && config.isDouble(world + ".spawn.y") && config.isDouble(world + ".spawn.z") && config.isDouble(world + ".spawn.yaw") && config.isDouble(world + ".spawn.pitch")) {
            double x = config.getDouble(world + ".spawn.x");
            double y = config.getDouble(world + ".spawn.y");
            double z = config.getDouble(world + ".spawn.z");
            float yaw = (float) config.getDouble(world + ".spawn.yaw");
            float pitch = (float) config.getDouble(world + ".spawn.pitch");

            return new Location(null, x, y, z, yaw, pitch);
        }
        return null;
    }

    /**
     * Set spawn location before save
     *
     * @param world | Name of the world
     * @param loc   | Location of the fix spawn
     */
    private void setLocation(String world, Location loc) {
        config.set(world + ".spawn.x", loc.getX());
        config.set(world + ".spawn.y", loc.getY());
        config.set(world + ".spawn.z", loc.getZ());
        config.set(world + ".spawn.yaw", loc.getYaw());
        config.set(world + ".spawn.pitch", loc.getPitch());
    }

    /**
     * World Entry
     */
    public static class WorldEntry {
        // Pvp default value
        private boolean pvp = true;
        // Animals default value
        private boolean animals = true;
        // Monsters default value
        private boolean monsters = true;
        // Difficulty default value
        private Difficulty difficulty = Difficulty.NORMAL;
        // Environment default value
        private Environment environment = Environment.NORMAL;
        // Spawn default value
        private Location location = null;
        // Gamemode default value
        private boolean gamemode = false;

        /**
         * Get if the world allow the pvp
         *
         * @return | Allow pvp
         */
        public boolean isPvp() {
            return pvp;
        }

        /**
         * Set pvp allow
         *
         * @param pvp | Allow pvp
         */
        public void setPvp(boolean pvp) {
            this.pvp = pvp;
        }

        /**
         * Get if the world allow animals spawn
         *
         * @return | Allow animals
         */
        public boolean isAnimals() {
            return animals;
        }

        /**
         * Set animals allow
         *
         * @param animals | Allow animals
         */
        public void setAnimals(boolean animals) {
            this.animals = animals;
        }

        /**
         * Get if the world allow monsters spawn
         *
         * @return | Allow monsters
         */
        public boolean isMonsters() {
            return monsters;
        }

        /**
         * Set monsters allow
         *
         * @param monsters | Allow monsters
         */
        public void setMonsters(boolean monsters) {
            this.monsters = monsters;
        }

        /**
         * Get the world difficulty
         *
         * @return | World difficulty
         */
        public Difficulty getDifficulty() {
            return difficulty;
        }

        /**
         * Set the world difficulty
         *
         * @param difficulty | World difficulty
         */
        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

        /**
         * Get the world environment
         *
         * @return | World environment
         */
        public Environment getEnvironment() {
            return environment;
        }

        /**
         * Set the world environment
         *
         * @param environment | World environment
         */
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        /**
         * Get world spawn location
         */
        public Location getLocation() {
            return location;
        }

        /**
         * Set world spawn location
         *
         * @param location | New location
         */
        public void setLocation(Location location) {
            if (this.location == null) {
                this.location = location;
                return;
            }

            this.location.setX(location.getX());
            this.location.setY(location.getY());
            this.location.setZ(location.getZ());
            this.location.setYaw(location.getYaw());
            this.location.setPitch(location.getPitch());
            this.location.setWorld(location.getWorld());
        }

        /**
         * Get if the world allow gamemode
         *
         * @return | Gamemode base
         */
        public boolean isGamemode() {
            return gamemode;
        }

        /**
         * Set gamemode of the world
         *
         * @param gamemode | World gamemode
         */
        public void setGamemode(boolean gamemode) {
            this.gamemode = gamemode;
        }

    }

}
