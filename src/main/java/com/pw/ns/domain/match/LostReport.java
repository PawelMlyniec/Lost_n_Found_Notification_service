package com.pw.ns.domain.match;

import com.pw.ns.domain.notification.UserId;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder(setterPrefix = "with")
public class LostReport {

    private final LostReportId id;
    private final String title;
    private final String description;
    private final String category;
    private final UserId userId;
    private final Instant reportedAt;
}
