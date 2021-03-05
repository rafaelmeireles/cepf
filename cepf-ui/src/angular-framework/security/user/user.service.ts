import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {BaseService} from '../../service/base-service';

import {User} from './user';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService<T extends User> extends BaseService<T> {

  constructor(httpClient: HttpClient) {
    super('/users', httpClient);
  }

  public resetPassword(entity: T): Observable<void> {
    return this.httpClient.put<void>(this.url + '/reset-password', entity);
  }

  public changePassword(entity: T): Observable<void> {
    return this.httpClient.put<void>(this.url + '/change-password', entity);
  }
}
