package org.auioc.mods.addrlimiter.server.command;

import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.addrlimiter.AddrLimiter;
import org.auioc.mods.addrlimiter.server.command.impl.DumpCommand;
import org.auioc.mods.addrlimiter.server.command.impl.RefreshCommand;
import org.auioc.mods.addrlimiter.server.command.impl.SwitchCommand;
import org.auioc.mods.arnicalib.common.command.impl.VersionCommand;
import org.auioc.mods.arnicalib.server.command.AHServerCommands;
import net.minecraft.commands.CommandSourceStack;

public final class ServerCommandRegistry {

    public static final CommandNode<CommandSourceStack> NODE = literal(AddrLimiter.MOD_ID).build();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        NODE.addChild(literal("version").executes((ctx) -> VersionCommand.getModVersion(ctx, AddrLimiter.MAIN_VERSION, AddrLimiter.FULL_VERSION, AddrLimiter.MOD_NAME)).build());

        NODE.addChild(SwitchCommand.NODE_DISABLE);
        NODE.addChild(SwitchCommand.NODE_ENABLE);
        NODE.addChild(RefreshCommand.NODE);
        NODE.addChild(DumpCommand.NODE);

        AHServerCommands.getRootNode(dispatcher).addChild(NODE);
    }

}
