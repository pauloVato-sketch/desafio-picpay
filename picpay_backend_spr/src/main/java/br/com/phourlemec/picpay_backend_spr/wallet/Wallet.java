package br.com.phourlemec.picpay_backend_spr.wallet;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("WALLETS")
public record Wallet(
		@Id Long id,
		String nomeCompleto,
		Long cpf,
		String email,
		String senha,
		int tipo,
		BigDecimal saldo) {

	/*
	 * Método para realizar débito de uma carteira, como records são imutáveis,
	 * devemos criar uma nova wallet com saldo
	 * atualizado, e retorná-la, e salvá-la no repository.
	 */
	public Wallet debit(BigDecimal valor) {
		return new Wallet(id, nomeCompleto, cpf, email, senha, tipo, saldo.subtract(valor));
	}

	public Wallet credit(BigDecimal valor) {
		return new Wallet(id, nomeCompleto, cpf, email, senha, tipo, saldo.add(valor));

	}

}
