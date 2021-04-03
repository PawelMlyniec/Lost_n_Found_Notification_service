package com.pw.ns.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class NotificationId {

    private final String id;
}
