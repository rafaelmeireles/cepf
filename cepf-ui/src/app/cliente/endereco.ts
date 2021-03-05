import {BaseEntity} from '../../angular-framework/model/base-entity';

import {TipoLogradouro} from './tipo-logradouro';
import {Uf} from './uf';

export class Endereco extends BaseEntity {
  tipoLogradouro: TipoLogradouro;
  logradouro: string;
  numero: number;
  complemento: string;
  bairro: string;
  cidade: string;
  uf: Uf;
  cep: string;
}
