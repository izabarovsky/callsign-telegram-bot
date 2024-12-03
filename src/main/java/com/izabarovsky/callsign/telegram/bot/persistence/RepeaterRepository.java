package com.izabarovsky.callsign.telegram.bot.persistence;

import com.izabarovsky.callsign.telegram.bot.persistence.entity.RepeaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepeaterRepository extends JpaRepository<RepeaterEntity, Long> {

    @Query("select u from RepeaterEntity u where u.official = true order by u.id")
    List<RepeaterEntity> findOfficial();

    @Query("select u from RepeaterEntity u where u.official = false and u.echolink = false and u.simplex = false order by u.id")
    List<RepeaterEntity> findNonOfficial();

    @Query("select u from RepeaterEntity u where u.simplex = true order by u.id")
    List<RepeaterEntity> findParrots();

    @Query("select u from RepeaterEntity u where u.echolink = true order by u.id")
    List<RepeaterEntity> findEcholink();

    @Query("select u from RepeaterEntity u where u.digital = true order by u.id")
    List<RepeaterEntity> findDigital();

}
