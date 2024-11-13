package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import org.springframework.stereotype.Component;

@Component
public class IsCommand implements Condition<UpdateWrapper> {

    @Override
    public boolean check(UpdateWrapper update) {
        return update.getText().startsWith("/");
    }

}
