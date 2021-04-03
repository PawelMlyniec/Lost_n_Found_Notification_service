package com.pw.ns.domain.ports.incoming;

import com.pw.ns.domain.UserId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class TextMessageSentEvent {

    private final String messageId;
    private final UserId sourceUserId;
    private final UserId targetUserId;
    private final String content;
}
