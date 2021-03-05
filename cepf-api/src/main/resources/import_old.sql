CREATE SCHEMA cadastro AUTHORIZATION postgres;
CREATE SCHEMA emprestimo AUTHORIZATION postgres;
CREATE SCHEMA financeiro AUTHORIZATION postgres;
CREATE SCHEMA security AUTHORIZATION postgres;
CREATE SCHEMA contabil AUTHORIZATION postgres;

INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 001, '001 - BANCO DO BRASIL');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 004, '004 - BANCO DO NORDESTE');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 104, '104 - CAIXA ECONOMICA FEDERAL');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 237, '237 - BRADESCO');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 260, '260 - NUBANK');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 033, '033 - SANTANDER');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 341, '341 - ITAÚ UNIBANCO');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 212, '212 - BANCO ORIGINAL');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 041, '041 - BANRISUL');
INSERT INTO financeiro.banco (version, codigo, nome) VALUES (0, 422, '422 - BANCO SAFRA');

INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Aporte');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Resgate');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Transferencia entre contas');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Pagamento de Investidor Senior');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Recebimento de Operação');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Pagamento de Parcela');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Tarifa Bancaria - DOC/TED');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Tarifa Bancaria - Manutenção Conta');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Fator Desagio');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Apuração de Resultado');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Distribuição de Resultado');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Provisão de Perdas');


--INSERT INTO cadastro.pessoa(
--            cpf, version, email, bairro, cep, cidade, complemento, logradouro, 
--            numero, tipo_logradouro, uf, nome, telefone_principal, telefone_secundario)
--    VALUES ('91221927353', 0, 'rafaelmeireles@gmail.com', 'Parquelandia', '60455390', 'Fortaleza', 'Apt 602', 'Francisca Rangel', 
--            '174', 'RUA', 'CE', 'Rafael Meireles Caetano', '85988111806', '85999999999');

--INSERT INTO cadastro.pessoa(
--            cpf, version, email, bairro, cep, cidade, complemento, logradouro, 
--            numero, tipo_logradouro, uf, nome, telefone_principal, telefone_secundario)
--    VALUES ('11111111111', 0, 'investidor1@gmail.com', 'Parquelandia', '60455390', 'Fortaleza', 'Apt 602', 'Francisca Rangel', 
--            '174', 'RUA', 'CE', 'Investidor 1', '85988111806', '85999999999');
--
--INSERT INTO cadastro.pessoa(
--            cpf, version, email, bairro, cep, cidade, complemento, logradouro, 
--            numero, tipo_logradouro, uf, nome, telefone_principal, telefone_secundario)
--    VALUES ('22222222222', 0, 'investidorsenior1@gmail.com', 'Parquelandia', '60455390', 'Fortaleza', 'Apt 602', 'Francisca Rangel', 
--            '174', 'RUA', 'CE', 'Investidor Sênior 1', '85988111806', '85999999999');
--            
--INSERT INTO cadastro.pessoa(
--            cpf, version, email, bairro, cep, cidade, complemento, logradouro, 
--            numero, tipo_logradouro, uf, nome, telefone_principal, telefone_secundario)
--    VALUES ('33333333333', 0, 'pedro@gmail.com', 'Parquelandia', '60455390', 'Fortaleza', 'Apt 602', 'Francisca Rangel', 
--            '174', 'RUA', 'CE', 'Cliente 1', '85988111806', '85999999999');

--Investidor 1
--INSERT INTO cadastro.investidor(version, pessoa_id) VALUES (0, 2);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, true, false, 'Conta Capital - Investidor 1', 0.00);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, true, false, 'Conta Receita a Receber - Investidor 1', 0.00);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, true, false, 'Conta Receita Recebida - Investidor 1', 0.00);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, false, true, 'Conta Parcelas a Receber - Investidor 1', 0.00);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, false, true, 'Conta Despesa Bancaria - Investidor 1', 0.00);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, false, true, 'Conta Despesa com Investidor Senior - Investidor 1', 0.00);

-- Uma para cada conta corrente uma conta contabil
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo)
--	VALUES (0, false, true, 'Conta Corrente - Investidor 1 - BANCO DO BRASIL - 240940', 0.00);
--
--INSERT INTO financeiro.conta_corrente(
--            version, agencia, conta, descricao, digito_agencia, digito_conta, banco_id, conta_contabil_id, investidor_id)
--    VALUES (0, 3474, 24094, 'BANCO DO BRASIL - 240940', 3, 0, 1, 6, 1);

--Investidor Sênior 1
--INSERT INTO cadastro.investidor(version, pessoa_id) VALUES (0, 3);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, true, false, 'Conta Capital - Investidor Sênior 1', 0.00);
----INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, true, false, 'Conta Receita - Investidor Sênior 1', 0.00);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, true, false, 'Conta Receita a Receber - Investidor Sênior 1', 0.00);
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo) VALUES (0, true, false, 'Conta Receita Recebida - Investidor Sênior 1', 0.00);

--Cliente 1
--INSERT INTO cadastro.referencia(version, nome, telefone) VALUES (0, 'Referencia 1', '85988111806');
--INSERT INTO contabil.conta_contabil(version, credora, devedora, nome, saldo)
--	VALUES (0, true, false, 'Conta Cliente - Cliente 1', 0.00);
--INSERT INTO cadastro.cliente(version, taxa, conta_contabil_id, investidor_id, pessoa_id, referencia_id)
--	VALUES (0, 10, 9, 1, 4, 1);

INSERT INTO security.role(version, active, code, name) VALUES (0, true, 'ADMINISTRADOR', 'Administrador do Sistema');
INSERT INTO security.role(version, active, code, name) VALUES (0, true, 'INVESTIDOR', 'Investidor');
INSERT INTO security.role(version, active, code, name) VALUES (0, true, 'INVESTIDOR_SENIOR', 'Investidor Sênior');

INSERT INTO security."user"(version, active, password, cpf, username, investidor_id, dtype, email) VALUES (0, true, '202cb962ac59075b964b07152d234b70', '91221927353', 'administrador', null, 'UserApplication', 'rafaelmeireles@gmail.com');
--INSERT INTO security."user"(version, active, password, cpf, username, investidor_id, dtype, email) VALUES (0, true, '202cb962ac59075b964b07152d234b70', '22222222222', 'investidor1', 1, 'UserApplication', 'investidor1@gmail.com');
--INSERT INTO security."user"(version, active, password, cpf, username, investidor_id, dtype, email) VALUES (0, true, '202cb962ac59075b964b07152d234b70', '33333333333', 'investidor-senior1', 2, 'UserApplication', 'investidor-senior1@gmail.com');

INSERT INTO security.user_has_role(user_id, roles_id, user_has_role_order) VALUES (1, 1, 0);
--INSERT INTO security.user_has_role(user_id, roles_id, user_has_role_order) VALUES (2, 2, 0);
--INSERT INTO security.user_has_role(user_id, roles_id, user_has_role_order) VALUES (3, 3, 0);