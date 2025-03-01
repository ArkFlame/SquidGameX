package com.arkflame.squidgame.listeners;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.player.SquidPlayer;

import dev._2lstudios.jelly.utils.ObjectUtils;

public class AsyncPlayerChatListener implements Listener {

    private SquidGame plugin;

    public AsyncPlayerChatListener(SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (this.plugin.getMainConfig().getBoolean("game-settings.per-arena-chat", true)) {
            SquidPlayer thisPlayer = this.plugin.getPlayerManager().getPlayer(e.getPlayer());
            Iterator<Player> players = e.getRecipients().iterator();

            if (thisPlayer == null) {
                return;
            }

            while (players.hasNext()) {
                SquidPlayer recipient = this.plugin.getPlayerManager().getPlayer(players.next());

                if (!ObjectUtils.checkEquals(recipient.getArena(), thisPlayer.getArena())) {
                    players.remove();
                }
            }
        }
    }
}
