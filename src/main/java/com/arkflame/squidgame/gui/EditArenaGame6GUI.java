package com.arkflame.squidgame.gui;

import java.io.IOException;

import org.bukkit.entity.Player;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.gui.InventoryGUI;
import com.arkflame.squidgame.player.PlayerWand;
import com.arkflame.squidgame.player.SquidPlayer;
import com.arkflame.squidgame.utils.Materials;

public class EditArenaGame6GUI extends InventoryGUI {

    private Arena arena;

    public EditArenaGame6GUI(Arena arena, InventoryGUI prevGui) {
        super("§d§lArena §f" + arena.getName(), 45, prevGui);
        this.arena = arena;
    }

    @Override
    public void init() {
        this.addItem(0, this.createItem("§eSpawn point", Materials.get("COMPASS"), "§r\n§7Set at your current location\n§r"), 3,
                2);
        this.addItem(1, this.createItem("§eGlass Zone", Materials.get("GLASS"), "§r\n§7Set with your location wand\n§r"), 5, 2);
        this.addItem(2, this.createItem("§eGoal Zone", Materials.get("ARMOR_STAND"), "§r\n§7Set with your location wand\n§r"),
                7, 2);

        this.addItem(99, this.createItem("§cBack", Materials.get("BARRIER")), 5, 4);
    }

    @Override
    public void handle(int id, Player player) {
        if (id == 99) {
            this.back(player);
            return;
        } else if (id == 0) {
            this.arena.getConfig().setLocation("games.sixth.spawn", player.getLocation(), false);
            player.sendMessage("§eSixth game spawn §aset in your current location.");
        }

        else if (id == 1 || id == 2) {
            SquidPlayer squidPlayer = SquidGame.getInstance().getPlayerManager().getPlayer(player);
            PlayerWand wand = squidPlayer.getWand();

            if (wand == null) {
                player.sendMessage("§cYou don't have an region wand, use /squid wand to get it.");
            } else if (!wand.isComplete()) {
                player.sendMessage("§cYou need to set area with your region wand first.");
            } else {

                if (id == 1) {
                    this.arena.getConfig().setCuboid("games.sixth.glass", wand.getCuboid());
                    player.sendMessage("§eSixth game glass zone§a set with your location wand §7("
                            + wand.getFirstPoint().toString() + ") (" + wand.getSecondPoint().toString() + ")");
                }

                else if (id == 2) {
                    this.arena.getConfig().setCuboid("games.sixth.goal", wand.getCuboid());
                    player.sendMessage("§eSixth game goal zone§a set with your location wand §7("
                            + wand.getFirstPoint().toString() + ") (" + wand.getSecondPoint().toString() + ")");
                }
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
