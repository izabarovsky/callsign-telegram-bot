package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgPrivateStatistics;

@AllArgsConstructor
@Component
public class K2StatisticsPrivateAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var chatId = payload.getChatId();
        var threadId = payload.getThreadId();
        return msgPrivateStatistics(chatId, threadId, callSignService.getStatistics());
    }
}
