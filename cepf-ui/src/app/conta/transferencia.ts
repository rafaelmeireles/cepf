import {BaseEntity} from '../../angular-framework/model/base-entity';
import {User} from '../../angular-framework/security/user/user';

import {Favorecido} from './favorecido';
import {ContaCorrente} from './conta-corrente';

export class Transferencia extends BaseEntity {
  data: Date;
  favorecido: Favorecido;
  contaOrigem: ContaCorrente;
  valor: number;
  tarifa: number;
  responsavel: User;
}
