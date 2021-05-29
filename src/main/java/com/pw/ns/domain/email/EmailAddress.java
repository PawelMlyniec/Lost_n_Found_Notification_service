package com.pw.ns.domain.email;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.validator.routines.EmailValidator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.pw.ns.infrastructure.util.Strings.format;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailAddress {

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    private final String raw;

    public static EmailAddress of(String address) {

        checkArgument(EMAIL_VALIDATOR.isValid(address), format("Email address '{}' is incorrect!", address));
        return new EmailAddress(address);
    }
}
