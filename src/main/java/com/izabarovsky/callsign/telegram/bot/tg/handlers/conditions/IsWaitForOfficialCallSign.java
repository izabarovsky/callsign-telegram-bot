package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class IsWaitForOfficialCallSign implements Condition<UpdateWrapper> {
    private final DialogStateService dialogStateService;

    @Override
    public boolean check(UpdateWrapper update) {
        var tgId = update.getUserId();
        return DialogState.EXPECT_OFFICIAL.equals(dialogStateService.getState(tgId));
    }

}
