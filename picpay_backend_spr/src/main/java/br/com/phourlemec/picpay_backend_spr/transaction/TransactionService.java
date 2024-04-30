package br.com.phourlemec.picpay_backend_spr.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.phourlemec.picpay_backend_spr.authorization.AuthorizationService;
import br.com.phourlemec.picpay_backend_spr.notification.NotificationService;
import br.com.phourlemec.picpay_backend_spr.wallet.Wallet;
import br.com.phourlemec.picpay_backend_spr.wallet.WalletRepository;
import br.com.phourlemec.picpay_backend_spr.wallet.WalletType;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository,
            AuthorizationService authorizationService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;

    }

    @Transactional
    public Transaction create(Transaction transaction) {
        // 1 - Validar transação (se ela é permitida de ser realizada)
        validate(transaction);
        // 2 - Criar a transação no banco
        var novaTransacao = this.transactionRepository.save(transaction);
        // 3 - Realizar efeito da transação (no caso, debitar da carteira)
        var wallet_pagador = this.walletRepository.findById(transaction.pagador()).get();
        var wallet_recebedor = this.walletRepository.findById(transaction.recebedor()).get();

        this.walletRepository.save(wallet_pagador.debit(transaction.valor()));
        this.walletRepository.save(wallet_recebedor.credit(transaction.valor()));
        // 4 - Chamar serviço externo
        this.authorizationService.authorize(transaction);

        this.notificationService.notify(transaction);
        return novaTransacao;
    }

    /*
     * Atenção para as regras de negócio de permicissividade de transações:
     * - Lojistas só recebem, não enviam, então o pagador não pode ser um lojista.
     * - O pagador não pode ser o recebedor.
     * - Verificar se o pagador possui saldo para realizar a transferência.
     * Acredito que nesta ordem é possível poupar um pouco de processamento,
     * colocando os casos negativos/barrados primeiro e o saldo que sempre será
     * feito por último.Pois não adianta de nada calcular o saldo e ver que a
     * transação não é permitida.
     */
    private void validate(Transaction transaction) {
        // Busca recebedor no repository.
        this.walletRepository.findById(transaction.recebedor())
                // Pega o objeto Wallet do recebedor e busca pagador no repository
                .map(recebedor -> this.walletRepository.findById(transaction.pagador())
                        // Pega o objeto Wallet do pagador e usa para realizar as checagens.
                        // Até aqui os maps fazem sentido porque usamos o pagador, mas e o recebedor?
                        // Vamos usar agora
                        .map(pagador -> isTransactionValid(transaction, pagador) ? transaction : null)
                        .orElseThrow(() -> new InvalidTransactionException(
                                "Transação Inválida - %s".formatted(transaction))))
                .orElseThrow(() -> new InvalidTransactionException(
                        "Pagador não encontrado - %s".formatted(transaction)));
    }

    private boolean isTransactionValid(Transaction transaction, Wallet pagador) {

        return pagador.tipo() == WalletType.COMUM.getValor() &&
                !pagador.id().equals(transaction.recebedor()) &&
                pagador.saldo().compareTo(transaction.valor()) >= 0;
    }

    public List<Transaction> list() {
        return transactionRepository.findAll();
    }
}
