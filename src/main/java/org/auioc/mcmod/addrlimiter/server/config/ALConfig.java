package org.auioc.mcmod.addrlimiter.server.config;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ALConfig {

    public static final ForgeConfigSpec CONFIG;

    public static final IntValue maxPlayersPreAddress;
    public static final BooleanValue bypassLocalAddress;
    public static final BooleanValue bypassLanAddress;
    public static final BooleanValue disconnectBeforeLogin;

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        {
            maxPlayersPreAddress = b.worldRestart().defineInRange("max_players_pre_address", 1, 1, getMaxPlayers());
            bypassLocalAddress = b.worldRestart().define("bypass_local_address", true);
            bypassLanAddress = b.worldRestart().define("bypass_lan_address", true);
            disconnectBeforeLogin = b.define("disconnect_before_login", true);
        }

        CONFIG = b.build();
    }

    private static int getMaxPlayers() {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server instanceof DedicatedServer dedicated) {
            return dedicated.getProperties().maxPlayers;
        } else {
            return Integer.MAX_VALUE;
        }
    }

}
