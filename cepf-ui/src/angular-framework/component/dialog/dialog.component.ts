import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {AbstractControl, NgForm} from '@angular/forms';

@Component({
  selector: 'af-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent implements OnInit {

  @Input() public id: string;
  @Input() public title: string;
  @Input() public message: string;
  @Input() public confirmDialog: boolean = true;
  @Input() public large: boolean = false;
  @Input() public labelOperationButton: string;

  @Output() protected onOpen: EventEmitter<any> = new EventEmitter<any>();
  @Output() public onClose: EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  ngOnInit() {
  }

  public prepareOpen(): void {
    this.onOpen.emit();
  }

  public close(dialog: any): void {
    this.onClose.emit(dialog);
  }
}
