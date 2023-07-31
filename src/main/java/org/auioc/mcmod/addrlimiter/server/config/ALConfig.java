package org.auioc.mcmod.addrlimiter.server.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ALConfig {

    public static final ForgeConfigSpec CONFIG;

    public static final IntValue maxPlayersPreAddress;
    public static final BooleanValue bypassLocalAddress;
    public static final BooleanValue bypassLanAddress;
    public static final ConfigValue<List<String>> bypassableAddresses;
    public static final BooleanValue disconnectBeforeLogin;
    public static final ConfigValue<String> disconnectMessage;

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        {
            maxPlayersPreAddress = b.worldRestart().defineInRange("max_players_pre_address", 1, 1, getMaxPlayers());
            bypassLocalAddress = b.worldRestart().define("bypass_local_address", true);
            bypassLanAddress = b.worldRestart().define("bypass_lan_address", true);
            bypassableAddresses = b.define("bypassable_addresses", new ArrayList<String>());
            disconnectBeforeLogin = b.define("disconnect_before_login", true);
            disconnectMessage = b.define("disconnect_message", "The number of players with the same IP address has reached the limit. \u00A74You can only connect %s times with the same IP!");
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
