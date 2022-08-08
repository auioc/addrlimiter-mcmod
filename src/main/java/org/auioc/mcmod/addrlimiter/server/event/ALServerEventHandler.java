package org.auioc.mcmod.addrlimiter.server.event;

import org.auioc.mcmod.addrlimiter.server.address.AddressHandler;
import org.auioc.mcmod.addrlimiter.server.command.ALServerCommands;
import org.auioc.mcmod.arnicalib.server.event.impl.ServerLoginEvent;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ALServerEventHandler {

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        ALServerCommands.register(event.getDispatcher());
    }


    @SubscribeEvent
    public static void onPlayerAttemptLogin(final ServerLoginEvent event) {
        if (event.getPacket().getIntention() == ConnectionProtocol.LOGIN) {
            AddressHandler.playerAttemptLogin(event);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        AddressHandler.playerLogin((ServerPlayer) event.getPlayer());

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        AddressHandler.playerLogout((ServerPlayer) event.getPlayer());
    }

}
