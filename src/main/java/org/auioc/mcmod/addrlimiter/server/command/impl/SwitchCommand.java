package org.auioc.mcmod.addrlimiter.server.command.impl;

import static net.minecraft.commands.Commands.literal;
import static org.auioc.mcmod.addrlimiter.server.command.ALCommandReferences.CFH;
import org.auioc.mcmod.addrlimiter.server.address.AddressHandler;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;


public class SwitchCommand {

    public static final CommandNode<CommandSourceStack> NODE_ENABLE = literal("enable").requires(source -> source.hasPermission(4)).executes(ctx -> SwitchCommand.switchStatus(ctx, true)).build();
    public static final CommandNode<CommandSourceStack> NODE_DISABLE = literal("disable").requires(source -> source.hasPermission(4)).executes(ctx -> SwitchCommand.switchStatus(ctx, false)).build();

    private static final SimpleCommandExceptionType ALREADY_ENABLED = new SimpleCommandExceptionType(CFH.createMessage("already_enabled"));
    private static final SimpleCommandExceptionType ALREADY_DISABLED = new SimpleCommandExceptionType(CFH.createMessage("already_disabled"));

    private static final int switchStatus(CommandContext<CommandSourceStack> ctx, boolean status) throws CommandSyntaxException {
        if (status) {
            if (AddressHandler.isEnabled()) {
                throw ALREADY_ENABLED.create();
            }
            AddressHandler.enable();
            return CFH.sendSuccessAndBoardcast(ctx, "enable");
        } else {
            if (!AddressHandler.isEnabled()) {
                throw ALREADY_DISABLED.create();
            }
            AddressHandler.disable();
            return CFH.sendSuccessAndBoardcast(ctx, "disable");
        }
    }

}
