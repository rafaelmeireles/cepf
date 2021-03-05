import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Router} from '@angular/router';

import {Observable} from 'rxjs';

import {AuthService} from './auth-service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService,
              private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
      this.router.navigate(['/login']);
    }
    return next.handle(request);
  }
}
