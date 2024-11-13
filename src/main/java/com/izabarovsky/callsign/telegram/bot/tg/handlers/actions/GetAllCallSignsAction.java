package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import com.izabarovsky.callsign.telegram.bot.utils.CsvUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.Consumer;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgOnGetAll;

@AllArgsConstructor
@Component
public class GetAllCallSignsAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;
    private final CsvUtil csvUtil;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var chatId = payload.getChatId();
        var threadId = payload.getThreadId();
        var csv = csvUtil.generateCsv(callSignService.findAll());
        HandlerResult handlerResult = new HandlerResult(null);
        Consumer<DefaultAbsSender> consumer = defaultAbsSender -> {
            try {
                defaultAbsSender.execute(msgOnGetAll(chatId, threadId, csv));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        };
        handlerResult.setConsumer(consumer);
        return handlerResult;
    }
}
