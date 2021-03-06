import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {Observable} from 'rxjs';

import {BaseController} from '../../../angular-framework/base-controller/base-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';

import {Resgate} from '../resgate';
import {InvestidorService} from '../../investidor/investidor.service';
import {UserApplication} from '../../security/user-application';
import {ContaCorrenteService} from '../../conta/conta-corrente.service';
import {Investidor} from '../../investidor/investidor';
import {ContaCorrente} from '../../conta/conta-corrente';

@Component({
  selector: 'app-aporte-edit',
  templateUrl: './resgate-edit.component.html'
})
export class ResgateEditComponent extends BaseController<Resgate> implements OnInit {

  private user: UserApplication;
  public contas: Observable<ContaCorrente[]>;
  public observableInvestidores: Observable<Investidor[]>;
  public resgateDeRepasse: boolean = false;

  constructor(private investidorService: InvestidorService,
              private authService: AuthService,
              private contaCorrenteService: ContaCorrenteService,
              messageService: MessageService,
              route: ActivatedRoute,
              router: Router) {
    super(Resgate, messageService, null, route, router);
    this.user = this.authService.getUser() as UserApplication;
  }

  ngOnInit() {
    super.ngOnInit();
    this.observableInvestidores = this.investidorService.findAll();

    if (this.user.investidor != null) {
      this.contas = this.contaCorrenteService.find(new ContaCorrente(this.user.investidor));
    } else {
      this.contas = this.contaCorrenteService.findAll();
    }
  }

  protected createInstance(): void {
    super.createInstance();
    if (this.user.investidor != null) {
      this.instance.investidor = this.user.investidor;
    } else {
      this.instance.investidor = new Investidor();
      this.instance.investidor.contas = [];
    }
  }

  persist(): void {
    if (!this.hasRequiredError()) {
      this.investidorService.resgate(this.instance).subscribe(
        (status: number) => {
          this.success('Registro inserido com sucesso.');
          // this.createInstance();
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        });
    } else {
      this.error('Campos obrigat??rios n??o preenchidos.');
    }
  }

  public displayAutocompleteInvestidor(investidor: Investidor): string {
    if (investidor != null) {
      return investidor.id + ' - ' + investidor.pessoa.nome;
    }
    return '';
  }

}


