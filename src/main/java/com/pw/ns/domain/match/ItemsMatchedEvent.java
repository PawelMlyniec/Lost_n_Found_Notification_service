package com.pw.ns.domain.match;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ItemsMatchedEvent {

    private final LostReport lostReport;
    private final FoundReport foundReport;
}
