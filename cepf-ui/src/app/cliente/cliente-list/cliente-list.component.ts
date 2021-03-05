import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {MessageService} from '../../../angular-framework/service/message-service';
import {BaseListController} from '../../../angular-framework/base-controller/base-list-controller';

import {Cliente} from '../cliente';
import {ClienteService} from '../cliente.service';
import {Pessoa} from '../../pessoa/pessoa';
import {UserApplication} from '../../security/user-application';
import {AuthService} from '../../../angular-framework/security/auth-service';

@Component({
  selector: 'app-cliente-list',
  templateUrl: './cliente-list.component.html',
  styleUrls: ['./cliente-list.component.css']
})
export class ClienteListComponent extends BaseListController<Cliente> implements OnInit {

  constructor(private authService: AuthService,
              messageService: MessageService,
              service: ClienteService,
              route: ActivatedRoute,
              router: Router) {
    super(Cliente, '/clientes', messageService, service, route, router);
  }

  ngOnInit() {
    super.ngOnInit();
    this.instance.investidor = (this.authService.getUser() as UserApplication).investidor;
  }

  protected createInstance(): void {
    super.createInstance();
    this.instance.pessoa = new Pessoa();
  }
}
