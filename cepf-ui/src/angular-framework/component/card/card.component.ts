import {Component, Input, OnInit} from '@angular/core';

import {BaseController} from '../../base-controller/base-controller';

@Component({
  selector: 'card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

  //@Input() public borda: boolean;

  @Input() public headerTools: boolean;

  @Input() public baseRoute: string;

  @Input() public label: string;

  @Input() component: BaseController<any>;

  constructor() {
  }

  ngOnInit() {
  }

}
