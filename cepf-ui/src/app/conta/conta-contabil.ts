import {BaseEntity} from '../../angular-framework/model/base-entity';

export class ContaContabil extends BaseEntity {
  nome: string;
  saldo: number;
  credora: boolean;
  devedora: boolean;

  constructor(nome?: string) {
    super();
    this.nome = nome;
  }
}
