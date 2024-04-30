package br.com.phourlemec.picpay_backend_spr.authorization;

public class UnauthorizedTransactionException extends RuntimeException {
    public UnauthorizedTransactionException(String mensagem) {
        super(mensagem);
    }
}
