import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {MessageService} from '../../service/message-service';
import {BaseController} from '../../base-controller/base-controller';
import {User} from '../user/user';
import {AuthService} from '../auth-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent extends BaseController<User> implements OnInit {

  constructor(protected service: AuthService,
              messageService: MessageService,
              protected router: Router) {
    super(User, messageService);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  login(): void {
    if (!this.hasRequiredError()) {
      this.service.login(this.instance).subscribe(    user => {
        this.service.storeAuthInformation(user);
        this.success('Bem vindo, ' + user.username + '.');
        this.router.navigate(['/']);
      }, (error: HttpErrorResponse) => {
        this.service.logout();
        this.messageService.handleError(error);
      });
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

}
