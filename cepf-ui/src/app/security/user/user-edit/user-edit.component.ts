import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {Observable} from 'rxjs';

import {BaseEditController} from '../../../../angular-framework/base-controller/base-edit-controller';
import {UserService} from '../../../../angular-framework/security/user/user.service';
import {Role} from '../../../../angular-framework/security/role/role';
import {UtilService} from '../../../../angular-framework/service/util-service';
import {RoleService} from '../../../../angular-framework/security/role/role.service';
import {MessageService} from '../../../../angular-framework/service/message-service';

import {UserApplication} from '../../user-application';
import {Investidor} from '../../../investidor/investidor';
import {InvestidorService} from '../../../investidor/investidor.service';

@Component({
  selector: 'af-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent extends BaseEditController<UserApplication> implements OnInit {

  public role: Role;
  public roles: Observable<Role[]>;
  public investidores: Observable<Investidor[]>;

  constructor(private investidorService: InvestidorService,
              private roleService: RoleService,
              private utilService: UtilService,
              messageService: MessageService,
              service: UserService<UserApplication>,
              route: ActivatedRoute,
              router: Router) {
    super(UserApplication, messageService, service, route, router);
  }

  ngOnInit() {
    super.ngOnInit();
    this.investidores = this.investidorService.findAll();
    this.roles = this.roleService.findAll();
  }

  persist(): void {
    if (this.instance.investidor !== undefined) {
      this.instance.cpf = this.instance.investidor.pessoa.cpf;
    }
    super.persist();
  }

  protected createInstance(): void {
    this.instance = new UserApplication();

    this.instance.active = true;
    this.instance.roles = [];
  }

  public openModal(): void {
    this.role = null;
  }

  public addRole(): void {
    if (this.role == null) {
      this.error('Nenhum papel foi selecionado.');
    } else {
      this.instance.roles.push(this.role);
    }
  }

  private deleteRole(role: Role): void {
    this.utilService.remove(role).from(this.instance.roles);
  }

  public onChangeInvestidor(investidor: Investidor): void {
    if (investidor !== undefined && investidor.pessoa !== undefined) {
      this.instance.cpf = investidor.pessoa.cpf;
    } else {
      this.instance.cpf = null;
    }
  }

}
