package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgMenuRepeatersLocal;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgMenuRepeatersUkraine;

@Component
public class RepeatersUkraineMenuAction implements Handler<UpdateWrapper, HandlerResult> {

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        return msgMenuRepeatersUkraine(payload.getChatId(), payload.getThreadId());
    }

}
