package org.auioc.mcmod.addrlimiter.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import static org.auioc.mcmod.addrlimiter.server.command.ALCommandReferences.CFH;
import org.auioc.mcmod.addrlimiter.server.address.AddressHandler;
import org.auioc.mcmod.addrlimiter.server.command.ALCommandReferences;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;

public class RefreshCommand {

    public static final CommandNode<CommandSourceStack> NODE =
        literal("refresh")
            .requires(source -> source.hasPermission(4))
            .executes(RefreshCommand::refresh)
            .build();

    private static final int refresh(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddressHandler.isEnabled()) throw ALCommandReferences.NOT_ENABLED_ERROR.create();

        CFH.sendSuccessAndBoardcast(ctx, "refresh.start");
        AddressHandler.disable();
        AddressHandler.enable();
        CFH.sendSuccessAndBoardcast(ctx, "refresh");

        return Command.SINGLE_SUCCESS;
    }

}
