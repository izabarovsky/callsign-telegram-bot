package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.RootHandler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CallSignValidationTest {
    @Autowired
    private RootHandler<UpdateWrapper, HandlerResult> handler;
    @Autowired
    private DialogStateService dialogStateService;
    @Autowired
    private CallSignRepository repository;

    @Test
    void k2CallSignConflict() {
        var exists = getExistsCallSign(repository).getK2CallSign();
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_UNOFFICIAL);
        var updateWrapper = updFromUser(tgId, chatId, exists);
        var msg = handler.handle(updateWrapper).getResponseMsg();
        assertEquals(textCallSingIsBooked(exists), msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

    @Test
    void officialCallSignConflict() {
        var exists = getExistsCallSign(repository).getOfficialCallSign();
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_OFFICIAL);
        var updateWrapper = updFromUser(tgId, chatId, exists);
        var msg = handler.handle(updateWrapper).getResponseMsg();
        assertEquals(textCallSingIsBooked(exists), msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

    @Test
    void officialCallSignValidation() {
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_OFFICIAL);
        var updateWrapper = updFromUser(tgId, chatId, RandomStringUtils.randomAlphabetic(5));
        var msg = handler.handle(updateWrapper)
                .getResponseMsg();
        assertEquals(textCallSingIsInvalid(), msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

    @ParameterizedTest
    @MethodSource("provideK2CallSign")
    void k2CallSignValidation(String invalidCallSign, String expectedMsg) {
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_UNOFFICIAL);
        var updateWrapper = updFromUser(tgId, chatId, invalidCallSign);
        var msg = handler.handle(updateWrapper)
                .getResponseMsg();
        assertEquals(expectedMsg, msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

    private static Stream<Arguments> provideK2CallSign() {
        return Stream.of(
                Arguments.of(RandomStringUtils.randomAlphabetic(4) + "=&?", textK2CallSingIsInvalid()),
                Arguments.of(RandomStringUtils.randomAlphabetic(4) + "🦜", textK2CallSingIsInvalid()),
                Arguments.of(RandomStringUtils.randomAlphabetic(4) + "+", textK2CallSingIsInvalid()),
                Arguments.of("УкєЇї", textEnterValueOrSkip("OfficialCallSign")),
                Arguments.of("QwАбвЇ- 123", textEnterValueOrSkip("OfficialCallSign")),
                Arguments.of("Bax (Бакс)", textEnterValueOrSkip("OfficialCallSign"))
        );
    }

}
