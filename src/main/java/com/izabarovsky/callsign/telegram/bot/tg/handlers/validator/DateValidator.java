package com.izabarovsky.callsign.telegram.bot.tg.handlers.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class DateValidator {

    public boolean isValid(String payload) {
        try {
            LocalDate.parse(payload);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}
