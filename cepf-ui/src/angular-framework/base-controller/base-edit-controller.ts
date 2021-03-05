import {AfterViewInit, OnInit} from '@angular/core';
import {ParamMap} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import * as $ from 'jquery';

import {BaseController} from './base-controller';
import {BaseEntity} from '../model/base-entity';

export abstract class BaseEditController<T extends BaseEntity> extends BaseController<T> implements OnInit, AfterViewInit {

  ngOnInit() {
    super.ngOnInit();
    this.route.paramMap.subscribe(params => this.prepareUpdateOrView(params));
    this.prepareView();
  }

  ngAfterViewInit() {
    super.ngAfterViewInit();
    this.prepareViewAfterViewInit();
  }

  persist(): void {
    if (!this.hasRequiredError()) {
      this.service.persist(this.instance).subscribe(
        (entity: T) => {
          this.instance = entity;
          this.success('Registro inserido com sucesso.');
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        });
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }
  }

  update(): void {
    if (!this.hasRequiredError()) {
      this.service.update(this.instance).subscribe(
        (entity: T) => {
          this.instance = entity;
          this.success('Registro atualizado com sucesso.');
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        });
    } else {
      this.error('Campos obrigatórios não preenchidos.');
    }

  }

  delete(id: number): void {
    this.service.delete(id).subscribe(
      (entity: T) => {
        // this.instance = entity;
        this.success('Registro excluido com sucesso.');
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      });
  }

  protected prepareUpdateOrView(params: ParamMap): void {
    if (params.get('id') != null) {
      this.instance.id = +params.get('id');
      this.service.findById(this.instance.id).subscribe(
        (entity: T) => {
          this.instance = entity;
          this.completeUpdateOrView();
        }, (error: HttpErrorResponse) => {
          this.messageService.handleError(error);
        }
      );
    }
  }

  public isInsertMode(): boolean {
    return this.router.url.endsWith('/insert');
  }

  public isUpdateMode(): boolean {
    return !this.isInsertMode() && !this.isViewMode() && this.instance.id !== undefined;
  }

  public isViewMode(): boolean {
    return this.router.url.endsWith('/view');
  }

  /** Prepare the page to be loaded when the state of view was selected.*/
  protected prepareView(): void {}

  /** Prepare the page to be loaded when the state of view was selected. This preparation differs of prepareView
   *  because in this method you put code that need to manipulate the view after it have been initialized */
  private prepareViewAfterViewInit() {
    if (this.isViewMode()) {
      $('.form-group :input').each(function () {
        $(this).attr('disabled', 'true');
      });
      $('.form-group :checkbox').each(function () {
        $(this).attr('disabled', 'true');
      });
      $('.btn.btn-raised').each(function () {
        $(this).attr('disabled', 'true');
      });
    }
  }

  protected completeUpdateOrView(): void {}
}
