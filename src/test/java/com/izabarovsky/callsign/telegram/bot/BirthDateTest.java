package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.RootHandler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils.textBirthDateIsInvalid;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils.textDialogDone;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BirthDateTest {
    @Autowired
    private CallSignRepository callSignRepository;
    @Autowired
    private CallSignService callSignService;
    @Autowired
    private DialogStateService dialogStateService;
    @Autowired
    private RootHandler<UpdateWrapper, HandlerResult> handler;


    @Test
    void shouldSaveBirthDate() {
        var birthDate = LocalDate.now();
        var callsignModel = CallSignModel.builder()
                .tgId(randomId())
                .k2CallSign(k2CallSign())
                .birthDate(birthDate)
                .build();
        long id = callSignService.save(callsignModel);
        var callsignEntity = callSignRepository.findById(id).orElseThrow();
        assertEquals(birthDate, callsignEntity.getBirthDate());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBirthDate")
    void shouldReturnInvalidBirthDateMsg(String birthDateString) {
        var chatId = randomId();
        var tgId = randomId();
        dialogStateService.putState(tgId, DialogState.EXPECT_BIRTHDATE);
        var updateWrapper = updFromUser(tgId, chatId, birthDateString);
        var msg = handler.handle(updateWrapper).getResponseMsg();
        assertEquals(textBirthDateIsInvalid(), msg.getText());
        assertEquals(String.valueOf(chatId), msg.getChatId(), "Response to chatId");
    }

    @Test
    void shouldPassBirthDateSet() {
        var chatId = randomId();
        var tgId = getExistsCallSign(callSignRepository).getTgId();
        dialogStateService.putState(tgId, DialogState.EXPECT_BIRTHDATE);
        var updateWrapper = updFromUser(tgId, chatId, "1999-03-12");
        var msg = handler.handle(updateWrapper).getResponseMsg();
        assertEquals(textDialogDone(), msg.getText());
    }

    private static Stream<Arguments> provideInvalidBirthDate() {
        return Stream.of(
                Arguments.of("abc"),
                Arguments.of("2000.12.12"),
                Arguments.of("2000-13-31"),
                Arguments.of("2000-02-31")
        );
    }

}
