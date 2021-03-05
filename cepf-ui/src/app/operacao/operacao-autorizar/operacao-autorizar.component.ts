import {Component, OnInit} from '@angular/core';
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
import {NgForm} from '@angular/forms';
import {Banco} from '../../conta/banco';

@Component({
  selector: 'app-operacao-cancelar',
  templateUrl: './operacao-autorizar.component.html'
})
export class OperacaoAutorizarComponent extends BaseListController<Operacao> implements OnInit {

  private user: UserApplication;
  public selection: Operacao;
  public observableContas: Observable<ContaCorrente[]>;
  public observableBancos: Observable<Banco[]>;

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
    this.observableBancos = this.bancoService.findAll();

    if (this.user.investidor != null) {
      this.observableContas = this.contaCorrenteService.find(new ContaCorrente(this.user.investidor));
    } else {
      this.observableContas = this.contaCorrenteService.findAll();
    }
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

  public displayAutocompleteBanco(banco: Banco): string {
    if (banco != null) {
      return banco.nome;
    }
    return '';
  }

  public onFind(resultList: Operacao[]): void {
    resultList.forEach( operacao => {
      operacao.showExtraOperation = operacao.dataDeAutorizacao == null;
    });
    this.resultList = resultList;
  }

  public autorizar(operacao: Operacao, modal: string, form: NgForm): void {
    if (this.hasRequiredErrorInForm(form)) {
      this.error('Campos obrigatórios não preenchidos.');
      return;
    } else if (!operacao.transferencia.favorecido.banco) {
      this.error('Campo obrigatório \'banco\' não preenchido.');
      return;
    }

    operacao.transferencia.favorecido.cliente = operacao.cliente;
    operacao.transferencia.responsavel = this.user;

    this.getService().autorizar(operacao).subscribe(
      () => {
        this.find();
        this.hideModal(modal);
        this.success('Operação autorizada com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.hideModal(modal);
        this.messageService.handleError(error);
      });
  }
}
