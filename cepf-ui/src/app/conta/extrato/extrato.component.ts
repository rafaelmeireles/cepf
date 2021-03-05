import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {DatePipe} from '@angular/common';

import {Observable} from 'rxjs';

import {BaseListController} from '../../../angular-framework/base-controller/base-list-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';

import {LancamentoContabil} from '../lancamento-contabil';
import {LancamentoContabilService} from '../lancamento-contabil.service';

import {ContaContabil} from '../conta-contabil';
import {ContaContabilService} from '../conta-contabil.service';
import {TipoLancamentoContabil} from '../tipo-lancamento-contabil';
import {TipoLancamentoContabilService} from '../tipo-lancamento-contabil.service';
import {UserApplication} from '../../security/user-application';

@Component({
  selector: 'app-extrato',
  templateUrl: './extrato.component.html'
})
export class ExtratoComponent extends BaseListController<LancamentoContabil> implements OnInit {

  private contas: ContaContabil[] = [];
  private user: UserApplication;
  public observableContas: Observable<ContaContabil[]>;
  public observableTipoDeLancamento: Observable<TipoLancamentoContabil[]>;
  public dataInicial: Date;
  public dataFinal: Date;
  public totalCredito: number = 0;
  public totalDebito: number = 0;


  constructor(private lancamentoContabilService: LancamentoContabilService,
              private authService: AuthService,
              private contaContabilService: ContaContabilService,
              private tipoLancamentoContabilService: TipoLancamentoContabilService,
              private datePipe: DatePipe,
              messageService: MessageService,
              route: ActivatedRoute,
              router: Router) {
    super(LancamentoContabil, '/extrato', messageService, lancamentoContabilService, route, router);
    this.user = this.authService.getUser() as UserApplication;
  }

  ngOnInit() {
    super.ngOnInit();

    if (this.user.investidor != null) {
      this.observableContas = this.contaContabilService.find(new ContaContabil(this.user.investidor.pessoa.nome));
    } else {
      this.observableContas = this.contaContabilService.findAll();
    }

    this.observableTipoDeLancamento = this.tipoLancamentoContabilService.findAll();
  }

  protected createInstance(): void {
    super.createInstance();
    // this.instance.conta = new ContaContabil();
  }

  public displayAutocompleteConta(conta: ContaContabil): string {
    if (conta != null) {
      return conta.id + ' - ' + conta.nome;
    }
    return '';
  }

  public displayAutocompleteTipoLancamento(tipoLancamentoContabil: TipoLancamentoContabil): string {
    if (tipoLancamentoContabil != null) {
      return tipoLancamentoContabil.id + ' - ' + tipoLancamentoContabil.nome;
    }
    return '';
  }

  public find(): void {

    this.totalCredito = 0;
    this.totalDebito = 0;

    if (this.instance.conta == null) {
      this.error('Campo conta possui preenchimento obrigatório.');
      return;
    }

    const dataInicialFormated: string = this.datePipe.transform(this.dataInicial, 'dd/MM/yyyy');
    const dataFinalFormated: string = this.datePipe.transform(this.dataFinal, 'dd/MM/yyyy');

    if (!this.hasRequiredError()) {
      // this.lancamentoContabilService.extrato(this.instance, this.from, this.to).subscribe(
      this.lancamentoContabilService.extrato(this.instance, dataInicialFormated, dataFinalFormated).subscribe(
        (resultList: LancamentoContabil[]) => {
          if (!resultList || resultList.length === 0) {
            this.info('Nenhum registro foi encontrado');
            this.resultList = null;
          } else {
            this.resultList = resultList;
            this.calcularValores();
          }
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        }
      );
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

  private calcularValores(): void {
    let valorAposLancamento: number = 0;
    this.resultList.forEach( lancamentoContabil => {
      lancamentoContabil.valorAposLancamento = 0;
      if (lancamentoContabil.credito && lancamentoContabil.conta.credora) {
        this.totalCredito += lancamentoContabil.valor;
        valorAposLancamento += lancamentoContabil.valor;
      } else if (lancamentoContabil.credito && lancamentoContabil.conta.devedora) {
        this.totalCredito += lancamentoContabil.valor;
        valorAposLancamento -= lancamentoContabil.valor;
      } else if (lancamentoContabil.debito && lancamentoContabil.conta.credora) {
        this.totalDebito += lancamentoContabil.valor;
        valorAposLancamento -= lancamentoContabil.valor;
      } else if (lancamentoContabil.debito && lancamentoContabil.conta.devedora) {
        this.totalDebito += lancamentoContabil.valor;
        valorAposLancamento += lancamentoContabil.valor;
      }
      lancamentoContabil.valorAposLancamento = valorAposLancamento;
    });
  }

}
