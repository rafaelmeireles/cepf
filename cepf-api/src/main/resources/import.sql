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
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Pagamento de Parcela Somente Juros');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Tarifa Bancaria - DOC/TED');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Tarifa Bancaria - Manutenção Conta');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Fator Desagio');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Geração de Nova Parcela Somente Juros');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Finalizando Operação Somente Juros');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Apuração de Resultado');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Distribuição de Resultado');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Provisão de Perdas');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Cancelamento de Operação');
INSERT INTO contabil.tipo_lancamento_contabil(version, nome) VALUES (0, 'Pagamento de Operação Sem Juros');

INSERT INTO security.role(version, active, code, name) VALUES (0, true, 'ADMINISTRADOR', 'Administrador do Sistema');
INSERT INTO security.role(version, active, code, name) VALUES (0, true, 'INVESTIDOR', 'Investidor');
INSERT INTO security.role(version, active, code, name) VALUES (0, true, 'INVESTIDOR_SENIOR', 'Investidor Sênior');

INSERT INTO security."user"(version, active, password, cpf, username, investidor_id, dtype, email) VALUES (0, true, '202cb962ac59075b964b07152d234b70', '10274712602', 'administrador', null, 'UserApplication', 'administrador@gmail.com');

INSERT INTO security.user_has_role(user_id, roles_id, user_has_role_order) VALUES (1, 1, 0);