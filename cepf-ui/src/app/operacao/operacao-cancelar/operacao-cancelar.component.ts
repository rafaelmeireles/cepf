import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DatePipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';

import {Observable} from 'rxjs';

import {BaseListController} from '../../../angular-framework/base-controller/base-list-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';

import {Investidor} from '../../investidor/investidor';
import {Operacao} from '../operacao';
import {OperacaoService} from '../operacao.service';
import {Cliente} from '../../cliente/cliente';
import {UserApplication} from '../../security/user-application';
import {ClienteService} from '../../cliente/cliente.service';
import {InvestidorService} from '../../investidor/investidor.service';
import {TipoDeOperacao} from '../tipo-operacao';
import {EnumService} from '../../enum-service';
import {TipoDePagamento} from '../tipo-pagamento';
import {ContaContabilService} from '../../conta/conta-contabil.service';
import {ContaCorrenteService} from '../../conta/conta-corrente.service';
import {ContaCorrente} from '../../conta/conta-corrente';
import {BancoService} from '../../conta/banco.service';
import {UtilService} from '../../../angular-framework/service/util-service';

// @Component({
//   selector: 'app-operacao-cancelar',
//   templateUrl: './operacao-cancelar.component.html'
// })
export class OperacaoCancelarComponent extends BaseListController<Operacao> implements OnInit {

  private user: UserApplication;
  private selection: Operacao;

  constructor(private operacaoService: OperacaoService,
              private authService: AuthService,
              private clienteService: ClienteService,
              private investidorService: InvestidorService,
              private contaCorrenteService: ContaCorrenteService,
              private contaContabilService: ContaContabilService,
              private bancoService: BancoService,
              public enumService: EnumService,
              private utilService: UtilService,
              private datePipe: DatePipe,
              messageService: MessageService,
              route: ActivatedRoute,
              router: Router) {
    super(Operacao, '/operacoes', messageService, operacaoService, route, router);
    this.user = this.authService.getUser() as UserApplication;
  }

  ngOnInit() {
    super.ngOnInit();
  }

  public hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }

  protected getService(): OperacaoService {
    return super.getService() as OperacaoService;
  }

  protected createInstance(): void {
    super.createInstance();
    if (this.user.investidor) {
      this.instance.responsavel = this.user;
      this.instance.investidor = this.user.investidor;
    }
  }

  public onFind(resultList: Operacao[]): void {
    resultList.forEach( operacao => {
      operacao.showExtraOperation = !operacao.cancelada;
    });
    this.resultList = resultList;
  }

  public cancelar(operacao: Operacao): void {
    console.log(operacao.id);
    // this.getService().cancelar(operacao).subscribe(
    //   () => {
    //     this.find();
    //     this.success('Operação cancelada com sucesso.');
    //   }, (error: HttpErrorResponse) => {
    //     this.messageService.handleError(error);
    //   });
  }
}
