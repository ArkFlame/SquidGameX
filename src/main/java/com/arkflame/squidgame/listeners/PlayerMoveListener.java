package com.arkflame.squidgame.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.arena.ArenaState;
import com.arkflame.squidgame.arena.games.G1RedGreenLightGame;
import com.arkflame.squidgame.arena.games.G6GlassesGame;
import com.arkflame.squidgame.arena.games.G7SquidGame;
import com.arkflame.squidgame.jelly.math.Vector3;
import com.arkflame.squidgame.jelly.utils.BlockUtils;
import com.arkflame.squidgame.player.SquidPlayer;
import com.arkflame.squidgame.utils.Materials;

public class PlayerMoveListener implements Listener {

    private SquidGame plugin;

    public PlayerMoveListener(SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().distance(e.getTo()) <= 0.015) {
            return;
        }

        SquidPlayer player = this.plugin.getPlayerManager().getPlayer(e.getPlayer());
        Arena arena = player.getArena();

        if (arena == null || player.isSpectator()) {
            return;
        }

        /* Game 1: Handling */
        if (arena.getCurrentGame() instanceof G1RedGreenLightGame) {
            G1RedGreenLightGame game = (G1RedGreenLightGame) arena.getCurrentGame();

            if (arena.getState() == ArenaState.EXPLAIN_GAME) {
                if (game.getBarrier().isBetween(e.getTo())) {
                    e.setCancelled(true);
                    e.setTo(e.getFrom());
                }
            }

            else if (arena.getState() == ArenaState.IN_GAME) {
                if (!game.isCanWalk()) {
                    Vector3 playerPosition = new Vector3(e.getTo().getX(), e.getTo().getY(), e.getTo().getZ());
                    if (game.getKillZone().isBetween(playerPosition)) {
                        arena.killPlayer(player);
                    }
                }
            }
        }

        /* Game 6: Handling */
        else if (arena.getCurrentGame() instanceof G6GlassesGame) {
            Location loc = e.getTo().clone().subtract(0, 1, 0);
            Block block = loc.getBlock();

            if (block != null && block.getType() == Materials.get("GLASS")) {
                G6GlassesGame game = (G6GlassesGame) arena.getCurrentGame();

                if (game.isFakeBlock(loc.getBlock())) {
                    BlockUtils.destroyBlockGroup(loc.getBlock());
                    arena.broadcastSound(
                            this.plugin.getMainConfig().getSound("game-settings.sounds.glass-break", "GLASS"));

                    // Take the player down one block
                    player.teleport(player.getLocation().subtract(0, 1, 0));

                    // Schedule death of the player that walked on the fake block
                    this.plugin.runTaskLater(() -> {
                        if (arena.isAlive(player)) {
                            arena.killPlayer(player);
                        }
                    }, this.plugin.getMainConfig().getInt("game-settings.g6-glass-break-killing-ticks"));
                }
            }
        }

        /* Game 7: Handling */
        else if (arena.getCurrentGame() instanceof G7SquidGame) {
            Location loc = e.getTo().clone();
            String killBlock = arena.getConfig().getString("games.seventh.kill-block", "sand");

            loc.subtract(0, 1, 0);

            if (loc.getBlock() != null && loc.getBlock().getType() != null
                    && loc.getBlock().getType().toString().equalsIgnoreCase(killBlock)) {
                arena.killPlayer(player);
            }
        }
    }
}
