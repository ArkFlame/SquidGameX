package com.arkflame.squidgame.gui;

import java.io.IOException;

import org.bukkit.entity.Player;

import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.gui.InventoryGUI;
import com.arkflame.squidgame.utils.Materials;

public class EditArenaWaitingLobbyGUI extends InventoryGUI {

    private Arena arena;

    public EditArenaWaitingLobbyGUI(Arena arena, InventoryGUI prevGui) {
        super("§d§lArena §f" + arena.getName(), 36, prevGui);
        this.arena = arena;
    }

    @Override
    public void init() {
        this.addItem(0, this.createItem("§eWaiting Lobby Spawn", Materials.get("COMPASS")), 4, 2);
        this.addItem(1, this.createItem("§eSet arena World", Materials.get("GRASS", "GRASS_BLOCK")), 6, 2);
        this.addItem(99, this.createItem("§cBack", Materials.get("BARRIER")), 5, 4);
    }

    @Override
    public void handle(int id, Player player) {
        switch (id) {
        case 0:
            this.arena.getConfig().setLocation("arena.prelobby", player.getLocation(), false);
            this.arena.getConfig().setLocation("arena.waiting_room", player.getLocation(), false);
            player.sendMessage("§eWaiting room spawn§a has set to your current position.");
            break;
        case 1:
            this.arena.getConfig().set("arena.world", player.getWorld().getName());
            player.sendMessage("§eArena map §a set in your current world.");
            break;
        case 99:
            this.back(player);
            return;
        }

        try {
            this.arena.getConfig().save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.close(player);
    }
}
