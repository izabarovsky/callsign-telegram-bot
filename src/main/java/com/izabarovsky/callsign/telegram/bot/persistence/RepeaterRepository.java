package com.izabarovsky.callsign.telegram.bot.persistence;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.RepeaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepeaterRepository extends JpaRepository<RepeaterEntity, Long> {

    @Query("select u from RepeaterEntity u where u.official = true")
    List<RepeaterEntity> findOfficial();

    @Query("select u from RepeaterEntity u where u.official = false and u.echolink = false and u.simplex = false")
    List<RepeaterEntity> findNonOfficial();

    @Query("select u from RepeaterEntity u where u.simplex = true")
    List<RepeaterEntity> findParrots();

    @Query("select u from RepeaterEntity u where u.echolink = true")
    List<RepeaterEntity> findEcholink();

    @Query("select u from RepeaterEntity u where u.digital = true")
    List<RepeaterEntity> findDigital();

}
