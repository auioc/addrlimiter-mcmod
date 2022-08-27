package org.auioc.mcmod.addrlimiter.server.address;

import java.util.concurrent.atomic.AtomicBoolean;
import org.auioc.mcmod.addrlimiter.server.config.ALConfig;
import org.auioc.mcmod.arnicalib.server.event.impl.ServerLoginEvent;
import org.auioc.mcmod.arnicalib.utils.game.AddrUtils;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class AddressHandler {

    private static final AddressManager LIMITER = new AddressManager(ALConfig.maxPlayersPreAddress.get(), ALConfig.bypassLocalAddress.get(), ALConfig.bypassLanAddress.get(), ALConfig.bypassableAddresses.get());
    private static final AtomicBoolean ENABLED = new AtomicBoolean(true);

    public static AddressManager getLimiter() {
        return LIMITER;
    }


    public static boolean isEnabled() {
        return ENABLED.get();
    }

    public static void enable() {
        LIMITER.clear();
        (ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
            .forEach((player) -> {
                LIMITER.add(AddrUtils.getPlayerIp(player), player.getUUID());
            });
        ENABLED.set(true);
    }

    public static void disable() {
        ENABLED.set(false);
        LIMITER.clear();
    }


    public static void playerAttemptLogin(final ServerLoginEvent event) {
        if (!ENABLED.get()) return;

        if (!LIMITER.check(AddrUtils.getIp(event.getNetworkManager()), Util.NIL_UUID)) {
            event.cancel(getMessage());
        }
    }

    public static void playerLogin(final ServerPlayer player) {
        if (!ENABLED.get()) return;

        if (!LIMITER.check(AddrUtils.getPlayerIp(player), player.getUUID())) {
            player.connection.disconnect((Component) new TextComponent(getMessage()));
        } else {
            LIMITER.add(AddrUtils.getPlayerIp(player), player.getUUID());
        }
    }

    public static void playerLogout(final ServerPlayer player) {
        if (!ENABLED.get()) return;

        LIMITER.remove(AddrUtils.getPlayerIp(player), player.getUUID());
    }


    private static String getMessage() {
        return String.format(
            "The number of players with the same IP address has reached the limit. §4You can only connect %s times with the same IP!",
            LIMITER.getMaxPlayersPreAddress()
        );
    }

}
