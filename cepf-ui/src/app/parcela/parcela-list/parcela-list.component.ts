import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DatePipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';
import {NgForm} from '@angular/forms';

import {Observable} from 'rxjs';

import {BaseListController} from '../../../angular-framework/base-controller/base-list-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';
import {UtilService} from '../../../angular-framework/service/util-service';

import {Parcela} from '../parcela';
import {ParcelaService} from '../parcela.service';
import {Cliente} from '../../cliente/cliente';
import {ClienteService} from '../../cliente/cliente.service';
import {UserApplication} from '../../security/user-application';
import {ContaCorrente} from '../../conta/conta-corrente';
import {ContaCorrenteService} from '../../conta/conta-corrente.service';
import {OperacaoService} from '../../operacao/operacao.service';
import {Operacao} from '../../operacao/operacao';
import {TipoDeOperacao} from '../../operacao/tipo-operacao';

@Component({
  selector: 'app-parcela-list',
  templateUrl: './parcela-list.component.html'
})
export class ParcelaListComponent extends BaseListController<Parcela> implements OnInit {

  private user: UserApplication;
  public dataVencimentoInicial: Date;
  public dataVencimentoFinal: Date;
  public dataPagamentoInicial: Date;
  public dataPagamentoFinal: Date;
  public parcelasEmAberto: boolean;
  public dataProrrogacao: Date;
  public dataDoPagamento: Date;

  public observableClientes: Observable<Cliente[]>;
  public observableContas: Observable<ContaCorrente[]>;

  constructor(private parcelaService: ParcelaService,
              private operacaoService: OperacaoService,
              private authService: AuthService,
              private clienteService: ClienteService,
              private contaCorrenteService: ContaCorrenteService,
              private datePipe: DatePipe,
              private utilService: UtilService,
              messageService: MessageService,
              route: ActivatedRoute,
              router: Router) {
    super(Parcela, '/parcelas', messageService, parcelaService, route, router);
    this.user = this.authService.getUser() as UserApplication;
  }

  ngOnInit() {
    super.ngOnInit();
    if (this.user.investidor != null) {// usuario com investidor pode ser administrador
      this.observableClientes = this.clienteService.find(new Cliente(this.user.investidor));
      this.observableContas = this.contaCorrenteService.find(new ContaCorrente(this.user.investidor));
    } else {
      this.observableClientes = this.clienteService.findAll();
      this.observableContas = this.contaCorrenteService.findAll();
    }
    this.parcelasEmAberto = true;
    this.dataVencimentoFinal = new Date();
    this.find();
  }

  protected getService(): ParcelaService {
    return super.getService() as ParcelaService;
  }

  public find(): void {

    this.dataProrrogacao = null;
    const dataVencimentoInicialFormatted: string = this.datePipe.transform(this.dataVencimentoInicial, 'dd/MM/yyyy');
    const dataVencimentoFinalFormatted: string = this.datePipe.transform(this.dataVencimentoFinal, 'dd/MM/yyyy');
    const dataPagamentoInicialFormatted: string = this.datePipe.transform(this.dataPagamentoInicial, 'dd/MM/yyyy');
    const dataPagamentoFinalFormatted: string = this.datePipe.transform(this.dataPagamentoFinal, 'dd/MM/yyyy');

    if (!this.hasRequiredError()) {
      this.parcelaService.findParcela(
        dataVencimentoInicialFormatted, dataVencimentoFinalFormatted,
        dataPagamentoInicialFormatted, dataPagamentoFinalFormatted,
        this.user.investidor, this.instance.cliente, this.parcelasEmAberto).subscribe(
        (resultList: Parcela[]) => {
          if (!resultList || resultList.length === 0) {
            this.info('Nenhum registro foi encontrado');
            this.resultList = null;
          } else {
            this.resultList = resultList;
            this.resultList.forEach( parcela => {
              parcela.operacao = new Operacao();
            });
          }
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        }
      );
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

  public displayAutocompleteCliente(cliente: Cliente): string {
    if (cliente != null) {
      return cliente.id + ' - ' + cliente.pessoa.nome;
    }
    return '';
  }

  public realizarCobranca(parcela: Parcela): void {
    window.open(parcela.cliente.linkWhatsApp, '_blank');
  }

  public openReceber(parcela: Parcela): void {
    this.dataDoPagamento = new Date();
    // this.gerarProximaParcela = false;
    parcela.valorPago = parcela.valor;

    this.operacaoService.findById(parcela.codigoOperacao).subscribe(
      operacao => {
        parcela.operacao = operacao;
        if (operacao.pagamentoSomenteJuros) {
          // this.gerarProximaParcela = true;
          parcela.gerarProximaParcela = true;
        }
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  public receber(parcela: Parcela, modal: string, form: NgForm): void {
    if (this.hasRequiredErrorInForm(form)) {
      this.error('Campos obrigatórios não preenchidos.');
      return;
    }

    parcela.dataDoPagamento = this.utilService.toLocalDate(this.dataDoPagamento);

    this.getService().receberPagamento(parcela).subscribe(
    () => {
      this.hideModal(modal);
      this.find();

      if (parcela.operacao.repasse) {
        if (parcela.operacao.pagamentoMensal || parcela.operacao.pagamentoSomenteJuros) {
          this.success('Pagamento de parcela recebido com sucesso. Favor efetuar o pagamento do repasse mensal.');
        } else {
          this.success('Pagamento de parcela recebido com sucesso. Favor verificar se existe repasse mensal vencendo.');
        }
      } else {
        this.success('Pagamento de parcela recebido com sucesso.');
      }

      this.verificarQuitacaoDeOperacao(parcela);
    }, (error: HttpErrorResponse) => {
      parcela.contaDeDeposito = null;
      parcela.dataDoPagamento = null;
      parcela.pagarSomenteJuros = false;
      this.hideModal(modal);
      this.messageService.handleError(error);
    });
  }

  public verificarQuitacaoDeOperacao(parcela: Parcela): void {
    this.operacaoService.findById(parcela.codigoOperacao).subscribe(
      operacao => {
        if (operacao.dataQuitacao != null && !operacao.pagamentoSomenteJuros) {
          if (operacao.repasse) {
            this.success('Última parcela paga, favor realizar repasse e finalizar a operação.');
          } else {
            this.success('Última parcela paga, favor finalizar a operação.');
          }
        }
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  public prorrogar(parcela: Parcela, modal: string, form: NgForm): void {

    if (this.hasRequiredErrorInForm(form)) {
      this.error('Campos obrigatórios não preenchidos.');
      return;
    }

    const dataProrrogacaoFormatted: string = this.datePipe.transform(this.dataProrrogacao, 'dd/MM/yyyy');

    this.parcelaService.prorrogar(parcela, dataProrrogacaoFormatted).subscribe(
      () => {
          this.hideModal(modal);
          this.find();
          this.success('Parcela prorrogada com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  protected atualizarHistorico(parcela: Parcela): void {
    this.getService().update(parcela).subscribe(
      () => {
        this.find();
        this.success('Histórico atualizado com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  // public onChangePagarSomenteJuros(event, parcela: Parcela): void {
  //   if (event.target.checked) {
  //     parcela.valorPago = parcela.operacao.valor * (parcela.operacao.taxa / 100);
  //   } else {
  //     parcela.valorPago = parcela.valor;
  //   }
  // }

}
