-- Script para criação de carteiras default a fim de testar as poucas funcionalidades da API.
-- Motivação para existência: Não há função para criação de carteiras no front-end, já que não é requisito do serviço.

-- Limpando por garantia os registros do banco, no caso de lixo de memória:
DELETE FROM TRANSACTIONS;
DELETE FROM WALLETS;

-- Criando carteiras, uma de usuário e uma de lojista.
INSERT INTO WALLETS(
    ID, NOME_COMPLETO, CPF, EMAIL, SENHA, TIPO, SALDO
) VALUES (
    1, 'Paulo Lopes do Nascimento', 10130656636, 'plnlukscolor@gmail.com', 'toor', 1, 4900.00
);

INSERT INTO WALLETS(
    ID, NOME_COMPLETO, CPF, EMAIL, SENHA, TIPO, SALDO
) VALUES (
    2, 'Vinícius Hiago Gonçalves Ribeiro', 41301174670, 'Nifunean@gmail.com', 'zn6OA*6T1W', 2, 10900.00
);  
