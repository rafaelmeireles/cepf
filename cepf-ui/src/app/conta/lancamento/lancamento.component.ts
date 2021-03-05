import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {Observable} from 'rxjs';

import {BaseController} from '../../../angular-framework/base-controller/base-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';

import {UserApplication} from '../../security/user-application';
import {LancamentoContabil} from '../lancamento-contabil';
import {LancamentoContabilService} from '../lancamento-contabil.service';
import {ContaContabil} from '../conta-contabil';
import {ContaContabilService} from '../conta-contabil.service';
import {TipoLancamentoContabil} from '../tipo-lancamento-contabil';
import {TipoLancamentoContabilService} from '../tipo-lancamento-contabil.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-lancamento',
  templateUrl: './lancamento.component.html'
})
export class LancamentoComponent extends BaseController<LancamentoContabil> implements OnInit {
  private user: UserApplication;
  public observableContaContabilCredito: Observable<ContaContabil[]>;
  public observableContaContabilDebito: Observable<ContaContabil[]>;
  public observableTipoDeLancamento: Observable<TipoLancamentoContabil[]>;

  constructor(private lancamentoContabilService: LancamentoContabilService,
              private authService: AuthService,
              private contaContabilService: ContaContabilService,
              private tipoLancamentoContabilService: TipoLancamentoContabilService,
              messageService: MessageService,
              route: ActivatedRoute,
              router: Router) {
    super(LancamentoContabil, messageService, lancamentoContabilService, route, router);
    this.user = this.authService.getUser() as UserApplication;
  }

  ngOnInit() {
    super.ngOnInit();
    this.instance.contraPartida = new LancamentoContabil();
    this.observableContaContabilCredito = this.contaContabilService.findAll();
    this.observableContaContabilDebito = this.contaContabilService.findAll();
    this.observableTipoDeLancamento = this.tipoLancamentoContabilService.findAll();
  }

  protected createInstance(): void {
    super.createInstance();
    this.instance.dataDeReferencia = new Date();
    this.instance.responsavel = this.user;
  }

  public lancamentoManual(): void {
    this.lancamentoContabilService.lancamentoManual(this.instance).subscribe(
      () => {
        this.success('Registro inserido com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  public displayAutocompleteContaCredito(contaCredito: ContaContabil): string {
    if (contaCredito != null) {
      return contaCredito.id + ' - ' + contaCredito.nome;
    }
    return '';
  }

  public displayAutocompleteContaDebito(contaCredito: ContaContabil): string {
    if (contaCredito != null) {
      return contaCredito.id + ' - ' + contaCredito.nome;
    }
    return '';
  }

  public displayAutocompleteTipoLancamento(tipoLancamentoContabil: TipoLancamentoContabil): string {
    if (tipoLancamentoContabil != null) {
      return tipoLancamentoContabil.id + ' - ' + tipoLancamentoContabil.nome;
    }
    return '';
  }

}


