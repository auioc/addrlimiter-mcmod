package org.auioc.mods.addrlimiter;

import org.auioc.mods.addrlimiter.server.event.ServerEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public final class Initialization {

    public static void init() {
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(ServerEventHandler.class);
    }

}
