package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgEnterSearchOrCancel;

@AllArgsConstructor
@Component
public class StartDialogSearchAction implements Handler<UpdateWrapper, HandlerResult> {
    private final DialogStateService dialogService;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var id = payload.getUserId();
        var chatId = payload.getChatId();
        var threadId = payload.getThreadId();
        dialogService.putState(id, DialogState.EXPECT_SEARCH);
        return msgEnterSearchOrCancel(chatId, threadId);
    }

}
