import {BaseEntity} from '../../angular-framework/model/base-entity';

import {ContaCorrente} from '../conta/conta-corrente';
import {Investidor} from '../investidor/investidor';

export class Aporte extends BaseEntity {
  investidor: Investidor;
  valor: number;
  contaCorrente: ContaCorrente;
  investidorSenior: Investidor;
}
