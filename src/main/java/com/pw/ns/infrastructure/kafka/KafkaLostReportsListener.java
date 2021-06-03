package com.pw.ns.infrastructure.kafka;

import com.pw.lrs.FoundReportProto;
import com.pw.lrs.ItemsMatchedProto;
import com.pw.lrs.LostReportProto;
import com.pw.ns.domain.email.EmailAddress;
import com.pw.ns.domain.match.*;
import com.pw.ns.domain.notification.UserId;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class KafkaLostReportsListener implements MessageListener<String, ItemsMatchedProto> {

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    KafkaLostReportsListener(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onMessage(ConsumerRecord<String, ItemsMatchedProto> data) {

        var proto = data.value();
        var event = ItemsMatchedEvent.builder()
            .withLostReport(mapLostReport(proto.getLostReport()))
            .withFoundReport(mapFoundReport(proto.getFoundReport()))
            .build();
        eventPublisher.publishEvent(event);
    }

    private LostReport mapLostReport(LostReportProto lostReport) {

        return LostReport.builder()
            .withId(LostReportId.of(lostReport.getLostReportId()))
            .withTitle(lostReport.getTitle())
            .withDescription(lostReport.getDescription())
            .withCategory(lostReport.getCategory())
            .withUserId(UserId.of(lostReport.getUserId()))
            .withUserFirstName(lostReport.getUserFirstName())
            .withUserEmail(EmailAddress.of(lostReport.getUserEmail()))
            .withReportedAt(Instant.ofEpochMilli(lostReport.getReportedAt()))
            .build();
    }

    private FoundReport mapFoundReport(FoundReportProto foundReport) {

        return FoundReport.builder()
            .withId(FoundReportId.of(foundReport.getFoundReportId()))
            .withTitle(foundReport.getTitle())
            .withDescription(foundReport.getDescription())
            .withCategory(foundReport.getCategory())
            .withUserId(UserId.of(foundReport.getUserId()))
            .withUserFirstName(foundReport.getUserFirstName())
            .withUserEmail(EmailAddress.of(foundReport.getUserEmail()))
            .withReportedAt(Instant.ofEpochMilli(foundReport.getReportedAt()))
            .build();
    }
}
