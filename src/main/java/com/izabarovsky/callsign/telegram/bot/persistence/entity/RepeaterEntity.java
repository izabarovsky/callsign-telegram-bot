package com.izabarovsky.callsign.telegram.bot.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "repeater")
public class RepeaterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String info;
    private boolean official;
    private boolean digital;
    private boolean simplex;
    private boolean echolink;
}
