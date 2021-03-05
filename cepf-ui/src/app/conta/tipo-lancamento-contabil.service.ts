import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../angular-framework/service/base-service';

import {TipoLancamentoContabil} from './tipo-lancamento-contabil';

@Injectable({
  providedIn: 'root'
})
export class TipoLancamentoContabilService extends BaseService<TipoLancamentoContabil> {

  constructor(httpClient: HttpClient) {
    super('/lancamentos-contabeis/tipos', httpClient);
  }
}
