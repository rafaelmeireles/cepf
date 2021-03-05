import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable} from 'rxjs';

import {BaseEditController} from '../../../angular-framework/base-controller/base-edit-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';

import {ClienteService} from '../cliente.service';
import {Cliente} from '../cliente';
import {Endereco} from '../endereco';
import {EnumService} from '../../enum-service';
import {Pessoa} from '../../pessoa/pessoa';
import {Investidor} from '../../investidor/investidor';
import {UserApplication} from '../../security/user-application';
import {InvestidorService} from '../../investidor/investidor.service';
import {Referencia} from '../referencia';
import {HttpErrorResponse} from '@angular/common/http';
import {BaseError} from '../../../angular-framework/error/base-error';
import {UtilService} from '../../../angular-framework/service/util-service';
import {PessoaService} from '../../pessoa/pessoa.service';

@Component({
  selector: 'app-cliente-edit',
  templateUrl: './cliente-edit.component.html',
  styleUrls: ['./cliente-edit.component.css']
})
export class ClienteEditComponent extends BaseEditController<Cliente> implements OnInit {

  private user: UserApplication;
  private investidores: Observable<Investidor[]>;

  constructor(public enumService: EnumService,
              private authService: AuthService,
              private investidorService: InvestidorService,
              private utilService: UtilService,
              private pessoaService: PessoaService,
              messageService: MessageService,
              service: ClienteService,
              route: ActivatedRoute,
              router: Router) {
    super(Cliente, messageService, service, route, router);
  }

  ngOnInit() {
    super.ngOnInit();
    this.user = this.authService.getUser() as UserApplication;

    this.investidores = this.investidorService.findAll();

    if (this.user.investidor != null) {
      this.instance.investidor = this.user.investidor;
    }
  }

  public hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }

  protected createInstance(): void {
    super.createInstance();
    this.instance.pessoa = this.createPessoa();
    this.instance.referencia = new Referencia();
  }

  private createPessoa(): Pessoa {
    let pessoa: Pessoa = new Pessoa();
    pessoa.endereco = new Endereco();
    pessoa.endereco.tipoLogradouro = null;
    pessoa.endereco.uf = null;
    return pessoa;
  }

  public onChangeCpf(cpf: string): void {
    if (cpf) {
      cpf = this.utilService.onlyNumber(cpf);
      this.pessoaService.findOne(new Pessoa(cpf)).subscribe(
        (pessoa: Pessoa) => {
          if (pessoa != null) {
            this.instance.pessoa = pessoa;
          }
        }, (error: HttpErrorResponse | BaseError) => {
          this.messageService.handleError(error);
        });
    } else {
      this.instance.pessoa = this.createPessoa();
    }
  }
}
