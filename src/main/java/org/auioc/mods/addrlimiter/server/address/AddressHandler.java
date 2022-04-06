package org.auioc.mods.addrlimiter.server.address;

import java.util.concurrent.atomic.AtomicBoolean;
import org.auioc.mcmod.arnicalib.server.event.impl.ServerLoginEvent;
import org.auioc.mcmod.arnicalib.utils.game.AddrUtils;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class AddressHandler {

    private static final AddressManager limiter = new AddressManager(1, true, true);
    private static final AtomicBoolean enabled = new AtomicBoolean(true);


    public static AddressManager getLimiter() {
        return limiter;
    }


    public static boolean isEnabled() {
        return enabled.get();
    }

    public static void enable() {
        limiter.clear();
        (ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers())
            .forEach((player) -> {
                limiter.add(AddrUtils.getPlayerIp(player), player.getUUID());
            });
        enabled.set(true);
    }

    public static void disable() {
        enabled.set(false);
        limiter.clear();
    }


    public static void playerAttemptLogin(final ServerLoginEvent event) {
        if (!enabled.get()) {
            return;
        }
        if (!limiter.check(AddrUtils.getIp(event.getNetworkManager()), Util.NIL_UUID)) {
            event.cancel(getMessage());
        }

    }

    public static void playerLogin(final ServerPlayer player) {
        if (!enabled.get()) {
            return;
        }
        if (!limiter.check(AddrUtils.getPlayerIp(player), player.getUUID())) {
            player.connection.disconnect((Component) new TextComponent(getMessage()));
        } else {
            limiter.add(AddrUtils.getPlayerIp(player), player.getUUID());
        }

    }

    public static void playerLogout(final ServerPlayer player) {
        if (!enabled.get()) {
            return;
        }
        limiter.remove(AddrUtils.getPlayerIp(player), player.getUUID());

    }


    private static String getMessage() {
        return String.format(
            "The number of players with the same IP address has reached the limit. §4You can only connect %s times with the same IP!",
            limiter.getMaxPlayersPreAddress()
        );
    }

}
