package com.pw.ns.domain.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class Notification {

    private final String id;
    private final NotificationType type;
    private final String notifiedUserId;
    private final String content;

    public NotificationId id() {
        return NotificationId.of(id);
    }

    public UserId notifiedUserId() {
        return UserId.of(notifiedUserId);
    }

    public static final class Builder {

        public Builder withNotifiedUserId(UserId userId) {
            this.notifiedUserId = userId.raw();
            return this;
        }
    }
}
