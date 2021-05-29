package com.pw.ns.domain.email;

import lombok.Builder;
import lombok.Data;

/**
 * DTO containing all necessary information about the email
 */
@Data
@Builder(setterPrefix = "with")
public class Email {

    private final EmailAddress receiverAddress;
    private final String subject;
    private final String content;
}
