import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../angular-framework/service/base-service';

import {Banco} from './banco';

@Injectable({
  providedIn: 'root'
})
export class BancoService extends BaseService<Banco> {

  constructor(httpClient: HttpClient) {
    super('/bancos', httpClient);
  }
}
