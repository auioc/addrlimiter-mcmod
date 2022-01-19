package org.auioc.mods.addrlimiter.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.addrlimiter.AddrLimiter.LOGGER;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.addrlimiter.server.address.AddressHandler;
import org.auioc.mods.addrlimiter.server.address.AddressManager;
import org.auioc.mods.addrlimiter.server.command.CommandReference;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class DumpCommand {

    public static final CommandNode<CommandSourceStack> NODE =
        literal("dump")
            .requires(source -> source.hasPermission(3))
            .then(literal("json").executes(DumpCommand::dumpJson))
            .build();

    private static int dumpJson(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddressHandler.isEnabled()) {
            throw CommandReference.NOT_ENABLED.create();
        }

        AddressManager limiter = AddressHandler.getLimiter();

        CommandSourceStack source = ctx.getSource();
        if (source.getEntity() instanceof ServerPlayer) {
            source.sendSuccess((CommandReference.prefix()).append(limiter.toJsonText()), false);
        } else {
            LOGGER.info(limiter.toJsonString());
        }

        return Command.SINGLE_SUCCESS;
    }

}
