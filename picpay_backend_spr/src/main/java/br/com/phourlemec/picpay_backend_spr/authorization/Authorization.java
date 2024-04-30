package br.com.phourlemec.picpay_backend_spr.authorization;

public record Authorization(String message) {
    public boolean isAuth() {
        return message.equals("Autorizado");
    }
}
