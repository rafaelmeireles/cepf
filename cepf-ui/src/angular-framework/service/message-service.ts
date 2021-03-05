import {Injectable} from '@angular/core';

import {ToastrService} from 'ngx-toastr';

import {BaseError} from '../error/base-error';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class MessageService {

  protected constructor(private toastr: ToastrService) {
  }

  public success(message: string): void {
    this.toastr.success(message);
  }

  public warning(message: string): void {
    this.toastr.warning(message);
  }

  public error(message: string): void {
    this.toastr.error(message);
  }

  public info(message: string): void {
    this.toastr.info(message);
  }

  public handleError(error: HttpErrorResponse | BaseError): void {
    // console.log('error.name: ' + error.name);
    // console.log('error.status: ' + error.status);
    // console.log('error.message: ' + error.message);
    // console.log('error.statusText: ' + error.statusText);
    // console.log('error.error: ' + error.error);
    // console.log('error.error.timestamp: ' + error.error.timestamp);
    // console.log('error.error.constructor.name: ' + error.error.constructor.name);
    // console.log('error.error.status: ' + error.error.status);
    // console.log('error.error.message: ' + error.error.message);
    // console.log('error.error.path: ' + error.error.path);
    // console.log('error.error.error: ' + error.error.error);

    const baseError: BaseError = error instanceof HttpErrorResponse ? error.error : error;

    if (baseError.status === undefined) {
      this.error('Serviço indisponível. Tente novamente em instantes.');
    } else {
      if (baseError.status !== 401) {

        if (baseError.status === 404 && baseError.message === 'No message available') {
          this.error('O Serviço requisitado não existe.');
          return;
        }

        if (baseError.message != null) {
          this.error(baseError.message);
        } else {
          this.error('Ocorreu um erro inesperado em nosso servidor, favor tentar novamente.');
        }

      }
    }
  }
}
