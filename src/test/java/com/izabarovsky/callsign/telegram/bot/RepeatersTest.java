package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.RepeaterRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.entity.RepeaterEntity;
import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.RootHandler;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.*;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.TextUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class RepeatersTest {
    @Autowired
    private RootHandler<UpdateWrapper, HandlerResult> handler;
    @Autowired
    private RepeaterRepository repeaterRepository;
    @Autowired
    private CallSignRepository callSignRepository;

    private static final String OFFICIAL_INFO = "<b>R3 \uD83D\uDD0B</b>\nRX 145.675 / TX 145.075 (offset: -0.6)\nCTCSS: 88.5Hz";
    private static final String NONOFFICIAL_INFO = "<b>Kyiv-2 \uD83D\uDD0B</b>\nRX 446.150 / TX 434.950 (offset -11.2)\nCTCSS: 74.4Hz\nQTH - Кловський узвіз";
    private static final String PARROTS_INFO = "<b>Brovary Parrot</b>\nRX/TX 436.700\nCTSS: 71.9Hz\nQTH - Бровари";
    private static final String ECHOLINK_INFO = "RX/TX 438.375\nCTSS: 123.0Hz\nQTH - Борщагівка";

    private Long tgId;
    private Long chatId;

    @BeforeAll
    void prepareData() {
        tgId = getExistsCallSign(callSignRepository).getTgId();
        chatId = randomId();
        repeaterRepository.save(prepareOfficialEntity());
        repeaterRepository.save(prepareNonOfficialEntity());
        repeaterRepository.save(prepareParrotEntity());
        repeaterRepository.save(prepareEcholinkEntity());
    }

    @ParameterizedTest
    @MethodSource("provideRepeatersTestData")
    void shouldReturnRepeatersInfo(Command cmd, String expected) {
        var result = handler.handle(updFromUser(tgId, chatId, cmd)).getResponseMsg();
        assertEquals(expected, result.getText());
    }

    private static Stream<Arguments> provideRepeatersTestData() {
        var officialExpected = String.join("\n", textRepeatersOfficial(), OFFICIAL_INFO);
        var nonofficialExpected = String.join("\n", textRepeatersNonOfficial(), NONOFFICIAL_INFO);
        var parrotsExpected = String.join("\n", textParrots(), PARROTS_INFO);
        var echolinkExpected = String.join("\n", textEcholink(), ECHOLINK_INFO);
        return Stream.of(
                Arguments.of(Command.OFFICIAL, officialExpected),
                Arguments.of(Command.NONOFFICIAL, nonofficialExpected),
                Arguments.of(Command.PARROTS, parrotsExpected),
                Arguments.of(Command.ECHOLINK, echolinkExpected)
        );
    }

    private RepeaterEntity prepareOfficialEntity() {
        var official = new RepeaterEntity();
        official.setInfo(OFFICIAL_INFO);
        official.setOfficial(true);
        return official;
    }

    private RepeaterEntity prepareNonOfficialEntity() {
        var nonOfficial = new RepeaterEntity();
        nonOfficial.setInfo(NONOFFICIAL_INFO);
        return nonOfficial;
    }

    private RepeaterEntity prepareParrotEntity() {
        var parrot = new RepeaterEntity();
        parrot.setInfo(PARROTS_INFO);
        parrot.setSimplex(true);
        return parrot;
    }

    private RepeaterEntity prepareEcholinkEntity() {
        var echolink = new RepeaterEntity();
        echolink.setInfo(ECHOLINK_INFO);
        echolink.setEcholink(true);
        return echolink;
    }

}
