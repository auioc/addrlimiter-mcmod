package org.auioc.mods.addrlimiter.server.event;

import org.auioc.mods.addrlimiter.server.address.AddressHandler;
import org.auioc.mods.addrlimiter.server.command.ServerCommandRegistry;
import org.auioc.mods.arnicalib.server.event.impl.ServerLoginEvent;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ServerEventHandler {

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        ServerCommandRegistry.register(event.getDispatcher());
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
