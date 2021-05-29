package com.pw.ns.infrastructure.email;

import com.pw.ns.domain.email.Email;
import com.pw.ns.domain.email.EmailService;
import com.pw.ns.infrastructure.util.Strings;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.stereotype.Component;

import static com.pw.ns.infrastructure.util.Strings.*;

@Component
public class SmtpEmailService implements EmailService {

    @Override
    public void sendEmail(Email email) {

        var mail = EmailBuilder.startingBlank()
            .from("no-reply@lost-n-found.online")
            .to(email.receiverAddress().raw())
            .withSubject(email.subject())
            .withHTMLText(email.content())
            .buildEmail();
        var mailer = MailerBuilder
            .withSMTPServer("smtppro.zoho.eu", 587, "no-reply@lost-n-found.online", "*3kgbDXup?zNJJ-")
            .buildMailer();
        mailer.sendMail(mail);
    }
}
