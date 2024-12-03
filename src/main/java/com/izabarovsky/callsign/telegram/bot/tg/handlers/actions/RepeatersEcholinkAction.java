package com.izabarovsky.callsign.telegram.bot.tg.handlers.actions;

import com.izabarovsky.callsign.telegram.bot.persistence.RepeaterRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.RepeaterEntity;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.Handler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.msgRepeatersEcholink;

@AllArgsConstructor
@Component
public class RepeatersEcholinkAction implements Handler<UpdateWrapper, HandlerResult> {
    private final RepeaterRepository repeaterRepository;

    @Override
    public HandlerResult handle(UpdateWrapper payload) {
        List<String> repeaters = repeaterRepository.findEcholink()
                .stream().map(RepeaterEntity::getInfo)
                .toList();
        return msgRepeatersEcholink(payload.getChatId(), payload.getThreadId(), repeaters);
    }

}
