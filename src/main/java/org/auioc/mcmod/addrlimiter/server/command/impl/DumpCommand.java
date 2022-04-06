package org.auioc.mcmod.addrlimiter.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import static org.auioc.mcmod.addrlimiter.AddrLimiter.LOGGER;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mcmod.addrlimiter.server.address.AddressHandler;
import org.auioc.mcmod.addrlimiter.server.address.AddressManager;
import org.auioc.mcmod.addrlimiter.server.command.CommandReference;
import org.auioc.mcmod.arnicalib.utils.game.CommandUtils;
import org.auioc.mcmod.arnicalib.utils.java.FileUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class DumpCommand {

    public static final CommandNode<CommandSourceStack> NODE =
        literal("dump")
            .requires(source -> source.hasPermission(4))
            .then(literal("list").executes(DumpCommand::dumpAsFriendlyList))
            .then(literal("json").executes(DumpCommand::dumpAsJson))
            .then(
                literal("file")
                    .requires(source -> (source.getEntity() == null))
                    .executes(DumpCommand::dumpToFile)
            )
            .build();

    private static int dumpAsJson(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddressHandler.isEnabled()) {
            throw CommandReference.NOT_ENABLED.create();
        }

        AddressManager limiter = AddressHandler.getLimiter();

        CommandSourceStack source = ctx.getSource();
        if (source.getEntity() instanceof ServerPlayer) {
            source.sendSuccess(CommandReference.message(limiter.toJsonText()), false);
        } else {
            LOGGER.info(limiter.toJsonString());
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int dumpToFile(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddressHandler.isEnabled()) {
            throw CommandReference.NOT_ENABLED.create();
        }

        try {
            FileUtils.writeText("dumps/addrlimiter.json", AddressHandler.getLimiter().toJsonString());
            ctx.getSource().sendSuccess(CommandReference.message("dump.success", "dumps/addrlimiter.json"), false);
        } catch (Exception e) {
            LOGGER.error(e);
            throw CommandUtils.INTERNAL_ERROR.create();
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int dumpAsFriendlyList(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddressHandler.isEnabled()) {
            throw CommandReference.NOT_ENABLED.create();
        }

        AddressManager limiter = AddressHandler.getLimiter();

        CommandSourceStack source = ctx.getSource();
        if (source.getEntity() instanceof ServerPlayer) {
            source.sendSuccess(limiter.toChatMessage(), false);
        } else {
            LOGGER.info(limiter.toChatMessage().getString());
        }

        return Command.SINGLE_SUCCESS;
    }

}
