import {BaseEntity} from '../../angular-framework/model/base-entity';

import {Pessoa} from '../pessoa/pessoa';
import {ContaCorrente} from '../conta/conta-corrente';

export class Investidor extends BaseEntity {
  pessoa: Pessoa;
  contas: ContaCorrente[];

  constructor(cpf?: string) {
    super();
    this.pessoa = new Pessoa(cpf);
  }
}
