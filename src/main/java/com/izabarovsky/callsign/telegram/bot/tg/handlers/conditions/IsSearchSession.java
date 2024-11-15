package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class IsSearchSession implements Condition<UpdateWrapper> {
    private final DialogStateService dialogService;

    @Override
    public boolean check(UpdateWrapper update) {
        return DialogState.EXPECT_SEARCH.equals(dialogService.getState(update.getUserId()));
    }

}
