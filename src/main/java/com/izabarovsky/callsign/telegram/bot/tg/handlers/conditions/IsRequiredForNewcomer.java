package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class IsRequiredForNewcomer implements Condition<UpdateWrapper> {
    private final DialogStateService dialogService;

    @Override
    public boolean check(UpdateWrapper update) {
        return DialogState.EXPECT_UNOFFICIAL.equals(dialogService.getState(update.getUserId()));
    }

}
