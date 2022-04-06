package org.auioc.mcmod.addrlimiter.server.address;

import static org.auioc.mcmod.arnicalib.utils.game.TextUtils.EmptyText;
import static org.auioc.mcmod.arnicalib.utils.game.TextUtils.StringText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;
import org.auioc.mcmod.addrlimiter.AddrLimiter;
import org.auioc.mcmod.arnicalib.utils.game.TextUtils;
import org.auioc.mcmod.arnicalib.utils.network.AddressUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraftforge.server.ServerLifecycleHooks;

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
        return StringText(this.toJsonString());
    }

    public Component toChatMessage() {
        TextComponent m = EmptyText();

        m.append(StringText("[" + AddrLimiter.MOD_NAME + "]").withStyle(ChatFormatting.AQUA));
        m.append(newLine().append(I18nText("title")).withStyle(ChatFormatting.DARK_AQUA));

        if (map.isEmpty()) {
            return m.append(newLine().append(" ┗ ").append(I18nText("no_data")).withStyle(ChatFormatting.YELLOW));
        }

        int entryIndex = 0;
        int errorOffline = 0;
        List<UUID> uuidsAll = new ArrayList<UUID>();
        for (Map.Entry<String, List<UUID>> e : this.map.entrySet()) {
            String addr = e.getKey();
            List<UUID> uuids = e.getValue();

            boolean lastEntry = (entryIndex == map.size() - 1);
            entryIndex++;

            TextComponent l = StringText("\n  " + (lastEntry ? "┗ " : "┣ ") + addr);
            if (AddressUtils.isLocalAddress(addr)) {
                l.append(StringText(" ").append(I18nText("local_address")).withStyle(ChatFormatting.GRAY));
            } else if (AddressUtils.isLanAddress(addr)) {
                l.append(StringText(" ").append(I18nText("lan_address")).withStyle(ChatFormatting.GRAY));
            }
            l.append(StringText(" (" + uuids.size() + ")").withStyle(ChatFormatting.GRAY));

            PlayerList playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            for (int i = 0; i < uuids.size(); i++) {
                UUID uuid = uuids.get(i);
                uuidsAll.add(uuid);
                ServerPlayer player = playerList.getPlayer(uuid);

                String p = String.format("\n  %s  %s ", (lastEntry ? " " : "┃"), (i == uuids.size() - 1) ? "┗" : "┣");
                if (player != null) {
                    l.append(StringText(p).append(player.getDisplayName()));
                } else {
                    l.append(StringText(p).append(StringText(uuid.toString()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
                    errorOffline++;
                }
            }

            m.append(l);
        }

        TextComponent e = (TextComponent) EmptyText().withStyle(ChatFormatting.YELLOW);
        if (errorOffline > 0) {
            e.append(newLine().append(I18nText("detected_non_online_players", errorOffline)));
        }
        if (uuidsAll.size() > uuidsAll.stream().distinct().count()) {
            e.append(newLine().append(I18nText("detected_duplicate_players", uuidsAll.size() - uuidsAll.stream().distinct().count())));
        }
        if (!e.getSiblings().isEmpty()) {
            e.append(newLine().append(I18nText("refresh_tip")).withStyle(ChatFormatting.GREEN));
        }
        m.append(e);

        return m;
    }

    private static TranslatableComponent I18nText(String key) {
        return TextUtils.getI18nText("addrlimiter.dump." + key);
    }

    private static TranslatableComponent I18nText(String key, Object... arguments) {
        return TextUtils.getI18nText("addrlimiter.dump." + key, arguments);
    }

    private static TextComponent newLine() {
        return StringText("\n ");
    }

}
