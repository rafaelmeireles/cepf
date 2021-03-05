import {AfterViewInit, Component, OnInit, Renderer2} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DatePipe} from '@angular/common';

import {Observable, Subject} from 'rxjs';

import {BaseEditController} from '../../../angular-framework/base-controller/base-edit-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';
import {UtilService} from '../../../angular-framework/service/util-service';

import {Operacao} from '../operacao';
import {OperacaoService} from '../operacao.service';
import {Cliente} from '../../cliente/cliente';
import {ClienteService} from '../../cliente/cliente.service';
import {UserApplication} from '../../security/user-application';
import {EnumService} from '../../enum-service';
import {Investidor} from '../../investidor/investidor';
import {InvestidorService} from '../../investidor/investidor.service';
import {TipoDePagamento} from '../tipo-pagamento';
import {TipoDeOperacao} from '../tipo-operacao';

@Component({
  selector: 'app-operacao-edit',
  templateUrl: './operacao-edit.component.html',
  styleUrls: ['./operacao-edit.component.css']
})
export class OperacaoEditComponent extends BaseEditController<Operacao> implements OnInit, AfterViewInit {

  private user: UserApplication;
  public investidores: Observable<Investidor[]>;
  public observableClientes: Observable<Cliente[]>;
  public changeEventCliente: Subject<Cliente> = new Subject<Cliente>();
  public operacaoPropria: boolean;
  public valorTotalDasParcelas: number = 0;
  public valorTotalPagoDasParcelas: number = 0;
  public valorTotalAgio: number = 0;
  public valorTotalDosRepasses: number = 0;

  constructor(private clienteService: ClienteService,
              private authService: AuthService,
              public enumService: EnumService,
              private investidorService: InvestidorService,
              private utilService: UtilService,
              private datePipe: DatePipe,
              private renderer: Renderer2,
              messageService: MessageService,
              service: OperacaoService,
              route: ActivatedRoute,
              router: Router) {
    super(Operacao, messageService, service, route, router);
    this.user = this.authService.getUser() as UserApplication;
  }

  ngOnInit() {
    super.ngOnInit();
    this.observableClientes = this.clienteService.find(new Cliente(this.user.investidor));
    this.investidores = this.investidorService.findAll();
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
    const autocompleteCliente: any = this.renderer.selectRootElement('#autocompleteCliente');
    autocompleteCliente.focus();
  }

  protected createInstance(): void {
    super.createInstance();
    this.instance.responsavel = this.user;
    if (this.user.investidor) {
      this.instance.investidor = this.user.investidor;
    }
    this.instance.data = new Date();
  }

  protected getService(): OperacaoService {
    return this.service as OperacaoService;
  }

  public gerarParcelas(): void {
    this.instance.cliente.linkWhatsApp = null;

    if (!this.hasRequiredError()) {
      this.getService().gerarParcelas(this.instance);
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

  public onChangeTipoDePagamento(tipoDePagamento: TipoDePagamento): void {
    if (TipoDePagamento.SOMENTE_JUROS === tipoDePagamento) {
      this.instance.quantidadeDeParcelas = 1;
    }
  }

  public onChangeTipoDeOperacao(tipoDeOperacao: TipoDeOperacao): void {
    this.operacaoPropria = TipoDeOperacao.PROPRIA === tipoDeOperacao;
  }

  public isSomenteJuros(): boolean {
    return TipoDePagamento.SOMENTE_JUROS === this.instance.tipoDePagamento;
  }

  public displayAutocompleteCliente(cliente: Cliente): string {
    if (cliente != null) {
      return cliente.id + ' - ' + cliente.pessoa.nome;
    }
    return '';
  }

  public persist(): void {
    if (this.instance.cliente == null) {
      this.error('Campo cliente possui preenchimento obrigatório.');
      return;
    }
    super.persist();
  }

  protected completeUpdateOrView(): void {
    this.instance.data = this.utilService.toDate(this.instance.data.toString());
    this.emitChangeEventCliente(this.instance.cliente);

    this.instance.parcelas.forEach( parcela => {
      this.valorTotalDasParcelas += parcela.valor;
      this.valorTotalPagoDasParcelas += parcela.valorPago;
    });

    this.instance.repassesMensais.forEach( repasse => {
      this.valorTotalAgio += repasse.agio;
      this.valorTotalDosRepasses += repasse.valor;
    });
  }

  private emitChangeEventCliente(cliente: Cliente): void {
    this.changeEventCliente.next(cliente);
  }
}
