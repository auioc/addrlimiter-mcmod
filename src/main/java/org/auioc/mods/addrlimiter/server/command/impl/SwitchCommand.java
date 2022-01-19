package org.auioc.mods.addrlimiter.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.addrlimiter.server.address.AddressHandler;
import org.auioc.mods.addrlimiter.server.command.CommandReference;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;

public class SwitchCommand {

    public static final CommandNode<CommandSourceStack> NODE_ENABLE = literal("enable").requires(source -> source.hasPermission(4)).executes(ctx -> SwitchCommand.switchStatus(ctx, true)).build();
    public static final CommandNode<CommandSourceStack> NODE_DISABLE = literal("disable").requires(source -> source.hasPermission(4)).executes(ctx -> SwitchCommand.switchStatus(ctx, false)).build();

    private static final SimpleCommandExceptionType ALREADY_ENABLED = new SimpleCommandExceptionType(CommandReference.text("already_enabled"));
    private static final SimpleCommandExceptionType ALREADY_DISABLED = new SimpleCommandExceptionType(CommandReference.text("already_disabled"));

    private static final int switchStatus(CommandContext<CommandSourceStack> ctx, boolean status) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();

        MutableComponent message;

        if (status) {
            if (AddressHandler.isEnabled()) {
                throw ALREADY_ENABLED.create();
            }
            AddressHandler.enable();
            message = CommandReference.message("enable");
        } else {
            if (!AddressHandler.isEnabled()) {
                throw ALREADY_DISABLED.create();
            }
            AddressHandler.disable();
            message = CommandReference.message("disable");
        }

        source.sendSuccess(message, true);

        return Command.SINGLE_SUCCESS;
    }

}
