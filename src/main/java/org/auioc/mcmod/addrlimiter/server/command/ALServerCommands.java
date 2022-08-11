package org.auioc.mcmod.addrlimiter.server.command;

import static net.minecraft.commands.Commands.literal;
import org.auioc.mcmod.addrlimiter.AddrLimiter;
import org.auioc.mcmod.addrlimiter.server.command.impl.DumpCommand;
import org.auioc.mcmod.addrlimiter.server.command.impl.RefreshCommand;
import org.auioc.mcmod.addrlimiter.server.command.impl.SwitchCommand;
import org.auioc.mcmod.arnicalib.common.command.impl.VersionCommand;
import org.auioc.mcmod.arnicalib.server.command.AHServerCommands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;

public final class ALServerCommands {

    public static final CommandNode<CommandSourceStack> NODE = literal(AddrLimiter.MOD_ID).build();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        VersionCommand.addVersionNode(NODE, AddrLimiter.class);

        NODE.addChild(SwitchCommand.NODE_DISABLE);
        NODE.addChild(SwitchCommand.NODE_ENABLE);
        NODE.addChild(RefreshCommand.NODE);
        NODE.addChild(DumpCommand.NODE);

        AHServerCommands.getAHNode(dispatcher).addChild(NODE);
        dispatcher.register(literal(AddrLimiter.MOD_ID).redirect(NODE));
    }

}
