package org.auioc.mcmod.addrlimiter.server.command;

import org.auioc.mcmod.addrlimiter.AddrLimiter;
import org.auioc.mcmod.arnicalib.utils.game.CommandFeedbackHelper;
import org.auioc.mcmod.arnicalib.utils.game.TextUtils;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.ChatFormatting;

public class ALCommandReferences {

    public static final CommandFeedbackHelper CFH = new CommandFeedbackHelper(
        TextUtils.getStringText("[" + AddrLimiter.MOD_NAME + "] ").withStyle(ChatFormatting.AQUA),
        AddrLimiter::i18n
    );

    public static final SimpleCommandExceptionType NOT_ENABLED_ERROR = new SimpleCommandExceptionType(CFH.createMessage("not_enabled"));

}
