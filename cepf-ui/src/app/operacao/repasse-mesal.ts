import {BaseEntity} from '../../angular-framework/model/base-entity';
import {ContaCorrente} from '../conta/conta-corrente';
import {LocalDate} from 'js-joda';

export class RepasseMensal extends BaseEntity {
  dataDeVencimento: Date | string;
  valor: number;
  agio: number;
  dataDoPagamento: Date | string;
  dataDeProrrogacao: Date;
  dataDeCancelamento: Date;
  vencimentoAtual: LocalDate;
  codigoOperacao: number;
  contaCorrente: ContaCorrente;
  vencido: boolean;
  vencendo: boolean;
  gerarProximoRepasse: boolean;
}
