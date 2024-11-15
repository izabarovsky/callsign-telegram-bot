package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.RootHandler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class FlowTest {

    @Autowired
    private RootHandler<UpdateWrapper, HandlerResult> handler;
    @Autowired
    private CallSignRepository repository;

    @Test
    void newcomerCreateFlow() {
        var tgId = randomId();
        var chatId = randomId();

        var result = handler.handle(updFromUser(tgId, chatId, Command.CREATE)).getResponseMsg();
        assertEquals(TextUtils.textK2CallSignRequired(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, k2CallSign())).getResponseMsg();
        assertEquals(TextUtils.textEnterValueOrSkip("OfficialCallSign"), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, officialCallSign())).getResponseMsg();
        assertEquals(TextUtils.textEnterValueOrSkip("QTH"), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, "kyiv")).getResponseMsg();
        assertEquals(TextUtils.textDialogDone(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");
    }

    @Test
    void newcomerCantSkipK2CallSign() {
        var tgId = randomId();
        var chatId = randomId();

        var result = handler.handle(updFromUser(tgId, chatId, Command.CREATE)).getResponseMsg();
        assertEquals(TextUtils.textK2CallSignRequired(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, Command.SKIP)).getResponseMsg();
        assertEquals(TextUtils.textStepCantSkip(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");
    }

    @Test
    void memberEditFlow() {
        var tgId = getExistsCallSign(repository).getTgId();
        var chatId = randomId();

        var result = handler.handle(updFromUser(tgId, chatId, Command.EDIT)).getResponseMsg();
        assertEquals(TextUtils.textEnterValueOrSkip("K2CallSign"), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, k2CallSign())).getResponseMsg();
        assertEquals(TextUtils.textEnterValueOrSkip("OfficialCallSign"), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, officialCallSign())).getResponseMsg();
        assertEquals(TextUtils.textEnterValueOrSkip("QTH"), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, "kyiv")).getResponseMsg();
        assertEquals(TextUtils.textDialogDone(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");
    }

    @ParameterizedTest
    @MethodSource("provideUpdatesRestrictedInGroupChat")
    void shouldReturnNullForGroupChat(String msg) {
        var update = updFromGroupChat(randomId(), randomId(), (int) randomId(), msg);
        assertNull(handler.handle(update));
    }

    @ParameterizedTest
    @MethodSource("provideUpdatesAllowedInGroupChat")
    void shouldReturnMessageForGroupChat(String msg) {
        var tgId = randomId();
        var chatId = randomId();
        var threadId = (int) randomId();
        var update = updFromGroupChat(tgId, chatId, threadId, msg);
        var result = handler.handle(update).getResponseMsg();
        log.info(result.getText());
        assertAll(
                () -> assertEquals(String.valueOf(chatId), result.getChatId(), "chatId"),
                () -> assertEquals(threadId, result.getMessageThreadId(), "threadId"),
                () -> assertNotNull(result.getText(), "msg has text")
        );
    }

    private static Stream<Arguments> provideUpdatesRestrictedInGroupChat() {
        return Stream.of(
                Arguments.of(Command.CREATE.value()),
                Arguments.of(Command.EDIT.value()),
                Arguments.of(Command.SEARCH.value()),
                Arguments.of("some text")
        );
    }

    private static Stream<Arguments> provideUpdatesAllowedInGroupChat() {
        return Stream.of(
                Arguments.of(Command.MY_K2_INFO.value()),
                Arguments.of(Command.STATISTICS.value())
        );
    }

}
