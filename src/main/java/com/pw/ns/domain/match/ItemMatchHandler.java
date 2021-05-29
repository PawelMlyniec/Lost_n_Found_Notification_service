package com.pw.ns.domain.match;

import com.pw.ns.domain.email.Email;
import com.pw.ns.domain.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.pw.ns.infrastructure.util.Strings.*;

@Component
class ItemMatchHandler {

    private final EmailService emailService;

    @Autowired
    public ItemMatchHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    void onItemsMatched(final ItemsMatchedEvent event) {

        var email = Email.builder()
            .withReceiverAddress(event.lostReport().userEmail())
            .withSubject("It's possible that someone found your lost item!")
            .withContent(format(
                "<html>" +
                "Hi {}! <br/> <br/> " +
                "We detected that someone has recently found an item which is similar to the one you lost. " +
                "Visit our website and check it out :-) http://lost-n-found.online/lost_item/{} <br/> <br/>" +
                "Best regards, <br/>" +
                "The Lost'n'Found Team" +
                "</html>", event.lostReport().userFirstName().orElse("there"), event.foundReport().id()))
            .build();
        emailService.sendEmail(email);
    }
}
