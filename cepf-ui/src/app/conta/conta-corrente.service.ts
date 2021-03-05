import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../angular-framework/service/base-service';

import {ContaCorrente} from './conta-corrente';

@Injectable({
  providedIn: 'root'
})
export class ContaCorrenteService extends BaseService<ContaCorrente> {

  constructor(httpClient: HttpClient) {
    super('/contas-corrente', httpClient);
  }
}
