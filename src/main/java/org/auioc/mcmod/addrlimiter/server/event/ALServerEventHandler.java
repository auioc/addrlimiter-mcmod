package org.auioc.mcmod.addrlimiter.server.event;

import org.auioc.mcmod.addrlimiter.server.address.AddressHandler;
import org.auioc.mcmod.addrlimiter.server.command.ALServerCommands;
import org.auioc.mcmod.addrlimiter.server.config.ALConfig;
import org.auioc.mcmod.arnicalib.game.event.server.ServerLoginEvent;
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
        if (ALConfig.DISCONNECT_BEFORE_LOGIN.get()) {
            if (event.getPacket().getIntention() == ConnectionProtocol.LOGIN) {
                AddressHandler.playerAttemptLogin(event);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        AddressHandler.playerLogin((ServerPlayer) event.getEntity());

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        AddressHandler.playerLogout((ServerPlayer) event.getEntity());
    }

}
