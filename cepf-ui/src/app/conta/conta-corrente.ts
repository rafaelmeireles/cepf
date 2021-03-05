import {BaseEntity} from '../../angular-framework/model/base-entity';

import {Banco} from './banco';
import {ContaContabil} from './conta-contabil';
import {Investidor} from '../investidor/investidor';

export class ContaCorrente extends BaseEntity {
  banco: Banco;
  agencia: number;
  digitoAgencia: number;
  conta: number;
  digitoConta: number;
  descricao: string;
  contaContabil: ContaContabil;
  investidor: Investidor;

  constructor(investidor?: Investidor) {
    super();
    this.investidor = investidor;
  }
}
