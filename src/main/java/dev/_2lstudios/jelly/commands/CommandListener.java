package dev._2lstudios.jelly.commands;

import java.util.ArrayList;
import java.util.List;

import dev._2lstudios.jelly.annotations.Command;

public abstract class CommandListener {

    private List<CommandListener> subcommands = new ArrayList<>();

    public void addSubcommand(CommandListener obj) {
        this.subcommands.add(obj);
    }

    public CommandListener getSubcommand(String name) {
        for (CommandListener subcommand : this.subcommands) {
            Command cmd = subcommand.getClass().getAnnotation(Command.class);
            if (cmd.name().equalsIgnoreCase(name)) {
                return subcommand;
            }
        }

        return null;
    }

    public String mapCommandListToString(String format, String separator) {
        String output = null;

        for (CommandListener cmd : this.subcommands) {
            Command cmdInfo = cmd.getClass().getAnnotation(Command.class);
            String entry = format.replace("{name}", cmdInfo.name()).replace("{permission}", cmdInfo.permission())
                    .replace("{usage}", cmdInfo.usage()).replace("{description}", cmdInfo.description());

            if (output == null) {
                output = entry;
            } else {
                output += separator + entry;
            }
        }

        return output;
    }

    public abstract void handle(CommandContext context) throws Exception;
}
