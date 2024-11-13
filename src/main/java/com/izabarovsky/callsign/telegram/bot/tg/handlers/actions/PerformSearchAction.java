package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgSearchResult;

@AllArgsConstructor
@Component
public class PerformSearchAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var chatId = payload.getChatId();
        var text = payload.getText();
        List<CallSignModel> k2 = callSignService.findByK2PartialCallSign(text);
        List<CallSignModel> official = callSignService.findByOfficialPartialCallSign(text);
        List<CallSignModel> all = new ArrayList<>(k2);
        all.addAll(official);
        return msgSearchResult(chatId, payload.getThreadId(), all);
    }

}
