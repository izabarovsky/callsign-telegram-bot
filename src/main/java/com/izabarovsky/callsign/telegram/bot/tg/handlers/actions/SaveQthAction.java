package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgDialogDone;

@AllArgsConstructor
@Component
public class SaveQthAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;
    private final DialogStateService dialogStateService;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var chatId = payload.getChatId();
        var tgId = payload.getUserId();
        CallSignModel callSignModel = callSignService.getCallSign(tgId).orElseThrow();
        callSignModel.setQth(payload.getText());
        callSignService.save(callSignModel);
        dialogStateService.dropState(chatId);
        return msgDialogDone(chatId);
    }

}
