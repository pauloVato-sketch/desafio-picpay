package br.com.phourlemec.picpay_backend_spr.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSACTIONS")
public record Transaction(
        @Id Long id,
        Long pagador,
        Long recebedor,
        BigDecimal valor,
        @CreatedDate LocalDateTime criadaEm) {
    public Transaction {
        valor = valor.setScale(2);
    }
}
