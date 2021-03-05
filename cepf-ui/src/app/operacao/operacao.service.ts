import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {DatePipe} from '@angular/common';

import {Observable} from 'rxjs';

import {BaseService} from '../../angular-framework/service/base-service';
import {MessageService} from '../../angular-framework/service/message-service';

import {Operacao} from './operacao';
import {Parcela} from '../parcela/parcela';
import {TipoDeOperacao} from './tipo-operacao';
import {TipoDePagamento} from './tipo-pagamento';
import {UtilService} from '../../angular-framework/service/util-service';
import {tap} from 'rxjs/operators';
import {RepasseMensal} from './repasse-mesal';

@Injectable({
  providedIn: 'root'
})
export class OperacaoService extends BaseService<Operacao> {

  constructor(private messageService: MessageService,
              private datePipe: DatePipe,
              private utilService: UtilService,
              httpClient: HttpClient) {
    super('/operacoes', httpClient);
  }

  persist(operacao: Operacao): Observable<Operacao> {
    operacao.data = this.datePipe.transform(operacao.data, 'dd/MM/yyyy');
    return super.persist(operacao).pipe(
      tap(entity => entity.data = this.utilService.toDate(entity.data.toString()))
    );
  }

  public gerarParcelas(operacao: Operacao): void {
    operacao.parcelas = [];
    operacao.data = this.datePipe.transform(operacao.data, 'dd/MM/yyyy');
    this.httpClient.get<Parcela[]>(this.url + '/gerar-parcelas?entity=' + encodeURI(JSON.stringify(operacao))).subscribe(
      (parcelas: Parcela[]) => {
        operacao.parcelas = parcelas;
        operacao.data = this.utilService.toDate(operacao.data.toString());
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
        operacao.data = this.utilService.toDate(operacao.data.toString());
      }
    );
  }

  public findOperacao(entity: Operacao,
                      from: Date | string,
                      to: Date | string,
                      tipoOperacao: TipoDeOperacao[],
                      tipoPagamento: TipoDePagamento[]): Observable<Operacao[]> {

    from = this.datePipe.transform(from, 'dd/MM/yyyy');
    to = this.datePipe.transform(to, 'dd/MM/yyyy');
    entity.data = this.datePipe.transform(entity.data, 'dd/MM/yyyy');

    let url = this.url;
    url += '?entity=' + encodeURI(JSON.stringify(entity));
    url += from != null ? '&from=' + from  : '';
    url += to != null ? '&to=' + to  : '';
    url += tipoOperacao != null ? '&tipoOperacao=' + tipoOperacao  : '';
    url += tipoPagamento != null ? '&tipoPagamento=' + tipoPagamento  : '';
    return this.httpClient.get<Operacao[]>(url);
  }

  public autorizar(operacao: Operacao): Observable<Operacao> {
    return this.httpClient.put<Operacao>(this.url + '/' + operacao.id + '/autorizar', operacao);
  }

  public finalizar(operacao: Operacao): Observable<Operacao> {
    return this.httpClient.put<Operacao>(this.url + '/' + operacao.id + '/finalizar', operacao);
  }

  public pagarRepasseMensal(repasse: RepasseMensal): Observable<void> {
    const url = this.url + '/' + repasse.codigoOperacao + '/repasse-mensal/' + repasse.id + '/pagar';
    return this.httpClient.put<void>(url, repasse);
  }

  public receberSomenteJuros(operacao: Operacao): Observable<Operacao> {
    return this.httpClient.put<Operacao>(this.url + '/' + operacao.id + '/pagar-somente-juros', operacao);
  }

  public cancelar(operacao: Operacao): Observable<Operacao> {
    return this.httpClient.put<Operacao>(this.url + '/' + operacao.id + '/cancelar', operacao);
  }
}
