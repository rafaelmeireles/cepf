import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'af-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.css']
})
export class DataTableComponent implements OnInit {

  @Input() public dataSource: any[];
  @Input() public id: string;
  @Input() public padding: boolean = true;
  @Input() public withInsert: boolean;
  @Input() public withInsertAndModal: boolean;
  @Output() public onOpen: EventEmitter<any> = new EventEmitter<any>();
  @Output() public onInsert: EventEmitter<any> = new EventEmitter<any>();

  constructor() {}

  ngOnInit() {
  }

  protected prepareOpen(): void {
    this.onOpen.emit();
  }

  protected insert(): void {
    this.onInsert.emit();
  }

}
