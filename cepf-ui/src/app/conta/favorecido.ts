import {BaseEntity} from '../../angular-framework/model/base-entity';

import {Banco} from './banco';
import {Cliente} from '../cliente/cliente';

export class Favorecido extends BaseEntity {
  banco: Banco;
  agencia: number;
  digitoAgencia: number;
  conta: number;
  digitoConta: number;
  nome: string;
  cpf: number;
  cnpj: number;
  cliente: Cliente;
}
