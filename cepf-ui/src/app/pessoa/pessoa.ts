import {BaseEntity} from '../../angular-framework/model/base-entity';

import {Endereco} from '../cliente/endereco';

export class Pessoa extends BaseEntity {
  cpf: string;
  nome: string;
  endereco: Endereco;
  telefonePrincipal: string;
  telefoneSecundario: string;
  email: string;

  constructor(cpf?: string) {
    super();
    this.cpf = cpf;
  }
}
