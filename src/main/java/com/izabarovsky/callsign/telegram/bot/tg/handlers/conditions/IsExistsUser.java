package com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions;

import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class IsExistsUser implements Condition<UpdateWrapper> {
    private final CallSignService callSignService;

    @Override
    public boolean check(UpdateWrapper update) {
        var tgId = update.getUserId();
        return callSignService.getCallSign(tgId).isPresent();
    }

}
