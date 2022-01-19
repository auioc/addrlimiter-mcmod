package org.auioc.mods.addrlimiter.server.address;

import static org.auioc.mods.arnicalib.utils.game.TextUtils.getStringText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;
import org.auioc.mods.arnicalib.utils.network.AddressUtils;
import net.minecraft.network.chat.TextComponent;

public final class AddressManager {

    private final Map<String, List<UUID>> map = Collections.synchronizedMap(new HashMap<String, List<UUID>>());
    private final int maxPlayersPreAddress;
    private final boolean bypassLocalAddress;
    private final boolean bypassLanAddress;

    public AddressManager(int maxPlayersPreAddress, boolean bypassLocalAddress, boolean bypassLanAddress) {
        this.maxPlayersPreAddress = maxPlayersPreAddress;
        this.bypassLocalAddress = bypassLocalAddress;
        this.bypassLanAddress = bypassLanAddress;
    }


    public int getMaxPlayersPreAddress() {
        return this.maxPlayersPreAddress;
    }

    public boolean shouldBypassLocalAddress() {
        return this.bypassLocalAddress;
    }

    public boolean shouldBypassLanAddress() {
        return this.bypassLanAddress;
    }


    public void add(String addr, UUID uuid) {
        List<UUID> list = this.map.getOrDefault(addr, new ArrayList<UUID>());
        list.add(uuid);
        this.map.put(addr, list);
    }

    public void remove(String addr, UUID uuid) {
        if (this.map.containsKey(addr)) {
            List<UUID> list = this.map.get(addr);
            if (list.size() <= 1) {
                this.map.remove(addr);
            } else {
                list.remove(uuid);
                this.map.put(addr, list);
            }
        }
    }

    public boolean check(String addr, UUID uuid) {
        if (this.map.containsKey(addr)) {
            if (this.bypassLocalAddress && AddressUtils.isLocalAddress(addr)) {
                return true;
            }
            if (this.bypassLanAddress && AddressUtils.isLanAddress(addr)) {
                return true;
            }
            if ((this.map.get(addr)).size() > (this.maxPlayersPreAddress - 1)) {
                return false;
            }
        }
        return true;
    }

    public void clear() {
        this.map.clear();
    }

    public String toJsonString() {
        return (new Gson()).toJson(this.map);
    }

    public TextComponent toJsonText() {
        return getStringText(this.toJsonString());
    }

}
