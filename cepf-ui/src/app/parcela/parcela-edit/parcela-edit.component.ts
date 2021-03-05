import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';

import {BaseEditController} from '../../../angular-framework/base-controller/base-edit-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';

import {Parcela} from '../parcela';
import {ParcelaService} from '../parcela.service';
import {Operacao} from '../../operacao/operacao';
import {ContaCorrente} from '../../conta/conta-corrente';
import {OperacaoService} from '../../operacao/operacao.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-operacao-edit',
  templateUrl: './parcela-edit.component.html'
})
export class ParcelaEditComponent extends BaseEditController<Parcela> implements OnInit {

  public operacao: Operacao;

  constructor(private operacaoService: OperacaoService,
              private authService: AuthService,
              messageService: MessageService,
              service: ParcelaService,
              route: ActivatedRoute,
              router: Router) {
    super(Parcela, messageService, service, route, router);
  }

  protected completeUpdateOrView(): void {
    super.completeUpdateOrView();
    this.operacaoService.findById(this.instance.codigoOperacao).subscribe(
      (operacao: Operacao) => {
        this.operacao = operacao;
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      }
    );
  }
}
