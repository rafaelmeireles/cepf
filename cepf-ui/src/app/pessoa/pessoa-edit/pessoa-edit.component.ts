import {Component, OnInit} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {BaseController} from '../../../angular-framework/base-controller/base-controller';
import {User} from '../../../angular-framework/security/user/user';
import {BaseError} from '../../../angular-framework/error/base-error';
import {UtilService} from '../../../angular-framework/service/util-service';

import {Pessoa} from '../pessoa';
import {EnumService} from '../../enum-service';

import {PessoaService} from '../pessoa.service';

@Component({
  selector: 'app-pessoa-edit',
  templateUrl: './pessoa-edit.component.html'
})
export class PessoaEditComponent extends BaseController<Pessoa> implements OnInit {

  constructor(private pessoaService: PessoaService,
              public enumService: EnumService,
              private utilService: UtilService) {
    super(User);
  }

  ngOnInit() {
  }

  public onChangeCpf(cpf: string): void {
    this.hasRequiredError();
    if (cpf) {
      cpf = this.utilService.onlyNumber(cpf);
      this.pessoaService.findOne(new Pessoa(cpf)).subscribe(
        (pessoa: Pessoa) => {
          if (pessoa != null) {
            this.instance = pessoa;
          }
        }, (error: HttpErrorResponse | BaseError) => {
          this.messageService.handleError(error);
        });
    }
  }

}
