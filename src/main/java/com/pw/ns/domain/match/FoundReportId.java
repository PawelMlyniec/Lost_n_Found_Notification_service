package com.pw.ns.domain.match;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor(staticName = "of")
public class FoundReportId {

    @Getter(AccessLevel.NONE)
    private final String id;

    public String raw() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
