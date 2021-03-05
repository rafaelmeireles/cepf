import {BaseEntity} from '../../angular-framework/model/base-entity';

import {Cliente} from '../cliente/cliente';
import {ContaCorrente} from '../conta/conta-corrente';
import {Operacao} from '../operacao/operacao';
import {LocalDate} from 'js-joda';

export class Parcela extends BaseEntity {
  numero: number;
  dataDeVencimento: Date;
  dataDeProrrogacao: Date;
  valor: number;
  dataDoPagamento: Date | LocalDate;
  dataDeCancelamento: Date;
  valorPago: number;
  cliente: Cliente;
  vencendo: boolean;
  vencida: boolean;
  contaDeDeposito: ContaCorrente;
  codigoOperacao: number;
  operacao: Operacao;
  historico: string;
  gerarProximaParcela: boolean;
  pagarSomenteJuros: boolean;

  constructor(numero?: number, dataDeVencimento?: Date, valor?: number) {
    super();
    this.numero = numero;
    this.dataDeVencimento = dataDeVencimento;
    this.valor = valor;
  }
}
