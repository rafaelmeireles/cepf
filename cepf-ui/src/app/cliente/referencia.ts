import {BaseEntity} from '../../angular-framework/model/base-entity';

export class Referencia extends BaseEntity {
  nome: string;
  telefone: string;

  constructor(nome?: string, telefone?: string) {
    super();
    this.nome = nome;
    this.telefone = telefone;
  }
}
