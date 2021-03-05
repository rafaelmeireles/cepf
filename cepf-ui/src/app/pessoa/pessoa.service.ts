import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../angular-framework/service/base-service';
import {Pessoa} from './pessoa';

@Injectable({
  providedIn: 'root'
})
export class PessoaService extends BaseService<Pessoa> {

  constructor(httpClient: HttpClient) {
    super('/pessoas', httpClient);
  }
}
