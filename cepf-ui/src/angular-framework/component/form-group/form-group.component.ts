import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {MatDatepicker} from '@angular/material';

@Component({
  selector: 'form-group',
  templateUrl: './form-group.component.html',
  styleUrls: ['./form-group.component.css']
})
export class FormGroupComponent implements OnInit {

  @Input() public columnClass: string;
  @Input() public inputGroupClass: string;
  @Input() public labelColumnClass: string;
  @Input() public label: string;
  @Input() public input: any;
  @Input() public inputGroup: boolean;
  @Input() public checkbox: boolean;
  @Input() public checkboxInLine: boolean;
  @Input() public radio: boolean;
  @Input() public datepicker: any;

  constructor() {
  }

  ngOnInit() {
  }

}
