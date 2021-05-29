package com.pw.ns.domain.match;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemMatchHandlerTest {

    @Autowired
    private ItemMatchHandler itemMatchHandler;

    @Test
    void shouldSendEmailOnItemsMatched() {

        // given
        var event = ItemsMatchedEvent.builder()
            .withLostReport(LostReport.builder()
                .withId(LostReportId.of("60900f3ea46f8a3c86ad1f9e"))
                .build())
            .withFoundReport(FoundReport.builder()
                .withId(FoundReportId.of("60b2495837bbcf453a977b51"))
                .build())
            .build();

        // when
        itemMatchHandler.onItemsMatched(event);

        // then
        System.out.println();
    }
}