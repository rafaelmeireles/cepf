import {BaseEntity} from '../../angular-framework/model/base-entity';
import {User} from '../../angular-framework/security/user/user';

import {Cliente} from '../cliente/cliente';
import {Investidor} from '../investidor/investidor';
import {TipoDePagamento} from './tipo-pagamento';
import {Parcela} from '../parcela/parcela';
import {TipoDeOperacao} from './tipo-operacao';
import {Transferencia} from '../conta/transferencia';
import {RepasseMensal} from './repasse-mesal';
import {ContaCorrente} from '../conta/conta-corrente';
import {LocalDate} from 'js-joda';

export class Operacao extends BaseEntity {
  numero: number;
  cliente: Cliente;
  data: Date | string;
  dataDeAutorizacao: Date;
  dataQuitacao: Date;
  dataFinalizacao: Date;
  dataDeCancelamento: Date;
  valor: number;
  taxa: number;
  quantidadeDeParcelas: number;
  investidor: Investidor;
  tipoDeOperacao: TipoDeOperacao;
  investidorSenior: Investidor;
  taxaDeRepasse: number;
  tipoDePagamento: TipoDePagamento;
  responsavel: User;
  transferencia: Transferencia;
  parcelas: Parcela[];
  contaCorrentePagamentoSomenteJuros: ContaCorrente;
  contaCorrentePagamentoDoRepasseSomenteJuros: ContaCorrente;
  historico: string;
  temRepasseMensal: boolean;
  repassesMensais: RepasseMensal[];
  podeFinalizar: boolean;
  repasse: boolean;
  pagamentoSemanal: boolean;
  pagamentoQuinzenal: boolean;
  pagamentoMensal: boolean;
  pagamentoSomenteJuros: boolean;
  dataDePagamentoSomenteJuros: Date | LocalDate;
  valorSomenteJuros: number;
  cancelada: boolean;
  showExtraOperation: boolean;
}
