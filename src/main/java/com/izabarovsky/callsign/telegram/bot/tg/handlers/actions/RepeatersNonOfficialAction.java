package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgRepeatersNonOfficial;

@Component
public class RepeatersNonOfficialAction implements Handler<UpdateWrapper, HandlerResult> {

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        return msgRepeatersNonOfficial(payload.getChatId(), payload.getThreadId());
    }

}
