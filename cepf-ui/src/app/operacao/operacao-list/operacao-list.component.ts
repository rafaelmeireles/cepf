import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DatePipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {NgForm} from '@angular/forms';

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
import {Banco} from '../../conta/banco';
import {BancoService} from '../../conta/banco.service';
import {Favorecido} from '../../conta/favorecido';
import {UtilService} from '../../../angular-framework/service/util-service';
import {RepasseMensal} from '../repasse-mesal';
import {Parcela} from '../../parcela/parcela';

@Component({
  selector: 'app-operacao-list',
  templateUrl: './operacao-list.component.html'
})
export class OperacaoListComponent extends BaseListController<Operacao> implements OnInit {

  private user: UserApplication;
  public observableClientes: Observable<Cliente[]>;
  public observableInvestidores: Observable<Investidor[]>;
  private observableContas: Observable<ContaCorrente[]>;
  private observableBancos: Observable<Banco[]>;
  public dataInicial: Date;
  public dataFinal: Date;
  private tiposDeOperacaoSelecionados: TipoDeOperacao[] = [];
  private tiposDePagamentoSelecionados: TipoDePagamento[] = [];
  private contaCorrente: ContaCorrente;
  private repasseMensal: RepasseMensal;
  private gerarProximoRepasse: boolean;

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
    this.observableBancos = this.bancoService.findAll();
    // this.repasseMensal = new RepasseMensal();

    if (this.user.investidor != null) {
      this.observableContas = this.contaCorrenteService.find(new ContaCorrente(this.user.investidor));
    } else {
     this.observableContas = this.contaCorrenteService.findAll();
    }
    // this.find();
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

  public displayAutocompleteBanco(banco: Banco): string {
    if (banco != null) {
      return banco.nome;
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

              // if (operacao.dataQuitacao != null || operacao.temRepasseMensal) {
              //   operacao.resgateDeRepasse = new Resgate();
              // }

              // operacao.repassesMensais.forEach( (repasse, index) => {
              //   if (repasse.dataDoPagamento != null) {
              //     operacao.repassesMensais.splice(index, 1);
              //   }
              // });
            });
          }
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
    if(event.target.checked) {
      this.tiposDeOperacaoSelecionados.push(tipoDeOperacao);
    } else {
      this.tiposDeOperacaoSelecionados.splice(this.tiposDeOperacaoSelecionados.indexOf(tipoDeOperacao), 1);
    }
  }

  public onChangeTipoDePagamento(event, tipoDePagamento: TipoDePagamento) {
    if(event.target.checked) {
      this.tiposDePagamentoSelecionados.push(tipoDePagamento);
    } else {
      this.tiposDePagamentoSelecionados.splice(this.tiposDePagamentoSelecionados.indexOf(tipoDePagamento), 1);
    }
  }

  protected autorizar(operacao: Operacao, modal: string, form: NgForm): void {
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

  protected finalizar(operacao: Operacao, modal?: string, form?: NgForm): void {

    if (form != null && this.hasRequiredErrorInForm(form)) {
      this.error('Campos obrigatórios não preenchidos.');
      return;
    }

    this.getService().finalizar(operacao).subscribe(
      () => {
        this.find();
        if (modal != null) {
          this.hideModal(modal);
        }
        this.success('Operação finalizada com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  protected atualizarHistorico(operacao: Operacao): void {
    this.getService().update(operacao).subscribe(
      () => {
        this.find();
        this.success('Histórico atualizado com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  public openReceberSomenteJuros(operacao: Operacao): void {
    operacao.dataDePagamentoSomenteJuros = new Date();
    operacao.contaCorrentePagamentoSomenteJuros = new ContaCorrente();
  }

  public receberSomenteJuros(operacao: Operacao, modal: string, form: NgForm): void {
    if (this.hasRequiredErrorInForm(form)) {
      this.error('Campos obrigatórios não preenchidos.');
      return;
    }

    operacao.dataDePagamentoSomenteJuros = this.utilService.toLocalDate(operacao.dataDePagamentoSomenteJuros as Date);

    this.getService().receberSomenteJuros(operacao).subscribe(
      () => {
        this.hideModal(modal);
        this.find();
        if (operacao.repasse) {
          this.success('Pagamento somente do juros efetuado com sucesso. Favor efetuar o pagamento do repasse mensal.');
        } else {
          this.success('Pagamento somente do juros efetuado com sucesso.');
        }
      }, (error: HttpErrorResponse) => {
        operacao.dataDePagamentoSomenteJuros = null;
        this.hideModal(modal);
        this.messageService.handleError(error);
      });
  }

  public openPagarRepasseMensal(operacao: Operacao): void {
    this.repasseMensal = new RepasseMensal();
    this.repasseMensal.gerarProximoRepasse = operacao.pagamentoSomenteJuros;
  }

  public pagarRepasseMensal(modal: string, form: NgForm): void {

    if (this.hasRequiredErrorInForm(form)) {
      this.error('Campos obrigatórios não preenchidos.');
      return;
    }

    if (this.repasseMensal == null) {
      this.error('É necessário informar qual repasse será pago.');
      return;
    } else {
      if (this.repasseMensal.dataDoPagamento != null) {
        this.error('O repasse selecionado já foi pago.');
        return;
      }
      if (!this.repasseMensal.vencendo && !this.repasseMensal.vencido) {
        this.error('Repasse fora da data de vencimento.');
        return;
      }
      this.repasseMensal.contaCorrente = this.contaCorrente;
    }

    this.getService().pagarRepasseMensal(this.repasseMensal).subscribe(
      () => {
        this.find();
        this.hideModal(modal);
        this.success('Repasse Mensal pago com sucesso.');
        this.verificarQuitacaoDeOperacao(this.repasseMensal);
        this.repasseMensal = null;
        this.contaCorrente = null;
      }, (error: HttpErrorResponse) => {
        this.hideModal(modal);
        this.repasseMensal = null;
        this.contaCorrente = null;
        this.messageService.handleError(error);
      });
  }

  public verificarQuitacaoDeOperacao(repasse: RepasseMensal): void {
    this.operacaoService.findById(repasse.codigoOperacao).subscribe(
      operacao => {
        if (operacao.dataQuitacao != null && !operacao.pagamentoSomenteJuros) {
          this.success('Último repasse pago, favor finalizar a operação.');
        }
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  public cancelar(operacao: Operacao): void {
    this.getService().cancelar(operacao).subscribe(
      () => {
        this.find();
        this.success('Operação cancelada com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  // private _opened: boolean = false;

}
