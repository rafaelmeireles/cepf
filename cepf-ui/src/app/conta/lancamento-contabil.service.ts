import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../angular-framework/service/base-service';

import {LancamentoContabil} from './lancamento-contabil';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LancamentoContabilService extends BaseService<LancamentoContabil> {

  constructor(httpClient: HttpClient) {
    super('/lancamentos-contabeis', httpClient);
  }

  public extrato(entity: LancamentoContabil, from: string, to: string): Observable<LancamentoContabil[]> {

    let url = this.url + '/extrato';
    url = url + '?entity=' + encodeURI(JSON.stringify(entity));

    if (from != null) {
      url = url + '&from=' + from;
    }

    if (to != null) {
      url = url + '&to=' + to;
    }

    return this.httpClient.get<LancamentoContabil[]>(url);
  }

  public lancamentoManual(lancamentoContabil: LancamentoContabil): Observable<void> {
    const url = this.url + '/lancamento-manual';
    return this.httpClient.post<void>(url, lancamentoContabil);
  }
}
