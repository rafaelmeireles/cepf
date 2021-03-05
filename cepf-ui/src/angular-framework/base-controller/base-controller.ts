import {Input, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AbstractControl, NgForm} from '@angular/forms';

import {BaseService} from '../service/base-service';
import {BaseEntity} from '../model/base-entity';
import {MessageService} from '../service/message-service';
import {UtilController} from './util-controller';

export abstract class BaseController<T extends BaseEntity> extends UtilController implements OnInit {

  @Input() @ViewChild('form', {static: false}) protected form: NgForm;

  @Input() public instance: T = null;

  constructor(
    private type,
    protected messageService?: MessageService,
    protected service?: BaseService<T>,
    protected route?: ActivatedRoute,
    protected router?: Router,
    // public dialog?: MatDialog,
    protected parentSelector?: string) {
    super(parentSelector);
  }

  ngOnInit() {
    super.ngOnInit();
    this.createInstance();
  }

  protected getService(): BaseService<T> {
    return this.service;
  }

  protected createInstance(): void {
    this.instance = new this.type();
  }

  public clearInstance(): void {
    this.form.reset();
    this.createInstance();
    // código para a validação retornar ao estado inicial
    // Object.keys(this.form.controls).forEach(
    //   controlName => {
    //     const control: AbstractControl = this.form.form.get(controlName);
    //     control.reset();
    //     control.markAsUntouched();
    //     control.markAsPristine();
    //   });
  }

  protected success(message: string): void {
    this.messageService.success(message);
  }

  public warning(message: string): void {
    this.messageService.warning(message);
  }

  public error(message: string): void {
    this.messageService.error(message);
  }

  public info(message: string): void {
    this.messageService.info(message);
  }

  protected hasRequiredError(): boolean {
    return this.validateRequiredControls();
  }

  private validateRequiredControls(): boolean {
    let requiredError: boolean = false;
    Object.keys(this.form.controls).forEach(
      controlName => {
        const control: AbstractControl = this.form.form.get(controlName);
        if (control.invalid && control.hasError('required')) {
          control.markAsTouched();
          requiredError = true;
        }
      });
    return requiredError;
  }

  protected hasRequiredErrorInForm(form: NgForm): boolean {
    return this.validateRequiredControlsInForm(form);
  }

  private validateRequiredControlsInForm(form: NgForm): boolean {
    let requiredError: boolean = false;
    Object.keys(form.controls).forEach(
      controlName => {
        const control: AbstractControl = form.form.get(controlName);
        if (control.invalid && control.hasError('required')) {
          control.markAsTouched();
          requiredError = true;
        }
      });
    return requiredError;
  }

  public compareWith(e1: BaseEntity, e2: BaseEntity): boolean {
    return e1 && e2 ? e1.id === e2.id : e1 === e2;
  }

  protected showModal(modal: string): void {
    // $('#'+modal).show();
    (<any>$('#' + modal)).modal('show');
  }

  protected hideModal(modal: string): void {
    // $('#' + modal).hide();
    (<any>$('#' + modal)).modal('hide');
  }

  // public filterInCollection(textToFind: string, field: string, collection: BaseEntity[]): BaseEntity[] {
  //   textToFind = textToFind.toLowerCase();
  //   return collection.filter(entity => {
  //     const fullName: string = entity.id + ' - ' + entity[field].toLowerCase();
  //     return fullName.includes(textToFind);
  //   });
  // }
}


