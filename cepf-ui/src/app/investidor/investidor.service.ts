import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../angular-framework/service/base-service';

import {Investidor} from './investidor';
import {Aporte} from '../aporte/aporte';
import {Observable} from 'rxjs';
import {Resgate} from '../resgate/resgate';
import {ApuracaoResultado} from '../apuracao-resultado/apuracao-resultado';

@Injectable({
  providedIn: 'root'
})
export class InvestidorService extends BaseService<Investidor> {

  constructor(httpClient: HttpClient) {
    super('/investidores', httpClient);
  }

  public aporte(aporte: Aporte): Observable<number> {
    return this.httpClient.post<number>(this.url + '/aporte', aporte);
  }

  public resgate(resgate: Resgate): Observable<number> {
    return this.httpClient.post<number>(this.url + '/resgate', resgate);
  }

  public apurarResultado(apuracaoResultado: ApuracaoResultado): Observable<number> {
    return this.httpClient.post<number>(this.url + '/apuracao-resultado', apuracaoResultado);
  }
}
