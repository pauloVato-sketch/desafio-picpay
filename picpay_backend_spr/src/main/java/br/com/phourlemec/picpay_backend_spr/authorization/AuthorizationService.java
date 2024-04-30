package br.com.phourlemec.picpay_backend_spr.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import br.com.phourlemec.picpay_backend_spr.transaction.Transaction;

@Service
public class AuthorizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);
    private RestClient restClient;

    public AuthorizationService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc").build();
    }

    public void authorize(Transaction transaction) {
        /*
         * Em condições de produção, provavelmente o processamento deveria ser feito com
         * POST.
         * A requisição aqui é síncrona, ou seja, aguarda o retorno da chamada do
         * serviço para respondê-lo.
         * Isto é uma decisão de arquitetura!!, e poderia ser modificado
         */
        LOGGER.info("Autorizando a transação ... : {}", transaction);
        var response = this.restClient.get().retrieve().toEntity(Authorization.class);

        if (response.getStatusCode().isError() || !response.getBody().isAuth()) {
            throw new UnauthorizedTransactionException("Transação não autorizado pelo serviço externo!");
        }
        LOGGER.info("Transação Autorizada", transaction);
    }
}
