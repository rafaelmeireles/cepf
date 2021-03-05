import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {BaseListController} from '../../../angular-framework/base-controller/base-list-controller';
import {MessageService} from '../../../angular-framework/service/message-service';

import {Investidor} from '../investidor';
import {InvestidorService} from '../investidor.service';
import {Endereco} from '../../cliente/endereco';
import {Pessoa} from '../../pessoa/pessoa';

@Component({
  selector: 'app-investidor-list',
  templateUrl: './investidor-list.component.html',
  styleUrls: ['./investidor-list.component.css']
})
export class InvestidorListComponent extends BaseListController<Investidor> implements OnInit {

  constructor(messageService: MessageService,
              service: InvestidorService,
              route: ActivatedRoute,
              router: Router) {
    super(Investidor, '/investidores', messageService, service, route, router);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  private createPessoa(): Pessoa {
    let pessoa: Pessoa = new Pessoa();
    pessoa.endereco = new Endereco();
    pessoa.endereco.tipoLogradouro = null;
    pessoa.endereco.uf = null;
    return pessoa;
  }

  protected createInstance(): void {
    super.createInstance();
    this.instance.pessoa = this.createPessoa();
  }

}
