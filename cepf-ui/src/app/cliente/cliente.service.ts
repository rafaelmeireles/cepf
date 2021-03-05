import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

import { BaseService } from '../../angular-framework/service/base-service';

import {Cliente} from './cliente';

@Injectable({
  providedIn: 'root'
})
export class ClienteService extends BaseService<Cliente> {

  constructor(httpClient: HttpClient) {
    super('/clientes', httpClient);
  }
}
