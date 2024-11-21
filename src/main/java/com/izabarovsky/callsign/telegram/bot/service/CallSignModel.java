package com.izabarovsky.callsign.telegram.bot.service;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class CallSignModel {
    private Long tgId;
    private String firstName;
    private String lastName;
    private String userName;
    private LocalDate birthDate;
    private String k2CallSign;
    private String officialCallSign;
    private String qth;
    private String dmrId;
    private Instant creationTimestamp;
}
