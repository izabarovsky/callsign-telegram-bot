package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.service.CallSignModel;
import com.izabarovsky.callsign.telegram.bot.service.StatisticsModel;
import com.izabarovsky.callsign.telegram.bot.tg.Command;

import java.util.Objects;

public class TextUtils {
    public static String textOnNewDmrId(CallSignModel k2CallSign) {
        return String.format("""
                        %s
                        Комм'юніті К2 вітає %s [%s]
                        з отриманням DMRID [%s]!
                        Ласкаво просимо в цифру! 🎉🍾""",
                Objects.isNull(k2CallSign.getUserName()) ? "hidden" : "@" + k2CallSign.getUserName(),
                k2CallSign.getK2CallSign(),
                k2CallSign.getOfficialCallSign(),
                k2CallSign.getDmrId()
        );
    }

    public static String textCallSingIsInvalid() {
        return """
                Позивний невалідний!
                Має відповідати паттерну [2 LETTER][DIGIT][2 or 3 LETTER]
                Якщо ще не маєш офіційного позивного, просто тисни Skip""";
    }

    public static String textBirthDateIsInvalid() {
        return """
                Невалідний формат дати!
                Введи дату в форматі рррр-мм-дд""";
    }

    public static String textK2CallSingIsInvalid() {
        return """
                Позивний невалідний!
                В позивному дозволені символи українського та англійського алфавіту, цифри, дефіс, круглі дужки.
                Спробуй ще раз!""";
    }

    public static String textCallSingIsBooked(String callSign) {
        return String.format("Позивний %s вже зайнятий!", callSign);
    }

    public static String textK2CallSignRequired() {
        return "Придумай свій позивний для репітера К2. Це обов'язково!";
    }

    public static String textUseMenuButtons() {
        return "Використовуй кнопки меню";
    }

    public static String textHelloNewcomer(String userName) {
        return String.format("""
                Вітаю, @%s! Схоже ти ще не зареєстроаний. Давай зареєструємо твій позивний К2!
                Клікай сюди @K2CallSignBot
                """, Objects.nonNull(userName) ? userName : "[чел з прихованим username:)]");
    }

    public static String textEnterValueOrSkip(String payload) {
        return String.format("Вкажи свій %s. Або пропусти (Skip)", payload);
    }

    public static String textDialogDone() {
        return "Діалог завершено";
    }

    public static String textEnterSearch() {
        return "Введи позивний або частину позивного, спробую знайти цього учасника";
    }

    public static String textNothingFound() {
        return "Нічого не знайдено";
    }

    public static String textStepCantSkip() {
        return "Цей крок не можна пропустити!";
    }

    public static String textUseK2InfoCommandAsFollow() {
        return String.format("Використовуй команду так: %s@username", Command.K2_INFO.value());
    }

    public static String textUserNotFound(String username) {
        return String.format("""
                Учасника [%s] не знайдено
                Можливо він не реєструвався...
                """, username);
    }

    public static String textStatistics(StatisticsModel statisticsModel) {
        return String.format("""
                        <b>Зареєстровано через бот</b>: %s
                        <b>Мають офіційний позивний</b>: %s
                        <b>Не мають офіційного</b>: %s
                        <b>Мають DMR_ID</b>: %s
                        """,
                statisticsModel.getTotal(),
                statisticsModel.getOfficial(),
                statisticsModel.getNonOfficial(),
                statisticsModel.getDmr()
        );
    }

    public static String textRepeatersLocal() {
        return "Репітери Києва 🎙️";
    }

    public static String textRepeatersUkraine() {
        String flag = "\uD83C\uDDFA" + "\uD83C\uDDE6";
        return """
                Репітери України %s
                https://docs.google.com/spreadsheets/d/1Hxvus0C72gtiBaM2JPrPVpf7IUc_-zZYNPJQ9Tv_-IA/edit?usp=sharing
                """.formatted(flag);
    }

    public static String textRepeatersNonOfficial() {
        return "🗽 Можна без офіційного позивного, дотримуючись етики!";
    }

    public static String textRepeatersOfficial() {
        return "<b>Тільки з офіційним позивним!</b>";
    }

    public static String textParrots() {
        return "🦜 🦜 🦜";
    }

    public static String textEcholink() {
        return "<b>Echolink</b>";
    }

    public static String textDigital() {
        return "<b>Digital Voice</b>";
    }
}
