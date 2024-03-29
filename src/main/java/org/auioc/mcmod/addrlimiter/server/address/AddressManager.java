package org.auioc.mcmod.addrlimiter.server.address;

import static net.minecraft.network.chat.Component.literal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.auioc.mcmod.addrlimiter.AddrLimiter;
import org.auioc.mcmod.arnicalib.base.network.AddressUtils;
import com.google.gson.Gson;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class AddressManager {

    private final Map<String, List<UUID>> map = Collections.synchronizedMap(new HashMap<String, List<UUID>>());
    private final int maxPlayersPreAddress;
    private final boolean bypassLocalAddress;
    private final boolean bypassLanAddress;
    private final List<String> bypassableAddresses;

    public AddressManager(int maxPlayersPreAddress, boolean bypassLocalAddress, boolean bypassLanAddress, List<String> bypassableAddresses) {
        this.maxPlayersPreAddress = maxPlayersPreAddress;
        this.bypassLocalAddress = bypassLocalAddress;
        this.bypassLanAddress = bypassLanAddress;
        this.bypassableAddresses = bypassableAddresses;
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
            if (this.bypassLocalAddress && AddressUtils.isLocalAddress(addr)) return true;
            if (this.bypassLanAddress && AddressUtils.isLanAddress(addr)) return true;
            if (this.bypassableAddresses.contains(addr)) return true;
            if ((this.map.get(addr)).size() > (this.maxPlayersPreAddress - 1)) return false;
        }
        return true;
    }

    public void clear() {
        this.map.clear();
    }


    public String toJsonString() {
        return (new Gson()).toJson(this.map);
    }

    public Component toJsonText() {
        return literal(this.toJsonString());
    }

    public Component toChatMessage() {
        var m = Component.empty();

        m.append(literal("[" + AddrLimiter.MOD_NAME + "]").withStyle(ChatFormatting.AQUA));
        m.append(newLine().append(translatable("title")).withStyle(ChatFormatting.DARK_AQUA));

        if (map.isEmpty()) {
            return m.append(newLine().append(" ┗ ").append(translatable("no_data")).withStyle(ChatFormatting.YELLOW));
        }

        int entryIndex = 0;
        int errorOffline = 0;
        List<UUID> uuidsAll = new ArrayList<UUID>();
        for (Map.Entry<String, List<UUID>> e : this.map.entrySet()) {
            String addr = e.getKey();
            List<UUID> uuids = e.getValue();

            boolean lastEntry = (entryIndex == map.size() - 1);
            entryIndex++;

            var l = literal("\n  " + (lastEntry ? "┗ " : "┣ ") + addr);

            if (AddressUtils.isLocalAddress(addr)) l.append(literal(" ").append(translatable("local_address")).withStyle(ChatFormatting.GRAY));
            else if (AddressUtils.isLanAddress(addr)) l.append(literal(" ").append(translatable("lan_address")).withStyle(ChatFormatting.GRAY));
            else if (this.bypassableAddresses.contains(addr)) l.append(literal(" ").append(translatable("bypassable_address")).withStyle(ChatFormatting.GRAY));

            l.append(literal(" (" + uuids.size() + ")").withStyle(ChatFormatting.GRAY));

            PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            for (int i = 0; i < uuids.size(); i++) {
                UUID uuid = uuids.get(i);
                uuidsAll.add(uuid);
                ServerPlayer player = playerList.getPlayer(uuid);

                String p = String.format("\n  %s  %s ", (lastEntry ? " " : "┃"), (i == uuids.size() - 1) ? "┗" : "┣");
                if (player != null) {
                    l.append(literal(p).append(player.getDisplayName()));
                } else {
                    l.append(literal(p).append(literal(uuid.toString()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
                    errorOffline++;
                }
            }

            m.append(l);
        }

        var e = Component.empty().withStyle(ChatFormatting.YELLOW);
        if (errorOffline > 0) e.append(newLine().append(translatable("detected_non_online_players", errorOffline)));
        if (uuidsAll.size() > uuidsAll.stream().distinct().count()) e.append(newLine().append(translatable("detected_duplicate_players", uuidsAll.size() - uuidsAll.stream().distinct().count())));
        if (!e.getSiblings().isEmpty()) e.append(newLine().append(translatable("refresh_tip")).withStyle(ChatFormatting.GREEN));
        m.append(e);

        return m;
    }

    private static Component translatable(String key) {
        return Component.translatable("addrlimiter.dump." + key);
    }

    private static Component translatable(String key, Object... arguments) {
        return Component.translatable("addrlimiter.dump." + key, arguments);
    }

    private static MutableComponent newLine() {
        return literal("\n ");
    }

}
