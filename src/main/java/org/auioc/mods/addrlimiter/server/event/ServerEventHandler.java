package org.auioc.mods.addrlimiter.server.event;

import org.auioc.mods.addrlimiter.server.command.ServerCommandRegistry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ServerEventHandler {

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        ServerCommandRegistry.register(event.getDispatcher());
    }

}
