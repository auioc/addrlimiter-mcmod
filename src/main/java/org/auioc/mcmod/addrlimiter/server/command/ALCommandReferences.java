package org.auioc.mcmod.addrlimiter.server.command;

import org.auioc.mcmod.addrlimiter.AddrLimiter;
import org.auioc.mcmod.arnicalib.utils.game.MessageHelper;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class ALCommandReferences {

    public static final MessageHelper MSGH = new MessageHelper(AddrLimiter.MOD_NAME, (key) -> AddrLimiter.i18n("command." + key));

    public static final SimpleCommandExceptionType NOT_ENABLED_ERROR = new SimpleCommandExceptionType(MSGH.create("not_enabled", true));

}
