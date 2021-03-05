import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
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
import {Transferencia} from '../../conta/transferencia';
import {BancoService} from '../../conta/banco.service';
import {Favorecido} from '../../conta/favorecido';
import {UtilService} from '../../../angular-framework/service/util-service';

@Component({
  selector: 'app-operacao-list-filters',
  templateUrl: './operacao-list-filters.component.html'
})
export class OperacaoListFiltersComponent extends BaseListController<Operacao> implements OnInit {

  public user: UserApplication;
  public observableClientes: Observable<Cliente[]>;
  public observableInvestidores: Observable<Investidor[]>;
  public dataInicial: Date;
  public dataFinal: Date;
  private tiposDeOperacaoSelecionados: TipoDeOperacao[] = [];
  private tiposDePagamentoSelecionados: TipoDePagamento[] = [];

  @Input() public insert: boolean = true;
  @Output() public onFind: EventEmitter<Operacao[]> = new EventEmitter<Operacao[]>();

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
    this.observableClientes = this.clienteService.find(new Cliente(this.user.investidor));
    this.observableInvestidores = this.investidorService.findAll();
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

  public displayAutocompleteCliente(cliente: Cliente): string {
    if (cliente != null) {
      return cliente.id + ' - ' + cliente.pessoa.nome;
    }
    return '';
  }

  public displayAutocompleteInvestidor(investidor: Investidor): string {
    if (investidor != null) {
      return investidor.id + ' - ' + investidor.pessoa.nome;
    }
    return '';
  }

  public find(): void {
    // if (this.dataInicial == null && this.dataFinal == null) {
    //   this.error('Pelo menos uma das datas deve ser informada.');
    //   return;
    // }

    if (!this.hasRequiredError()) {
      this.getService().findOperacao(
          this.instance, this.dataInicial, this.dataFinal,
          this.tiposDeOperacaoSelecionados, this.tiposDePagamentoSelecionados).subscribe(
        (resultList: Operacao[]) => {
          if (!resultList || resultList.length === 0) {
            this.info('Nenhum registro foi encontrado');
            this.resultList = null;
          } else {
            this.resultList = resultList;
            this.resultList.forEach( operacao => {
              if (operacao.dataDeAutorizacao == null) {
                operacao.transferencia = new Transferencia();
                operacao.transferencia.favorecido = new Favorecido();
              }

              if (operacao.pagamentoSomenteJuros) {
                operacao.contaCorrentePagamentoSomenteJuros = new ContaCorrente();
                if (operacao.repasse) {
                  operacao.contaCorrentePagamentoDoRepasseSomenteJuros = new ContaCorrente();
                  operacao.repassesMensais.forEach( repasse => {
                    if (repasse.dataDoPagamento == null) {
                      repasse.gerarProximoRepasse = true;
                    }
                  });
                }
              }
            });
          }
          this.onFind.emit(resultList);
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
          console.log(error);
        }
      );
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

  public onChangeTipoDeOperacao(event, tipoDeOperacao: TipoDeOperacao) {
    if (event.target.checked) {
      this.tiposDeOperacaoSelecionados.push(tipoDeOperacao);
    } else {
      this.tiposDeOperacaoSelecionados.splice(this.tiposDeOperacaoSelecionados.indexOf(tipoDeOperacao), 1);
    }
  }

  public onChangeTipoDePagamento(event, tipoDePagamento: TipoDePagamento) {
    if (event.target.checked) {
      this.tiposDePagamentoSelecionados.push(tipoDePagamento);
    } else {
      this.tiposDePagamentoSelecionados.splice(this.tiposDePagamentoSelecionados.indexOf(tipoDePagamento), 1);
    }
  }

}
