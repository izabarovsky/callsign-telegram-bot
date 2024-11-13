package com.izabarovsky.callsign.telegram.bot.tg.handlers.validator;

import org.springframework.stereotype.Component;

@Component
public class UnofficialCallSignValidator {
    private final String pattern = "^[0-9A-Za-z-А-Яа-я-ЄєЇї ()]+$";

    public boolean isValid(String payload) {
        return payload.matches(pattern);
    }

}
