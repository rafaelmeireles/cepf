import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {BaseEditController} from '../../../base-controller/base-edit-controller';
import {MessageService} from '../../../service/message-service';

import {Role} from '../role';
import {RoleService} from '../role.service';

@Component({
  selector: 'af-role-edit',
  templateUrl: './role-edit.component.html',
  styleUrls: ['./role-edit.component.css']
})
export class RoleEditComponent extends BaseEditController<Role> implements OnInit {

  constructor(messageService: MessageService,
              service: RoleService,
              route: ActivatedRoute,
              router: Router) {
    super(Role, messageService, service, route, router);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  protected createInstance(): void {
    super.createInstance();
    this.instance.active = true;
  }

}
