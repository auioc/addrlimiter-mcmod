package org.auioc.mcmod.addrlimiter.server.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ALConfig {

    public static final ForgeConfigSpec CONFIG;

    public static final IntValue MAX_PLAYERS_PRE_ADDRESS;
    public static final BooleanValue BYPASS_LOCAL;
    public static final BooleanValue BYPASS_LAN;
    public static final ConfigValue<List<String>> BYPASS_ADDRESSES;
    public static final BooleanValue DISCONNECT_BEFORE_LOGIN;
    public static final ConfigValue<String> DISCONNECT_MESSAGE;

    static {
        final ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();


        MAX_PLAYERS_PRE_ADDRESS = b.worldRestart().defineInRange("max_players_pre_address", 1, 1, Integer.MAX_VALUE);
        {
            b.push("bypass");
            BYPASS_LOCAL = b.worldRestart().define("local", true);
            BYPASS_LAN = b.worldRestart().define("lan", true);
            BYPASS_ADDRESSES = b.define("addresses", new ArrayList<String>());
            b.pop();
        }
        DISCONNECT_BEFORE_LOGIN = b.define("disconnect_before_login", true);
        DISCONNECT_MESSAGE = b.define("disconnect_message", "The number of players with the same IP address has reached the limit. \u00A74You can only connect %s times with the same IP!");


        CONFIG = b.build();
    }

}
