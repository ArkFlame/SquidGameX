package com.arkflame.squidgame.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.errors.ArenaAlreadyExistException;
import com.arkflame.squidgame.errors.ArenaMisconfiguredException;
import com.arkflame.squidgame.errors.NoAvailableArenaException;

import dev._2lstudios.jelly.config.Configuration;

public class ArenaManager {

    private List<Arena> arenas;
    private File arenasPath;

    public ArenaManager(SquidGame plugin) {
        this.arenas = new ArrayList<>();
        this.arenasPath = new File(plugin.getDataFolder(), "arenas");

        if (!this.arenasPath.exists()) {
            this.arenasPath.mkdirs();
        }

        try {
            this.scanForArenas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scanForArenas() throws IOException {
        for (File fileEntry : this.arenasPath.listFiles()) {
            if (!fileEntry.isDirectory() && fileEntry.getName().endsWith(".yml")) {
                Configuration arenaConfig = new Configuration(fileEntry);

                try {
                    arenaConfig.load();
                } catch (Exception e) {
                }

                String name = fileEntry.getName().split("[.]")[0];

                World world = Bukkit.getWorld(arenaConfig.getString("arena.world"));
                if (world == null) {
                    world = new WorldCreator(arenaConfig.getString("arena.world")).createWorld();
                }

                Arena arena = new Arena(world, name, arenaConfig);
                this.arenas.add(arena);
            }
        }
    }

    public Arena getArena(String name) {
        for (Arena arena : this.arenas) {
            if (arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }

        return null;
    }

    public Arena createArena(String name, World world) throws ArenaAlreadyExistException, IOException {
        if (this.getArena(name) != null) {
            throw new ArenaAlreadyExistException(name);
        }

        Configuration arenaConfig = new Configuration(new File(this.arenasPath, name + ".yml"));
        Arena arena = new Arena(world, name, arenaConfig);
        arenaConfig.set("arena.world", world.getName());
        arenaConfig.save();
        this.arenas.add(arena);
        return arena;
    }

    public Arena getFirstAvailableArena() throws NoAvailableArenaException, ArenaMisconfiguredException {
        for (Arena arena : arenas) {
            if (arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING) {
                return arena;
            }
        }

        throw new NoAvailableArenaException();
    }

    public List<Arena> getArenas() {
        return this.arenas;
    }
}
