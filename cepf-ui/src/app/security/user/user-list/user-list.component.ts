import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {BaseListController} from '../../../../angular-framework/base-controller/base-list-controller';
import {UserService} from '../../../../angular-framework/security/user/user.service';
import {MessageService} from '../../../../angular-framework/service/message-service';

import {UserApplication} from '../../user-application';

@Component({
  selector: 'af-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent extends BaseListController<UserApplication> implements OnInit {

  private userService: UserService<UserApplication>;

  constructor(messageService: MessageService,
              service: UserService<UserApplication>,
              route: ActivatedRoute,
              router: Router) {
    super(UserApplication, '/users', messageService, service, route, router);
    this.userService = service;
  }

  ngOnInit() {
    super.ngOnInit();
  }

  protected createInstance(): void {
    super.createInstance();
  }

  protected resetPassword(user: UserApplication): void {
    this.userService.resetPassword(user).subscribe(
      () => {
        this.success('Senha alterada com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

}
