-- Em teoria, mantenha concordância entre os campos das entidades e do banco de dados, por exemplo,
-- Decimal do SQL pode ter tamanhos até 65 bits e o BigDecimal do Java pode ter até 32 bits, então o ideal seria manter
-- as tipagens e tamanhos de maneira que não sobre tamanho mas também não falte. Outro exemplo seria a relação entre o 
-- Long do Java e o BIGINT do SQL, tendo ambos 64 bits, podem se relacionar de forma equiparavel, diferente do Int do SQL (32).
-- ------------------------------------------------------------------------------------------------------------------------------
-- Criação da tabela de carteiras com os mesmos campos do modelo/da entity.
-- ID: Sequencial (1-2-3 etc), chave primária.
-- Nome completo: Varchar simples.
-- CPF: Apenas números BIGINT 64 bit. (Talvez nem houvesse necessidade considerando que o CPF é limitado)
-- Email: Varchar simples. (Acredito que verificações da estrutura por exemplo conter @, seriam feitas ainda no front).
-- Senha: Varchar simples. (Novamente, criptografia deveria ser aplicada aqui).
-- Tipo: Inteiro simples 1 ou 2.
-- Saldo: DECIMAL de 2 casas depois da vírgula.
-- PS: Os campos em aspas duplas são CASE-SENSITIVE, mas como por consenso e boas práticas, sempre usaria maiúsculo anyway. 
CREATE TABLE IF NOT EXISTS WALLETS(
    ID SERIAL PRIMARY KEY,
    NOME_COMPLETO VARCHAR(256),
    CPF BIGINT,
    EMAIL VARCHAR(128),
    "SENHA" VARCHAR(128),
    "TIPO" INT,
    SALDO DECIMAL(10,2)
);
-- CPF/CNPJ e e-mails devem ser únicos no sistema. 
-- Sendo assim, seu sistema deve permitir apenas um cadastro com o mesmo CPF ou endereço de e-mail;
-- Além de aplicar estas regras no código no momento da criação de uma conta, devemos colocar a regra no banco via cláusulas
 -- de unique.
CREATE UNIQUE INDEX IF NOT EXISTS cpf_idx ON WALLETS (CPF);
CREATE UNIQUE INDEX IF NOT EXISTS email_idx ON WALLETS (EMAIL);


-- Criação da tabela de transações com os mesmos campos do modelo/da entity.
-- ID: Sequencial (1-2-3 etc), chave primária.
-- Pagador: BigInt com o ID do pagador (chave estrangeira linkando o registro de transação à carteira do pagador).
-- Recebedor: BigInt com o ID do recebedor (chave estrangeira linkando o registro de transação à carteira do recebedor).
-- Valor: Decimal de 2 casas depois da vírgula.
-- Criada Em: Timestamp do momento de criação da entidade.
CREATE TABLE IF NOT EXISTS TRANSACTIONS(
    ID SERIAL PRIMARY KEY,
    PAGADOR BIGINT, 
    RECEBEDOR BIGINT,
    "VALOR" DECIMAL(10,2),
    CRIADA_EM TIMESTAMP,
    FOREIGN KEY (PAGADOR) REFERENCES WALLETS(ID),
    FOREIGN KEY (RECEBEDOR) REFERENCES WALLETS(ID)
);