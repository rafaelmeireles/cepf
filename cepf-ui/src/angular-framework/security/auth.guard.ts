import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, NavigationEnd, Router, RouterEvent} from '@angular/router';

import {MessageService} from '../service/message-service';
import {AuthService} from './auth-service';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private messageService: MessageService,
    private authService: AuthService) {
  }

  canActivate(route: ActivatedRouteSnapshot) {
    if (this.authService.isLoggedIn()) {
      if (!this.authService.authorize(route.data.roles)) {
        this.messageService.error('Você não possui permissão para acessar a funcionalidade deseja.');
        this.router.navigate(['/']);
        return false;
      }
      return true;
    } else {
      // not logged in so redirect to login page with the return url
      // https://github.com/cornflourblue/angular-7-jwt-authentication-example/blob/master/src/app/_guards/auth.guard.ts
      // this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
      // return false;

      this.messageService.error('Sua sessão expirou, favor realizar o login novamente.');
      this.authService.logout();
      this.router.navigate(['/login']);
      return false;
    }
  }

}
