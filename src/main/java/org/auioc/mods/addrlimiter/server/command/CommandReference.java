package org.auioc.mods.addrlimiter.server.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.auioc.mcmod.arnicalib.utils.game.TextUtils;
import org.auioc.mods.addrlimiter.AddrLimiter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CommandReference {

    public static final SimpleCommandExceptionType NOT_ENABLED = new SimpleCommandExceptionType(text("not_enabled"));

    public static MutableComponent prefix() {
        return TextUtils.empty().append(TextUtils.getStringText("[" + AddrLimiter.MOD_NAME + "] ").withStyle(ChatFormatting.AQUA));
    }

    public static MutableComponent message(Component message) {
        return prefix().append(message);
    }

    public static MutableComponent message(String key) {
        return prefix().append(TextUtils.getI18nText(AddrLimiter.i18n("command." + key)));
    }

    public static MutableComponent message(String key, Object... arguments) {
        return prefix().append(TextUtils.getI18nText(AddrLimiter.i18n("command." + key), arguments));
    }

    public static MutableComponent text(String key) {
        return (TextUtils.getI18nText(AddrLimiter.i18n("command." + key)));
    }

}
