import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {BaseController} from '../../../angular-framework/base-controller/base-controller';
import {MessageService} from '../../../angular-framework/service/message-service';
import {AuthService} from '../../../angular-framework/security/auth-service';

import {InvestidorService} from '../../investidor/investidor.service';
import {UserApplication} from '../../security/user-application';
import {ApuracaoResultado} from '../apuracao-resultado';
import {RoleEnum} from '../../../angular-framework/security/role/role.enum';

@Component({
  selector: 'app-apuracao-resultado-edit',
  templateUrl: './apuracao-resultado-edit.component.html'
})
export class ApuracaoResultadoEditComponent extends BaseController<ApuracaoResultado> implements OnInit {

  private user: UserApplication;

  constructor(private investidorService: InvestidorService,
              private authService: AuthService,
              messageService: MessageService,
              route: ActivatedRoute,
              router: Router) {
    super(ApuracaoResultado, messageService, null, route, router);
    this.user = this.authService.getUser() as UserApplication;
  }

  public hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }

  persist(): void {

    this.instance.investidor = this.user.investidor;

    if (this.hasRole(RoleEnum.ADMINISTRADOR) && this.user.investidor == null) {
      this.error('Operação permitida somente para investidor.');
      return;
    }

    if (!this.hasRequiredError()) {
      this.investidorService.apurarResultado(this.instance).subscribe(
        (status: number) => {
          this.success('Apuração de resultados efetuada com sucesso.');
          this.createInstance();
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        });
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }
}
