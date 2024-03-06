package com.huskydreaming.bouncyball.registries;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.commands.base.CommandExecutor;
import com.huskydreaming.bouncyball.commands.base.CommandInterface;
import com.huskydreaming.bouncyball.commands.subcommands.CreateCommand;
import com.huskydreaming.bouncyball.commands.subcommands.GiveCommand;
import com.huskydreaming.bouncyball.commands.subcommands.ReloadCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CommandRegistry implements Registry {

    private final Set<CommandInterface> commands = new HashSet<>();

    public Set<CommandInterface> getCommands() {
        return Collections.unmodifiableSet(commands);
    }

    @Override
    public void register(BouncyBallPlugin plugin) {
        CommandExecutor commandExecutor = new CommandExecutor(plugin);

        commands.add(new CreateCommand(plugin));
        commands.add(new GiveCommand(plugin));
        commands.add(new ReloadCommand(plugin));

        try {
            Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap commandMap = (CommandMap) field.get(server);
            commandMap.register("bouncyballs", commandExecutor);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}