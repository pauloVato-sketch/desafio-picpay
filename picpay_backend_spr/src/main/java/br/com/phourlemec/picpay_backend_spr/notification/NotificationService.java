package br.com.phourlemec.picpay_backend_spr.notification;

import org.springframework.stereotype.Service;

import br.com.phourlemec.picpay_backend_spr.transaction.Transaction;

@Service
public class NotificationService {

    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer) {
        this.notificationProducer = notificationProducer;
    }

    public void notify(Transaction transaction) {
        this.notificationProducer.sendNotification(transaction);
    }
}
