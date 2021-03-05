import {BaseEntity} from '../../angular-framework/model/base-entity';
import {User} from '../../angular-framework/security/user/user';

import {ContaContabil} from './conta-contabil';
import {TipoLancamentoContabil} from './tipo-lancamento-contabil';

export class LancamentoContabil extends BaseEntity {
  data: Date;
  dataDeReferencia: Date;
  tipoLancamentoContabil: TipoLancamentoContabil;
  historico: string;
  responsavel: User;
  valor: number;
  valorAposLancamento: number;
  credito: boolean;
  debito: boolean;
  conta: ContaContabil;
  contraPartida: LancamentoContabil;
  contaContraPartida: string;
  saldoAnteriorDaConta: number;
}
