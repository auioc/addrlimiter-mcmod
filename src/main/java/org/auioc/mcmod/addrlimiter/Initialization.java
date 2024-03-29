package org.auioc.mcmod.addrlimiter;

import org.auioc.mcmod.addrlimiter.server.config.ALConfig;
import org.auioc.mcmod.addrlimiter.server.event.ALServerEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;

public final class Initialization {

    public static void init() {
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (s, b) -> true));

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ALConfig.CONFIG);

        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(ALServerEventHandler.class);
    }

}
