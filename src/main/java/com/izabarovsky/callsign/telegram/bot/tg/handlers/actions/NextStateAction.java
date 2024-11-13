package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.*;

@AllArgsConstructor
@Component
public class NextStateAction implements Handler<UpdateWrapper, HandlerResult> {
    private final DialogStateService dialogService;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        var id = payload.getUserId();
        var chatId = payload.getChatId();
        var state = dialogService.getState(id);
        return switch (state) {
            case EXPECT_UNOFFICIAL -> {
                dialogService.putState(id, DialogState.EXPECT_OFFICIAL);
                yield msgEnterValueOrSkip(chatId, "OfficialCallSign");
            }
            case EXPECT_OFFICIAL -> {
                dialogService.putState(id, DialogState.EXPECT_QTH);
                yield msgEnterValueOrSkip(chatId, "QTH");
            }
            case EXPECT_QTH -> {
                dialogService.dropState(id);
                yield msgDialogDone(chatId);
            }
            default -> msgOnAnyUnknown(chatId);
        };
    }

}
