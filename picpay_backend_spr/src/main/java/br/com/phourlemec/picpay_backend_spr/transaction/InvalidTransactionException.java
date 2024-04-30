package br.com.phourlemec.picpay_backend_spr.transaction;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String mensagem) {
        super(mensagem);
    }
}
