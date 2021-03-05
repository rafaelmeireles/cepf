import {Input, OnInit} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import { BaseController } from './base-controller';

import { BaseEntity } from '../model/base-entity';
import {BaseService} from '../service/base-service';
import {MessageService} from '../service/message-service';
import {ActivatedRoute, Router} from '@angular/router';

export abstract class BaseListController<T extends BaseEntity> extends BaseController<T> implements OnInit {

  @Input() public resultList: T[] = null;

  constructor(
    type,
    protected baseRoute: string,
    messageService: MessageService,
    service: BaseService<T>,
    route: ActivatedRoute,
    router: Router,
    protected parentSelector?: string) {
    super(type, messageService, service, route, router, parentSelector);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  public find(): void {
    if (!this.hasRequiredError()) {
      this.service.find(this.instance).subscribe(
        (resultList: T[]) => {
          if (!resultList || resultList.length === 0) {
            this.info('Nenhum registro foi encontrado');
            this.resultList = null;
          } else {
            this.resultList = resultList;
          }
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        }
      );
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

  protected prepareUpdate(id: number): void {
    this.router.navigate([this.baseRoute, id]);
  }

  protected prepareView(id: number): void {
    this.router.navigate([this.baseRoute, id, 'view']);
  }

  protected delete(id: number): void {
    this.service.delete(id).subscribe(
      () => {
        this.find();
        this.success('Registro excluido com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

}
