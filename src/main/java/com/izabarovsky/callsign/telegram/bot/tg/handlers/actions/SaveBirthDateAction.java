package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.validator.DateValidator;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgBirthDateIsInvalid;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgDialogDone;

@AllArgsConstructor
@Component
public class SaveBirthDateAction implements Handler<UpdateWrapper, HandlerResult> {
    private final CallSignService callSignService;
    private final DialogStateService dialogStateService;
    private final DateValidator dateValidator;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        String dateString = payload.getText();
        var chatId = payload.getChatId();
        if (!dateValidator.isValid(dateString)) {
            return msgBirthDateIsInvalid(chatId);
        }
        LocalDate birthDate = LocalDate.parse(dateString);
        CallSignModel callSignModel = callSignService.getCallSign(payload.getUserId())
                .orElseThrow(() -> new RuntimeException("Try to get callsign for birthdate update, but not found"));
        callSignModel.setBirthDate(birthDate);
        callSignService.save(callSignModel);
        dialogStateService.dropState(payload.getUserId());
        return msgDialogDone(chatId);
    }

}
