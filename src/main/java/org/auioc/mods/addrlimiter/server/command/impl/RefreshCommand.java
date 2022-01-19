package org.auioc.mods.addrlimiter.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.addrlimiter.server.address.AddressHandler;
import org.auioc.mods.addrlimiter.server.command.CommandReference;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;

public class RefreshCommand {

    public static final CommandNode<CommandSourceStack> NODE =
        literal("refresh")
            .requires(source -> source.hasPermission(4))
            .executes(RefreshCommand::refresh)
            .build();

    private static final int refresh(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!AddressHandler.isEnabled()) {
            throw CommandReference.NOT_ENABLED.create();
        }

        CommandSourceStack source = ctx.getSource();

        source.sendSuccess(CommandReference.message("refresh.start"), true);
        AddressHandler.disable();
        AddressHandler.enable();
        source.sendSuccess(CommandReference.message("refresh.success").withStyle(ChatFormatting.GREEN), true);

        return Command.SINGLE_SUCCESS;
    }

}
