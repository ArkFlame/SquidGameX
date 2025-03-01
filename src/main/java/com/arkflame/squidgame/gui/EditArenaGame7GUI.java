package com.arkflame.squidgame.gui;

import java.io.IOException;

import org.bukkit.entity.Player;

import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.gui.InventoryGUI;
import com.arkflame.squidgame.utils.Materials;

public class EditArenaGame7GUI extends InventoryGUI {

    private Arena arena;

    public EditArenaGame7GUI(Arena arena, InventoryGUI prevGui) {
        super("§d§lArena §f" + arena.getName(), 45, prevGui);
        this.arena = arena;
    }

    @Override
    public void init() {
        this.addItem(0, this.createItem("§eSpawn point", Materials.get("COMPASS"), "§r\n§7Set at your current location\n§r"), 5,
                2);

        this.addItem(99, this.createItem("§cBack", Materials.get("BARRIER")), 5, 4);
    }

    @Override
    public void handle(int id, Player player) {
        if (id == 99) {
            this.back(player);
            return;
        } else if (id == 0) {
            this.arena.getConfig().setLocation("games.seventh.spawn", player.getLocation(), false);
            player.sendMessage("§eSeventh game spawn §aset in your current location.");
        }

        try {
            this.arena.getConfig().save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.close(player);
    }
}
