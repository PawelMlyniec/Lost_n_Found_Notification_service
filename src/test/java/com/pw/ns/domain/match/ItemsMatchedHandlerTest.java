package com.pw.ns.domain.match;

import com.pw.ns.domain.email.Email;
import com.pw.ns.domain.email.EmailAddress;
import com.pw.ns.domain.email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemsMatchedHandlerTest {

    @Mock
    private EmailService emailService;
    @InjectMocks
    private ItemsMatchedHandler itemsMatchedHandler;

    @Test
    void shouldSendEmailOnItemsMatched() {

        // given
        var event = ItemsMatchedEvent.builder()
            .withLostReport(LostReport.builder()
                .withId(LostReportId.of("123"))
                .withUserFirstName("Gwizdąg")
                .withUserEmail(EmailAddress.of("toster525@gmail.com"))
                .build())
            .withFoundReport(FoundReport.builder()
                .withId(FoundReportId.of("321"))
                .build())
            .build();

        // when
        itemsMatchedHandler.onItemsMatched(event);

        // then
        var expectedEmail = Email.builder()
            .withReceiverAddress(EmailAddress.of("toster525@gmail.com"))
            .withSubject("It's possible that someone found your lost item!")
            .withContent("<html>" +
                "Hi Gwizdąg! <br/> <br/> " +
                "We detected that someone has recently found an item which is similar to the one you lost. " +
                "Visit our website and check it out :-) http://lost-n-found.online/lost_item/321 <br/> <br/>" +
                "Best regards, <br/>" +
                "The Lost'n'Found Team" +
                "</html>")
            .build();
        verify(emailService).sendEmail(expectedEmail);
    }
}