package com.pw.ns.domain.email;

/**
 * Abstraction over sending emails
 */
public interface EmailService {

    /**
     * Send the email
     *
     * @param email email to be sent
     */
    void sendEmail(final Email email);
}
