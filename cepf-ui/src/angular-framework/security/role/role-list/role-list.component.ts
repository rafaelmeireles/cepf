import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {BaseListController} from '../../../base-controller/base-list-controller';

import {MessageService} from '../../../service/message-service';

import {Role} from '../role';
import {RoleService} from '../role.service';

@Component({
  selector: 'af-role-list',
  templateUrl: './role-list.component.html',
  styleUrls: ['./role-list.component.css']
})
export class RoleListComponent extends BaseListController<Role> implements OnInit {

  constructor(messageService: MessageService,
              service: RoleService,
              route: ActivatedRoute,
              router: Router) {
    super(Role, '/roles', messageService, service, route, router);
  }

  ngOnInit() {
    super.ngOnInit();
  }

}
