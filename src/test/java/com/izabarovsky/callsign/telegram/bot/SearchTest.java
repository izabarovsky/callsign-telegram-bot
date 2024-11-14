package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogState;
import com.izabarovsky.callsign.telegram.bot.tg.dialog.DialogStateService;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.RootHandler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SearchTest {

    @Autowired
    private DialogStateService dialogStateService;
    @Autowired
    private RootHandler<UpdateWrapper, HandlerResult> handler;
    @Autowired
    private CallSignRepository repository;

    @Test
    void searchInRepositoryByK2CallSign() {
        var expected = getExistsCallSign(repository).getK2CallSign();
        var caseIsSame = repository.findByK2CallSign(expected)
                .orElseThrow(() -> new AssertionFailedError("Search failed"));
        assertEquals(expected, caseIsSame.getK2CallSign());
        var caseIsLower = repository.findByK2CallSign(expected.toLowerCase())
                .orElseThrow(() -> new AssertionFailedError("Search by lowercase is failed"));
        var caseIsUpper = repository.findByK2CallSign(expected.toUpperCase())
                .orElseThrow(() -> new AssertionFailedError("Search by uppercase is failed"));
        assertAll(
                () -> assertEquals(expected, caseIsLower.getK2CallSign(), "Lower case"),
                () -> assertEquals(expected, caseIsUpper.getK2CallSign(), "Upper case")
        );
    }

    @Test
    void searchInRepositoryByOfficialCallSign() {
        var expected = getExistsCallSign(repository).getOfficialCallSign();
        var caseIsSame = repository.findByOfficialCallSign(expected)
                .orElseThrow(() -> new AssertionFailedError("Search failed"));
        assertEquals(expected, caseIsSame.getOfficialCallSign());
        var caseIsLower = repository.findByOfficialCallSign(expected.toLowerCase())
                .orElseThrow(() -> new AssertionFailedError("Search by lowercase is failed"));
        var caseIsUpper = repository.findByOfficialCallSign(expected.toUpperCase())
                .orElseThrow(() -> new AssertionFailedError("Search by uppercase is failed"));
        assertAll(
                () -> assertEquals(expected, caseIsLower.getOfficialCallSign(), "Lower case"),
                () -> assertEquals(expected, caseIsUpper.getOfficialCallSign(), "Upper case")
        );
    }

    @Test
    void newcomerCantSearch() {
        var tgId = randomId();
        var chatId = randomId();
        var userName = "test";
        var expected = TextUtils.textHelloNewcomer(userName);
        var update = updFromUser(tgId, chatId, Command.SEARCH);
        update.getUpdate().getMessage().getFrom().setUserName(userName);
        var result = handler.handle(update).getResponseMsg();
        assertEquals(expected, result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");
    }

    @Test
    void memberCanSearch() {
        var exists = getExistsCallSign(repository);
        var tgId = exists.getTgId();
        var chatId = randomId();

        var result = handler.handle(updFromUser(tgId, chatId, Command.SEARCH)).getResponseMsg();
        assertEquals(textEnterSearch(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, exists.getK2CallSign().substring(0, 3)))
                .getResponseMsg();
        var expectedFound = "Знайдено 1 учасників";
        assertTrue(result.getText().startsWith(expectedFound));
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        assertEquals(DialogState.EXPECT_SEARCH, dialogStateService.getState(tgId),
                "Still search mode");

        result = handler.handle(updFromUser(tgId, chatId, exists.getOfficialCallSign()))
                .getResponseMsg();
        assertTrue(result.getText().startsWith(expectedFound));
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, "ssssss")).getResponseMsg();
        assertEquals(textNothingFound(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        result = handler.handle(updFromUser(tgId, chatId, Command.CANCEL)).getResponseMsg();
        assertEquals(textUseMenuButtons(), result.getText());
        assertEquals(String.valueOf(chatId), result.getChatId(), "Response to chatId");

        assertNull(dialogStateService.getState(tgId), "State cleaned after Cancel command");
    }

    @Test
    void cmdReceivedInSearchMode() {
        var exists = getExistsCallSign(repository);
        var tgId = exists.getTgId();
        var chatId = randomId();
        handler.handle(updFromUser(tgId, chatId, Command.SEARCH));
        assertEquals(DialogState.EXPECT_SEARCH, dialogStateService.getState(tgId), "Entered search mode");
        var result = handler.handle(updFromUser(tgId, chatId, Command.K2_INFO))
                .getResponseMsg();
        assertEquals(DialogState.EXPECT_SEARCH, dialogStateService.getState(tgId), "Still search mode");
        assertEquals(textNothingFound(), result.getText());
    }

}
