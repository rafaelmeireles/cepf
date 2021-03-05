import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {UserService} from '../user.service';
import {MessageService} from '../../../service/message-service';
import {BaseController} from '../../../base-controller/base-controller';
import {AuthService} from '../../auth-service';
import {UserApplication} from '../../../../app/security/user-application';

@Component({
  selector: 'af-user-reset-password',
  templateUrl: './user-reset-password.component.html'
})
export class UserResetPasswordComponent extends BaseController<UserApplication> implements OnInit {

  public confirmPassword: string;
  public userService: UserService<UserApplication>;

  constructor(messageService: MessageService,
              service: UserService<UserApplication>,
              private authService: AuthService,
              route: ActivatedRoute,
              router: Router) {
    super(UserApplication, messageService, service, route, router);
    this.userService = service;
  }

  ngOnInit() {
    super.ngOnInit();
    this.instance = this.authService.getUser() as UserApplication;
  }

  public resetPassword(): void {

    if (this.instance.newPassword !== this.confirmPassword) {
      this.error('As senhas informadas estão diferentes.');
      return;
    }

    if (!this.hasRequiredError()) {
      this.userService.changePassword(this.instance).subscribe(
        () => {
          this.success('Senha alterada com sucesso.');
          this.clearInstance();
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        });
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

}
