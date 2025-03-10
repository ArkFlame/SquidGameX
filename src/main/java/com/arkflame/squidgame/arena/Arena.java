package com.arkflame.squidgame.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.games.ArenaGameBase;
import com.arkflame.squidgame.arena.games.G1RedGreenLightGame;
import com.arkflame.squidgame.arena.games.G3BattleGame;
import com.arkflame.squidgame.arena.games.G6GlassesGame;
import com.arkflame.squidgame.arena.games.G7SquidGame;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.player.SquidPlayer;

public class Arena {
    private List<SquidPlayer> players;
    private List<SquidPlayer> spectators;
    private List<ArenaGameBase> games;

    private Configuration mainConfig;
    private Configuration arenaConfig;
    private ArenaHandler handler;
    private World world;
    private String name;

    private String joined, leaved, death;

    private ArenaState state = ArenaState.WAITING;
    private ArenaGameBase currentGame;
    private int internalTime;
    private boolean allowPvP;

    public Arena(World world, String name, Configuration arenaConfig) {
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.games = new ArrayList<>();

        this.mainConfig = SquidGame.getInstance().getMainConfig();
        this.arenaConfig = arenaConfig;
        this.handler = new ArenaHandler(this);
        this.world = world;
        this.name = name;

        this.resetArena();
    }

    public void resetArena() {
        for (SquidPlayer player : this.getAllPlayers()) {
            this.removePlayer(player);
        }

        this.state = ArenaState.WAITING;
        this.currentGame = null;
        this.internalTime = -1;
        this.allowPvP = false;

        this.leaved = null;
        this.joined = null;
        this.death = null;

        this.players.clear();
        this.spectators.clear();
        this.games.clear();

        int game1Time = mainConfig.getInt("game-settings.game-time.1", 60);
        int game3Time = mainConfig.getInt("game-settings.game-time.3", 60);
        int game6Time = mainConfig.getInt("game-settings.game-time.6", 60);
        int game7Time = mainConfig.getInt("game-settings.game-time.7", 60);

        if (game1Time > 0)
            this.games.add(new G1RedGreenLightGame(this, game1Time));
        if (game3Time > 0)
            this.games.add(new G3BattleGame(this, game3Time));
        if (game6Time > 0)
            this.games.add(new G6GlassesGame(this, game6Time));
        if (game7Time > 0)
            this.games.add(new G7SquidGame(this, game7Time));
    }

    public Configuration getMainConfig() {
        return this.mainConfig;
    }

    public void broadcastPotionEffect(PotionEffect e) {
        for (SquidPlayer player : this.getPlayers()) {
            player.getBukkitPlayer().addPotionEffect(e);
        }
    }

    public void broadcastRemovePotionEffect(PotionEffectType e) {
        for (SquidPlayer player : this.getPlayers()) {
            player.getBukkitPlayer().removePotionEffect(e);
        }
    }

    public void broadcastMessage(String message) {
        for (SquidPlayer player : this.getAllPlayers()) {
            player.sendMessageI18n(message);
        }
    }

    public void broadcastSound(Sound sound) {
        for (SquidPlayer player : this.getAllPlayers()) {
            player.playSound(sound);
        }
    }

    public void broadcastTitle(String title, String subtitle) {
        for (SquidPlayer player : this.getAllPlayers()) {
            player.sendTitleI18n(title, subtitle, 2);
        }
    }

    public void broadcastScoreboard(String scoreboardKey) {
        for (SquidPlayer player : this.getAllPlayers()) {
            player.sendScoreboardI18n(scoreboardKey);
        }
    }

    public int getMinPlayers() {
        return this.mainConfig.getInt("game-settings.min-players");
    }

    public int getMaxPlayers() {
        return this.mainConfig.getInt("game-settings.max-players");
    }

    public Configuration getConfig() {
        return this.arenaConfig;
    }

    public Location getSpawnPosition() {
        if (this.getState() == ArenaState.INTERMISSION || this.getState() == ArenaState.FINISHING_ARENA) {
            Location loc = this.arenaConfig.getLocation("arena.waiting_room", false);
            loc.setWorld(this.world);
            return loc;
        } else if (this.getCurrentGame() != null) {
            return this.getCurrentGame().getSpawnPosition();
        } else {
            Location loc = this.arenaConfig.getLocation("arena.prelobby", false);
            loc.setWorld(this.world);
            return loc;
        }
    }

    public void teleportAllPlayers(Location location) {
        for (SquidPlayer player : this.getAllPlayers()) {
            player.teleport(location);
        }
    }

    public void finishArena(ArenaFinishReason reason) {
        if (this.currentGame != null) {
            this.currentGame.onStop();
        }

        this.handler.handleArenaFinish(reason);
        this.setState(ArenaState.FINISHING_ARENA);
        this.teleportAllPlayers(this.getSpawnPosition());
    }

    public Arena addPlayer(SquidPlayer player) {
        if (!this.players.contains(player) && !this.spectators.contains(player)) {
            this.joined = player.getBukkitPlayer().getName();
            this.players.add(player);
            player.getBukkitPlayer().teleport(this.getSpawnPosition());
            player.getBukkitPlayer().setFoodLevel(20);
            player.getBukkitPlayer().setHealth(20);
            player.setArena(this);
            this.handler.handlePlayerJoin(player);
        }

        return this;
    }

    public void forceStart() {
        this.handler.handleArenaStart();
    }

    public boolean isAlive(SquidPlayer player) {
        return this.players.contains(player);
    }

    public void killAllPlayers() {
        List<SquidPlayer> list = new ArrayList<>(this.players);

        for (SquidPlayer player : list) {
            this.death = player.getBukkitPlayer().getName();
            this.broadcastSound(this.getMainConfig().getSound("game-settings.sounds.player-death", "EXPLODE"));
            this.broadcastMessage("arena.death");
        }

        this.finishArena(ArenaFinishReason.ALL_PLAYERS_DEATH);
    }

    public void killPlayer(SquidPlayer player, boolean setSpectator) {
        if (setSpectator) {
            this.addSpectator(player);
        }

        this.death = player.getBukkitPlayer().getName();
        this.broadcastSound(this.getMainConfig().getSound("game-settings.sounds.player-death", "EXPLODE"));
        this.broadcastMessage("arena.death");

        if (this.isAllPlayersDeath()) {
            this.finishArena(ArenaFinishReason.ALL_PLAYERS_DEATH);
        }

        else if (this.calculateWinner() != null) {
            boolean allowVictory = this.mainConfig
                    .getBoolean("game-settings.allow-victory-before-completing-game", false);
            boolean isLastGame = this.currentGame != null && this.currentGame instanceof G7SquidGame;

            if (isLastGame || allowVictory) {
                this.finishArena(ArenaFinishReason.ONE_PLAYER_IN_ARENA);
            }
        }
    }

    public void killPlayer(SquidPlayer player) {
        this.killPlayer(player, true);
    }

    public Arena addSpectator(SquidPlayer player) {
        if (!this.spectators.contains(player)) {
            if (this.players.contains(player)) {
                this.players.remove(player);
                this.spectators.add(player);
                player.setSpectator(true);
                player.setArena(this);
            }
        }

        return this;
    }

    public void removePlayer(SquidPlayer player) {
        if (this.players.contains(player)) {
            this.leaved = player.getBukkitPlayer().getName();
            this.players.remove(player);
            this.handler.handlePlayerLeave(player);

            if (this.getState() != ArenaState.WAITING && this.getState() != ArenaState.STARTING
                    && this.getState() != ArenaState.FINISHING_ARENA) {
                this.killPlayer(player);
            }
        } else if (this.spectators.contains(player)) {
            this.spectators.remove(player);
            player.setSpectator(false);
        } else {
            return;
        }

        player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
        player.teleportToLobby();
        player.sendScoreboardI18n("lobby");
        player.setArena(null);
    }

    public List<SquidPlayer> getAllPlayers() {
        List<SquidPlayer> result = new ArrayList<>(this.getPlayers());
        result.addAll(this.getSpectators());
        return result;
    }

    public List<SquidPlayer> getPlayers() {
        return this.players;
    }

    public List<SquidPlayer> getSpectators() {
        return this.spectators;
    }

    public ArenaState getState() {
        return this.state;
    }

    public World getWorld() {
        return this.world;
    }

    public ArenaGameBase getCurrentGame() {
        return this.currentGame;
    }

    public String getName() {
        return this.name;
    }

    public int getInternalTime() {
        return this.internalTime;
    }

    public void setState(ArenaState newState) {
        this.state = newState;
        this.handler.handleArenaSwitchState();
    }

    public void setInternalTime(int time) {
        if (time >= 0) {
            this.internalTime = time;
        }
    }

    public String getJoinedPlayer() {
        return this.joined;
    }

    public String getLeavedPlayer() {
        return this.leaved;
    }

    public String getDeathPlayer() {
        return this.death;
    }

    public SquidPlayer calculateWinner() {
        if (this.players.size() == 1) {
            return this.players.get(0);
        } else {
            return null;
        }
    }

    public boolean isAllPlayersDeath() {
        return this.players.isEmpty();
    }

    public boolean isPvPAllowed() {
        return this.allowPvP;
    }

    public void setPvPAllowed(boolean result) {
        this.allowPvP = result;
    }

    public void doArenaTick() {
        if (this.internalTime >= 0) {
            this.internalTime--;
            this.handler.handleArenaTick();
        }
    }

    public void nextGame() {
        if (this.calculateWinner() != null) {
            this.finishArena(ArenaFinishReason.ONE_PLAYER_IN_ARENA);
            return;
        } else if (this.currentGame != null) {
            this.currentGame.onStop();
        }

        ArenaGameBase nextGame = this.games.get(0);
        this.currentGame = nextGame;
        this.games.remove(nextGame);

        this.setState(ArenaState.INTERMISSION);
        this.teleportAllPlayers(this.getSpawnPosition());

        this.setInternalTime(5);
        this.broadcastTitle("events.intermission.title", "events.intermission.subtitle");
    }

    public void giveItem(ItemStack itemStack) {
        for (SquidPlayer player : this.getAllPlayers()) {
            player.getBukkitPlayer().getInventory().addItem(itemStack);
        }
    }
}
