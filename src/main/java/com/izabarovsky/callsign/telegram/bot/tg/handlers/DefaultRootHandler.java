package com.izabarovsky.callsign.telegram.bot.tg.handlers;

import com.izabarovsky.callsign.telegram.bot.tg.Command;
import com.izabarovsky.callsign.telegram.bot.tg.HandlerResult;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.actions.*;
import com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.*;
import com.izabarovsky.callsign.telegram.bot.tg.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.izabarovsky.callsign.telegram.bot.tg.handlers.conditions.CmdConditionsFactory.cmdCondition;
import static com.izabarovsky.callsign.telegram.bot.tg.utils.MessageUtils.*;

@Slf4j
@Component
public class DefaultRootHandler implements Handler<UpdateWrapper, HandlerResult>, RootHandler<UpdateWrapper, HandlerResult> {
    private static ApplicationContext context;
    private final Handler<UpdateWrapper, HandlerResult> dummyHandler = s -> null;

    public DefaultRootHandler(ApplicationContext context) {
        DefaultRootHandler.context = context;
    }

    public HandlerResult handle(UpdateWrapper update) {
        log.info("{}", update);
        return rootHandler().handle(update);
    }

    private Handler<UpdateWrapper, HandlerResult> rootHandler() {
        var privateChatCommandChain = BranchHandler.builder()
                .condition(getCondition(IsCommand.class))
                .branchTrue(commandBranch())
                .branchFalse(textBranch())
                .build();

        var groupChatCommandChain = new ChainHandler(dummyHandler)
                .setHandler(cmdCondition(Command.MY_K2_INFO), getHandler(MyK2InfoGroupAction.class))
                .setHandler(cmdCondition(Command.K2_INFO), getHandler(K2InfoAction.class))
                .setHandler(cmdCondition(Command.STATISTICS), getHandler(K2StatisticsGroupAction.class))
                .setHandler(cmdCondition(Command.REPEATERS), getHandler(RepeatersPrivateAction.class))
                .setHandler(cmdCondition(Command.OFFICIAL), getHandler(RepeatersOfficialAction.class))
                .setHandler(cmdCondition(Command.NONOFFICIAL), getHandler(RepeatersNonOfficialAction.class))
                .setHandler(cmdCondition(Command.PARROTS), getHandler(RepeatersParrotsAction.class))
                .setHandler(cmdCondition(Command.ECHOLINK), getHandler(RepeatersEcholinkAction.class));

        return BranchHandler.builder()
                .condition(getCondition(IsPersonalChat.class))
                .branchTrue(privateChatCommandChain)
                .branchFalse(groupChatCommandChain)
                .build();
    }

    private BranchHandler commandBranch() {
        return BranchHandler.builder()
                .condition(getCondition(IsSession.class))
                .branchTrue(commandInSessionBranch())
                .branchFalse(commandOutSessionBranch())
                .build();
    }

    private BranchHandler commandInSessionBranch() {
        var existsUser = BranchHandler.builder()
                .condition(cmdCondition(Command.SKIP))
                .branchTrue(getHandler(NextStateAction.class))
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
                .build();

        var newcomerSkipNode = BranchHandler.builder()
                .condition(getCondition(IsRequiredForNewcomer.class))
                .branchTrue(s -> msgCantSkip(s.getChatId()))
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
                .build();

        var newcomerUser = BranchHandler.builder()
                .condition(cmdCondition(Command.SKIP))
                .branchTrue(newcomerSkipNode)
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
                .build();

        var existsUserNode = BranchHandler.builder()
                .condition(getCondition(IsExistsUser.class))
                .branchTrue(existsUser)
                .branchFalse(newcomerUser)
                .build();

        return BranchHandler.builder()
                .condition(cmdCondition(Command.CANCEL))
                .branchTrue(getHandler(CleanStateAction.class))
                .branchFalse(existsUserNode)
                .build();
    }

    private BranchHandler commandOutSessionBranch() {
        var newcomerUser = BranchHandler.builder()
                .condition(cmdCondition(Command.CREATE))
                .branchTrue(getHandler(StartDialogCreateAction.class))
                .branchFalse(s -> msgNewcomer(
                        s.getChatId(),
                        s.getThreadId(),
                        s.getUsername()))
                .build();
        var existsUserChain = new ChainHandler(s -> msgOnAnyUnknown(s.getChatId()))
                .setHandler(cmdCondition(Command.EDIT), getHandler(StartDialogEditAction.class))
                .setHandler(cmdCondition(Command.MY_K2_INFO), getHandler(MyK2InfoPrivateAction.class))
                .setHandler(cmdCondition(Command.K2_INFO), getHandler(K2InfoAction.class))
                .setHandler(cmdCondition(Command.SEARCH), getHandler(StartDialogSearchAction.class))
                .setHandler(cmdCondition(Command.GET_ALL), getHandler(GetAllCallSignsAction.class))
                .setHandler(cmdCondition(Command.STATISTICS), getHandler(K2StatisticsPrivateAction.class))
                .setHandler(cmdCondition(Command.REPEATERS), getHandler(RepeatersPrivateAction.class))
                .setHandler(cmdCondition(Command.OFFICIAL), getHandler(RepeatersOfficialAction.class))
                .setHandler(cmdCondition(Command.NONOFFICIAL), getHandler(RepeatersNonOfficialAction.class))
                .setHandler(cmdCondition(Command.PARROTS), getHandler(RepeatersParrotsAction.class))
                .setHandler(cmdCondition(Command.ECHOLINK), getHandler(RepeatersEcholinkAction.class));

        return BranchHandler.builder()
                .condition(getCondition(IsExistsUser.class))
                .branchTrue(existsUserChain)
                .branchFalse(newcomerUser)
                .build();
    }

    private BranchHandler textBranch() {
        var sessionChain = new ChainHandler(s -> msgOnAnyUnknown(s.getChatId()))
                .setHandler(getCondition(IsWaitForK2CallSign.class), getHandler(SaveK2CallSignAction.class))
                .setHandler(getCondition(IsWaitForOfficialCallSign.class), getHandler(SaveOfficialCallSignAction.class))
                .setHandler(getCondition(IsWaitForQth.class), getHandler(SaveQthAction.class))
                .setHandler(getCondition(IsWaitForSearch.class), getHandler(PerformSearchAction.class));

        return BranchHandler.builder()
                .condition(getCondition(IsSession.class))
                .branchTrue(sessionChain)
                .branchFalse(s -> msgOnAnyUnknown(s.getChatId()))
                .build();
    }

    private <T extends Handler<UpdateWrapper, HandlerResult>> T getHandler(Class<T> handlerClass) {
        return context.getBean(handlerClass);
    }

    private <T extends Condition<UpdateWrapper>> T getCondition(Class<T> conditionClass) {
        return context.getBean(conditionClass);
    }

}
