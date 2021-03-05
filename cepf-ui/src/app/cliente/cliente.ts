import {BaseEntity} from '../../angular-framework/model/base-entity';

import {Pessoa} from '../pessoa/pessoa';
import {Investidor} from '../investidor/investidor';
import {Referencia} from './referencia';
import {ContaContabil} from '../conta/conta-contabil';

export class Cliente extends BaseEntity {
  taxa: number;
  pessoa: Pessoa;
  referencia: Referencia;
  investidor: Investidor;
  conta: ContaContabil;
  linkWhatsApp: string;

  constructor(investidor?: Investidor) {
    super();
    this.investidor = investidor;
  }
}
