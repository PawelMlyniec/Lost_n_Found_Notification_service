package com.pw.ns.domain.ports.outgoing;

import com.pw.ns.domain.Notification;
import org.springframework.data.repository.CrudRepository;

public interface NotificationRepository extends CrudRepository<Notification, String> {

}
