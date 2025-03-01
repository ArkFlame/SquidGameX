package com.arkflame.squidgame.gui;

import java.io.IOException;

import org.bukkit.entity.Player;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.gui.InventoryGUI;
import com.arkflame.squidgame.player.PlayerManager;
import com.arkflame.squidgame.player.PlayerWand;
import com.arkflame.squidgame.player.SquidPlayer;
import com.arkflame.squidgame.utils.Materials;

public class EditArenaGame1GUI extends InventoryGUI {

    private Arena arena;

    public EditArenaGame1GUI(Arena arena, InventoryGUI prevGui) {
        super("§d§lArena §f" + arena.getName(), 45, prevGui);
        this.arena = arena;
    }

    @Override
    public void init() {
        this.addItem(0, this.createItem("§eSpawn point", Materials.get("COMPASS"), "§r\n§7Set at your current location\n§r"), 2,
                2);
        this.addItem(1, this.createItem("§eBarrier", Materials.get("BEDROCK"), "§r\n§7Set with your location wand\n§r"), 4, 2);
        this.addItem(2, this.createItem("§eKill Zone", Materials.get("ENDER_PEARL"), "§r\n§7Set with your location wand\n§r"),
                6, 2);
        this.addItem(3, this.createItem("§eGoal", Materials.get("ARMOR_STAND"), "§r\n§7Set with your location wand\n§r"), 8, 2);

        this.addItem(99, this.createItem("§cBack", Materials.get("BARRIER")), 5, 4);
    }

    @Override
    public void handle(int id, Player player) {
        PlayerManager pm = SquidGame.getInstance().getPlayerManager();
        SquidPlayer squidPlayer = pm.getPlayer(player);
        PlayerWand wand = squidPlayer.getWand();

        if (id == 99) {
            this.back(player);
            return;
        } else if (id == 0) {
            this.arena.getConfig().setLocation("games.first.spawn", player.getLocation(), false);
            player.sendMessage("§eFirst game spawn§a set in your current location.");
        } else {
            String key = "games.first";

            switch (id) {
            case 1:
                key += ".barrier";
                break;
            case 2:
                key += ".killzone";
                break;
            case 3:
                key += ".goal";
                break;
            }

            if (wand == null) {
                player.sendMessage("§cYou don't have an region wand, use /squid wand to get it.");
            } else if (!wand.isComplete()) {
                player.sendMessage("§cYou need to set area with your region wand first.");
            } else {
                this.arena.getConfig().setCuboid(key, wand.getCuboid());
                player.sendMessage("§eFirst game " + key + "§a set with your location wand §7("
                        + wand.getFirstPoint().toString() + ") (" + wand.getSecondPoint().toString() + ")");
            }
        }

        try {
            this.arena.getConfig().save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.close(player);
    }
}
