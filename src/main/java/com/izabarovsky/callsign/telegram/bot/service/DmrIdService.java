package com.izabarovsky.callsign.telegram.bot.service;

import com.izabarovsky.callsign.telegram.bot.dmrid.RadioIdClient;
import com.izabarovsky.callsign.telegram.bot.persistence.CallSignRepository;
import com.izabarovsky.callsign.telegram.bot.persistence.IntegrationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DmrIdService extends AbstractDmrIdService {

    public DmrIdService(RadioIdClient radioIdClient,
                        CallSignRepository callSignRepository,
                        IntegrationRepository integrationRepository,
                        NotificationService notificationService
    ) {
        super(radioIdClient, callSignRepository, integrationRepository, notificationService);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void onSchedule() {
        setUpTasks();
        executeTasks();
    }

}
