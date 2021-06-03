package com.pw.ns.domain.textmessage;

import com.pw.ns.domain.notification.UserId;
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
