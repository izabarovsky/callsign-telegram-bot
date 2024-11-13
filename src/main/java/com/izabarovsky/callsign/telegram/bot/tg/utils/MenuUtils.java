package com.izabarovsky.callsign.telegram.bot.tg.utils;

import com.izabarovsky.callsign.telegram.bot.tg.Command;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuUtils {

    public static ReplyKeyboardMarkup buildSkipOrCancelMenu() {
        List<KeyboardRow> keyboardRows = keyboardRows(Command.SKIP, Command.CANCEL);
        return newReplyKeyboardMarkup(keyboardRows);
    }

    public static ReplyKeyboardMarkup buildCancelMenu() {
        return newReplyKeyboardMarkup(keyboardRows(Command.CANCEL));
    }

    public static ReplyKeyboardMarkup buildMainMenu() {
        List<KeyboardRow> keyboardRows = keyboardRows(Command.MY_K2_INFO,
                Command.SEARCH, Command.STATISTICS, Command.REPEATERS);
        return newReplyKeyboardMarkup(keyboardRows);
    }

    public static InlineKeyboardMarkup buildEditInlineMenu() {
        InlineKeyboardButton inlineKeyboardButton = newInlineButton(Command.EDIT, "Редагувати", null);
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);
        return InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(keyboardButtonsRow))
                .build();
    }

    public static InlineKeyboardMarkup buildGetAllInlineMenu() {
        InlineKeyboardButton inlineKeyboardButton = newInlineButton(Command.GET_ALL, "Завантажити CSV", null);
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(inlineKeyboardButton);
        return InlineKeyboardMarkup.builder()
                .keyboard(Collections.singletonList(keyboardButtonsRow))
                .build();
    }

    public static InlineKeyboardMarkup buildRepeatersInlineMenu(Integer threadId) {
        InlineKeyboardButton buttonOfficial = newInlineButton(Command.OFFICIAL, "Офіційні", threadId);
        InlineKeyboardButton buttonNonOfficial = newInlineButton(Command.NONOFFICIAL, "Неофіційні", threadId);
        InlineKeyboardButton buttonParrots = newInlineButton(Command.PARROTS, "Папуги", threadId);
        InlineKeyboardButton buttonEcholink = newInlineButton(Command.ECHOLINK, "Ехолінк", threadId);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(Collections.singletonList(buttonOfficial));
        rows.add(Collections.singletonList(buttonNonOfficial));
        rows.add(Collections.singletonList(buttonParrots));
        rows.add(Collections.singletonList(buttonEcholink));
        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    public static ReplyKeyboardMarkup buildCreateMenu() {
        return newReplyKeyboardMarkup(keyboardRows(Command.CREATE));
    }

    private static List<KeyboardRow> keyboardRows(Command... commands) {
        Function<Command, KeyboardRow> mapper = s -> {
            KeyboardRow row = new KeyboardRow();
            row.add(s.value());
            return row;
        };
        return Stream.of(commands).map(mapper).collect(Collectors.toList());
    }

    private static KeyboardRow newRow(Command... commands) {
        KeyboardRow row = new KeyboardRow();
        Stream.of(commands).forEach(s -> row.add(s.value()));
        return row;
    }

    private static ReplyKeyboardMarkup newReplyKeyboardMarkup(List<KeyboardRow> rows) {
        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    private static InlineKeyboardButton newInlineButton(Command command, String text, Integer threadId) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        String data = command.value();
        if (Objects.nonNull(threadId)) {
            data = data + ":" + threadId;
        }
        button.setCallbackData(data);
        return button;
    }

}
