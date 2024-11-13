package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class IsSession implements Condition<UpdateWrapper> {
    private final DialogStateService dialogService;

    @Override
    public boolean check(UpdateWrapper update) {
        return Objects.nonNull(dialogService.getState(update.getUserId()));
    }

}
