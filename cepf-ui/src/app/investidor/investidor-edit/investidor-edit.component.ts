import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {Observable} from 'rxjs';

import {BaseEditController} from '../../../angular-framework/base-controller/base-edit-controller';
import {UtilService} from '../../../angular-framework/service/util-service';
import {MessageService} from '../../../angular-framework/service/message-service';
import {BaseError} from '../../../angular-framework/error/base-error';

import {Investidor} from '../investidor';
import {EnumService} from '../../enum-service';
import {PessoaService} from '../../pessoa/pessoa.service';
import {InvestidorService} from '../investidor.service';
import {Pessoa} from '../../pessoa/pessoa';
import {Endereco} from '../../cliente/endereco';
import {ContaCorrente} from '../../conta/conta-corrente';
import {BancoService} from '../../conta/banco.service';
import {Banco} from '../../conta/banco';

@Component({
  selector: 'app-investidor-edit',
  templateUrl: './investidor-edit.component.html',
  styleUrls: ['./investidor-edit.component.css']
})
export class InvestidorEditComponent extends BaseEditController<Investidor> implements OnInit {

  public contaCorrente: ContaCorrente;
  public bancos: Observable<Banco[]>;

  constructor(public enumService: EnumService,
              private utilService: UtilService,
              private pessoaService: PessoaService,
              private bancoService: BancoService,
              messageService: MessageService,
              service: InvestidorService,
              route: ActivatedRoute,
              router: Router) {
    super(Investidor, messageService, service, route, router);
  }

  ngOnInit() {
    super.ngOnInit();
    this.bancos = this.bancoService.findAll();
    this.contaCorrente = new ContaCorrente();
  }

  protected createInstance(): void {
    super.createInstance();
    this.instance.pessoa = this.createPessoa();
    this.instance.contas = [];
  }

  private createPessoa(): Pessoa {
    const pessoa: Pessoa = new Pessoa();
    pessoa.endereco = new Endereco();
    pessoa.endereco.tipoLogradouro = null;
    pessoa.endereco.uf = null;
    return pessoa;
  }

  public onChangeCpf(cpf: string): void {
    if (cpf) {
      cpf = this.utilService.onlyNumber(cpf);
      this.findInvestidor(cpf);
    } else {
      this.instance.pessoa = this.createPessoa();
    }
  }

  public openModal(): void {
    this.contaCorrente = new ContaCorrente();
  }

  public addContaCorrente(): void {
    if (this.contaCorrente == null) {
      this.error('Nenhuma conta foi informada.');
    } else {
      this.instance.contas.push(this.contaCorrente);
    }
  }

  private deleteContaCorrente(contaCorrente: ContaCorrente): void {
    this.utilService.remove(contaCorrente).from(this.instance.contas);
  }

  private findInvestidor(cpf: string) {
    this.service.findOne(new Investidor(cpf)).subscribe(
      (investidor: Investidor) => {
        if (investidor != null) {
          this.error('Já existe um investidor cadastrado para o CPF ' + cpf + '.');
          this.createInstance();
        } else {
          this.findPessoa(cpf);
        }
      }, (error: HttpErrorResponse | BaseError) => {
        this.messageService.handleError(error);
      });
  }

  private findPessoa(cpf: string) {
    this.pessoaService.findOne(new Pessoa(cpf)).subscribe(
      (pessoa: Pessoa) => {
        if (pessoa != null) {
          this.instance.pessoa = pessoa;
        } else {
          this.instance.pessoa = this.createPessoa();
          this.instance.pessoa.cpf = cpf;
        }
      }, (error: HttpErrorResponse | BaseError) => {
        this.messageService.handleError(error);
      });
  }
}
