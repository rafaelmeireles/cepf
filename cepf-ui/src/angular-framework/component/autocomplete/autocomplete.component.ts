import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {FormControl} from '@angular/forms';

import {Observable, Subscription} from 'rxjs';
import {map, startWith} from 'rxjs/operators';

import {BaseEntity} from '../../model/base-entity';
import {MessageService} from '../../service/message-service';

@Component({
  selector: 'af-autocomplete',
  templateUrl: './autocomplete.component.html',
  styleUrls: ['./autocomplete.component.css']
})
export class AutocompleteComponent implements OnInit, OnDestroy {

  @Input() public id: string;
  @Input() public label: string;
  @Input() public fieldToDisplay: any;
  @Input() public columnClass: string;
  @Input() public displayWith: any;
  @Input() public readonly: boolean = false;
  @Input() public observableDataSource: Observable<BaseEntity[]>;
  @Input() public change: Observable<BaseEntity>;
  private changeSubscription: Subscription;

  @Output() public onSelect: EventEmitter<BaseEntity> = new EventEmitter<BaseEntity>();

  public control: FormControl = new FormControl();
  private dataSource: BaseEntity[] = [];
  public filteredDataSource: Observable<BaseEntity[]>;

  constructor(private messageService: MessageService) { }

  ngOnInit() {
    this.observableDataSource.subscribe(
      (resultList: BaseEntity[]) => {
        this.dataSource = resultList;
      }, (error: HttpErrorResponse) => {
        this.messageService.handleError(error);
      }
    );

    this.filteredDataSource = this.control.valueChanges
      .pipe(
        startWith(''),
        map(value => typeof value === 'string' ? value : value.id + ' - ' + value[this.fieldToDisplay]),
        map( name => this.filterInCollection(name, this.fieldToDisplay, this.dataSource))
      );

    if (this.change) {
      this.changeSubscription = this.change.subscribe(entity => this.control.setValue(entity));
    }
  }

  ngOnDestroy(): void {
    if (this.changeSubscription) {
      this.changeSubscription.unsubscribe();
    }
  }

  public filterInCollection(textToFind: string, field: string, collection: BaseEntity[]): BaseEntity[] {
    textToFind = textToFind.toLowerCase();
    return collection.filter(entity => {

      let fullName: string = entity.id + ' - ';

      if (field.split('.').length > 1) {// para casos ex: pessoa.nome
        const fieldArray: string[] = field.split('.');
        const entityAux = entity[fieldArray[0]];
        fullName += entityAux[fieldArray[1]].toLowerCase();
      } else {
        fullName += entity[field].toLowerCase();
      }

      return fullName.includes(textToFind);
    });
  }

  public select(entity: BaseEntity): void {
    this.onSelect.emit(entity);
  }

  public verifyNull(value: string): void {
    if (!value) {
      this.onSelect.emit(null);
    }
  }

}
