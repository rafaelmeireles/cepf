import {Injectable} from '@angular/core';

import {ToastrService} from 'ngx-toastr';

import {BaseError} from '../error/base-error';
import {BaseEntity} from '../model/base-entity';
import {DateTimeFormatter, LocalDate} from 'js-joda';

@Injectable({providedIn: 'root'})
export class UtilService {

  private itemToRemove: any;

  constructor() {
  }

  public remove(item: any): UtilService {
    this.itemToRemove = item;
    return this;
  }

  public from(itens: any[]): void {
    itens.forEach(
      (item, index) => {
        if (item === this.itemToRemove) {
          itens.splice(index, 1);
          this.itemToRemove = null;
        }
      });
  }

  public onlyNumber(text: string): string {
    return text.replace(/\D+/g, '');
  }

  public toDate(date: string): Date {
    const data: LocalDate = LocalDate.parse(date.toString(), DateTimeFormatter.ofPattern('dd/MM/yyyy'));
    return new Date(data.year(), data.monthValue() - 1, data.dayOfMonth());
  }

  public toLocalDate(date: Date): LocalDate {
    return LocalDate.of(date.getFullYear(), date.getMonth() + 1, date.getDate());
  }
}
