package org.auioc.mcmod.addrlimiter.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import static org.auioc.mcmod.addrlimiter.server.command.ALCommandReferences.MSGH;
import org.auioc.mcmod.addrlimiter.server.address.AddressHandler;
import org.auioc.mcmod.arnicalib.game.command.CommandSourceUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;


public class SwitchCommand {

    public static final CommandNode<CommandSourceStack> NODE_ENABLE = literal("enable").requires(CommandSourceUtils.PERMISSION_LEVEL_4).executes(ctx -> SwitchCommand.switchStatus(ctx, true)).build();
    public static final CommandNode<CommandSourceStack> NODE_DISABLE = literal("disable").requires(CommandSourceUtils.PERMISSION_LEVEL_4).executes(ctx -> SwitchCommand.switchStatus(ctx, false)).build();

    private static final SimpleCommandExceptionType ALREADY_ENABLED = new SimpleCommandExceptionType(MSGH.create("already_enabled", true));
    private static final SimpleCommandExceptionType ALREADY_DISABLED = new SimpleCommandExceptionType(MSGH.create("already_disabled", true));

    private static final int switchStatus(CommandContext<CommandSourceStack> ctx, boolean status) throws CommandSyntaxException {
        if (status) {
            if (AddressHandler.isEnabled()) throw ALREADY_ENABLED.create();
            AddressHandler.enable();
            ctx.getSource().sendSuccess(MSGH.create("enable.success", true), true);
        } else {
            if (!AddressHandler.isEnabled()) throw ALREADY_DISABLED.create();
            AddressHandler.disable();
            ctx.getSource().sendSuccess(MSGH.create("disable.success", true), true);
        }
        return Command.SINGLE_SUCCESS;
    }

}
