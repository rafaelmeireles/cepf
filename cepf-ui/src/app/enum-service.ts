import {Injectable} from '@angular/core';

import {TipoLogradouro} from './cliente/tipo-logradouro';
import {Uf} from './cliente/uf';
import {TipoDeOperacao} from './operacao/tipo-operacao';
import {TipoDePagamento} from './operacao/tipo-pagamento';

@Injectable({providedIn: 'root'})
export class EnumService {

  public tipoLogradouro: TipoLogradouro[] = [];
  public uf: Uf[] = [];
  public tipoDeOperacao: TipoDeOperacao[] = [];
  public tipoDePagamento: TipoDePagamento[] = [];

  protected constructor() {
    Object.keys(TipoLogradouro).forEach( key => this.tipoLogradouro.push(TipoLogradouro[key]));
    Object.keys(Uf).forEach( key => this.uf.push(Uf[key]));
    Object.keys(TipoDeOperacao).forEach( key => this.tipoDeOperacao.push(TipoDeOperacao[key]));
    Object.keys(TipoDePagamento).forEach( key => this.tipoDePagamento.push(TipoDePagamento[key]));
  }
}
