import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';

import {BaseService} from '../../angular-framework/service/base-service';
import {MessageService} from '../../angular-framework/service/message-service';

import {Parcela} from './parcela';
import {Cliente} from '../cliente/cliente';
import {Operacao} from '../operacao/operacao';
import {Investidor} from '../investidor/investidor';

@Injectable({
  providedIn: 'root'
})
export class ParcelaService extends BaseService<Parcela> {

  constructor(private messageService: MessageService,
              httpClient: HttpClient) {
    super('/parcelas', httpClient);
  }

  public findParcela(dataDeVencimentoInicial: string,
                     dataDeVencimentoFinal: string,
                     dataDoPagamentoInicial?: string,
                     dataDoPagamentoFinal?: string,
                     investidor?: Investidor,
                     cliente?: Cliente,
                     parcelasEmAberto?: boolean): Observable<Parcela[]> {

    let url = this.url;

    if (parcelasEmAberto) {
      url += '/em-aberto';
    }

    if (dataDeVencimentoInicial || dataDeVencimentoFinal || dataDoPagamentoInicial || dataDoPagamentoFinal || cliente) {
      url += '?';
    }

    url += dataDeVencimentoInicial != null ? 'dataDeVencimentoInicial=' + dataDeVencimentoInicial + '&' : '';
    url += dataDeVencimentoFinal != null ? 'dataDeVencimentoFinal=' + dataDeVencimentoFinal + '&' : '';

    if (!parcelasEmAberto) {
      url += dataDoPagamentoInicial != null ? 'dataDoPagamentoInicial=' + dataDoPagamentoInicial + '&' : '';
      url += dataDoPagamentoFinal != null ? 'dataDoPagamentoFinal=' + dataDoPagamentoFinal + '&' : '';
    }

    url += investidor != null ? 'investidor=' + encodeURI(JSON.stringify(investidor)) + '&' : '';
    url += cliente != null ? 'cliente=' + encodeURI(JSON.stringify(cliente)) + '&' : '';

    return this.httpClient.get<Parcela[]>(url);
  }

  public receberPagamento(parcela: Parcela): Observable<Parcela> {
    return this.httpClient.put<Parcela>(this.url + '/' + parcela.id + '/pagamento', parcela);
  }

  prorrogar(parcela: Parcela, dataProrrogacao: string): Observable<Parcela> {
    const url = this.url + '/' + parcela.id + '/prorrogar?dataProrrogacao=' + dataProrrogacao;
    return this.httpClient.put<Parcela>(url, parcela);
  }

}
