import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';

import {JwtHelperService} from '@auth0/angular-jwt';

import {Md5} from 'ts-md5';

import {User} from './user/user';
import {HOST, ROOT_RESOURCE} from '../../app/api';
import {BaseService} from '../service/base-service';
import {RoleEnum} from './role/role.enum';

@Injectable({providedIn: 'root'})
export class AuthService extends BaseService<User> {

  constructor(private jwtHelperService: JwtHelperService, httpClient: HttpClient) {
    super('/auth', httpClient);
  }

  public login(user: User): Observable<User> {
    return this.httpClient.post<User>(HOST + ROOT_RESOURCE + '/auth/signin',
      new User(user.username, new Md5().appendStr(user.password).end().toString()));

    // return this.httpClient.post<User>(HOST + ROOT_RESOURCE + '/auth/signin',
    //   new User(user.username, new Md5().appendStr(user.password).end().toString())).pipe(
    //     map( user => {
    //       this.storeAuthInformation(user);
    //       return user;
    //       }
    //     ));
  }

  public storeAuthInformation(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('token', user.token);
  }

  public logout(): any {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  }

  public getUser(): User {
    return JSON.parse(localStorage.getItem('user'));
  }

  public hasRole(role: string): boolean {
    return this.getUser().roles.find(userRole => userRole.code === RoleEnum.ADMINISTRADOR || userRole.code === role) !== undefined;
  }

  public isLoggedIn(): boolean {
    return !this.jwtHelperService.isTokenExpired();
  }

  public authorize(rolesRequired: string[]): boolean {
    // verify if some role was passaed, if yes, verify the authorization
    if (rolesRequired !== undefined) {
      let hasRole: boolean = false;

      rolesRequired.forEach(role => {
        if (this.hasRole(role)) {
          hasRole = true;
          return;
        }
      });
      return hasRole;
    } else {
      // is logged, so verify if some role was passed, if no, be authenticated is enough
      return true;
    }
  }
}
