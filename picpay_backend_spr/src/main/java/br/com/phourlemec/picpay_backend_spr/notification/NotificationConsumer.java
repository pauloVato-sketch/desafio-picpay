package br.com.phourlemec.picpay_backend_spr.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.phourlemec.picpay_backend_spr.transaction.Transaction;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    private RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6").build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay_backend_spr")
    public void receiveNotification(Transaction transaction) {
        LOGGER.info("Notificando a transação ... : {}", transaction);
        // Vale ressaltar o fato de que a entidade Notification que foi criada no início
        // foi criada daquela forma para
        // servir como host do mock na requisição do restClient (apenas boolean no
        // objeto JSON de retorno = apenas
        // boolean como variavel da classe Notification).
        var response = this.restClient.get().retrieve().toEntity(Notification.class);

        if (response.getStatusCode().isError() || !response.getBody().message()) {
            throw new NotificationException("Erro ao enviar a notificação");
        }
        LOGGER.info("Notificação foi enviada! : {}", response.getBody());

    }
}
