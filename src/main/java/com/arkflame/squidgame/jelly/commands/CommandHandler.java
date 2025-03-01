package com.arkflame.squidgame.jelly.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.arkflame.squidgame.jelly.JellyPlugin;
import com.arkflame.squidgame.jelly.annotations.Command;
import com.arkflame.squidgame.jelly.errors.CommandException;
import com.arkflame.squidgame.jelly.errors.I18nCommandException;
import com.arkflame.squidgame.jelly.utils.ArrayUtils;
import com.arkflame.squidgame.player.SquidPlayer;

public class CommandHandler implements CommandExecutor {

    private Map<String, CommandListener> commands;
    private JellyPlugin plugin;

    public CommandHandler(JellyPlugin plugin) {
        this.commands = new HashMap<>();
        this.plugin = plugin;
    }

    public void addCommand(CommandListener listener) {
        if (listener.getClass().isAnnotationPresent(Command.class)) {
            Command command = listener.getClass().getAnnotation(Command.class);
            this.commands.put(command.name(), listener);
            this.plugin.getCommand(command.name()).setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmdInfo, String label, String[] args) {
        String name = cmdInfo.getName().toLowerCase();
        CommandListener listener = this.commands.get(name);

        while (args.length > 0) {
            CommandListener subCommand = listener.getSubcommand(args[0]);
            if (subCommand != null) {
                listener = subCommand;
                args = ArrayUtils.shift(args, 0);
            } else {
                break;
            }
        }

        Command command = listener.getClass().getAnnotation(Command.class);

        // Check execution target
        if (sender instanceof Player && command.target() == CommandExecutionTarget.ONLY_CONSOLE) {
            sender.sendMessage("§cThis command can run only in console.");
            return true;
        } else if (sender instanceof ConsoleCommandSender && command.target() == CommandExecutionTarget.ONLY_PLAYER) {
            sender.sendMessage("§cThis command can run only by a player.");
            return true;
        }

        // Check for permission
        if (!command.permission().isEmpty() && !sender.hasPermission(command.permission())) {
            sender.sendMessage("§cMissing permission " + command.permission());
            return true;
        }

        // Check for min arguments
        int minArguments = command.minArguments() == Integer.MIN_VALUE ? command.arguments().length
                : command.minArguments();

        if (args.length < minArguments) {
            sender.sendMessage(command.usage());
            return true;
        }

        // Parse arguments
        Object[] argList = new Object[args.length];
        int argumentDefinedLength = command.arguments().length;

        for (int i = 0; i < args.length; i++) {
            if (argumentDefinedLength >= (i + 1)) {
                Class<?> clazz = command.arguments()[i];
                try {
                    Object arg = CommandArgumentParser.parse(clazz, i + 1, args[i]);
                    argList[i] = arg;
                } catch (Exception e) {
                    sender.sendMessage("§cUsage: " + command.usage());
                    sender.sendMessage("§c" + e.getMessage());
                    return true;
                }
            } else {
                argList[i] = args[i];
            }
        }

        // Execute command
        CommandArguments arguments = new CommandArguments(argList);
        CommandContext context = new CommandContext(this.plugin, sender, arguments);

        try {
            listener.handle(context);
        } catch (Exception e) {
            if (e instanceof I18nCommandException && sender instanceof Player) {
                SquidPlayer player = (SquidPlayer) context.getPluginPlayer();
                I18nCommandException i18nE = (I18nCommandException) e;
                player.sendMessage(i18nE.getKey());
            } else if (e instanceof CommandException) {
                sender.sendMessage("§c" + e.getMessage());
            } else {
                sender.sendMessage("§cFatal exception ocurred while executing command. See console for more details.");
                e.printStackTrace();
            }
        }

        return true;
    }

}
