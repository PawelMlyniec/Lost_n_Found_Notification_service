package com.pw.ns.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor(staticName = "of")
public class NotificationId {

    @Getter(AccessLevel.NONE)
    private final String id;

    public String raw() {
        return id;
    }
}
