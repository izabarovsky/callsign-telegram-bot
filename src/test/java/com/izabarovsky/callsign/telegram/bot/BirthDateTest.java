package com.izabarovsky.callsign.telegram.bot;

import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.CallSignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.izabarovsky.callsign.telegram.bot.DataHelper.k2CallSign;
import static com.izabarovsky.callsign.telegram.bot.DataHelper.randomId;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BirthDateTest {
    @Autowired
    private CallSignRepository callSignRepository;
    @Autowired
    private CallSignService callSignService;

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

}
