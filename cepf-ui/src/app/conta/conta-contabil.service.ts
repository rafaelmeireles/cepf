import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../angular-framework/service/base-service';

import {ContaContabil} from './conta-contabil';

@Injectable({
  providedIn: 'root'
})
export class ContaContabilService extends BaseService<ContaContabil> {

  constructor(httpClient: HttpClient) {
    super('/contas-contabeis', httpClient);
  }
}
