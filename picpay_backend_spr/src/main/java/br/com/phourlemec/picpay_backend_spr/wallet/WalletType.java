package br.com.phourlemec.picpay_backend_spr.wallet;

public enum WalletType {
    COMUM(1),
    LOJISTA(2);

    private int valor;

    private WalletType(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
