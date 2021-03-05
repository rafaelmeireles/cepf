import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../service/base-service';

import {Role} from './role';

@Injectable({
  providedIn: 'root'
})
export class RoleService extends BaseService<Role> {

  constructor(httpClient: HttpClient) {
    super('/roles', httpClient);
  }
}
