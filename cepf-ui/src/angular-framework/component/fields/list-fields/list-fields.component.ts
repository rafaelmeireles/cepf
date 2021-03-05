import { Component, OnInit, Input } from '@angular/core';

import { BaseListController } from '../../../base-controller/base-list-controller';
import { BaseFields } from '../base-fields';

@Component({
  selector: 'af-list-fields',
  templateUrl: './list-fields.component.html',
  styleUrls: ['./list-fields.component.css']
})
export class ListFieldsComponent extends BaseFields implements OnInit {

  @Input() public component: BaseListController<any>;
  @Input() public insert: boolean = true;

  constructor() {
    super();
  }

  ngOnInit() {
    this.generateTitle();
  }

  protected generateTitle(): void {
    if (!this.fullTitle) {
      this.title = 'Pesquisar ' + this.title;
    }
  }

}
