import {Component, Input, OnInit} from '@angular/core';

import {BaseEditController} from 'src/angular-framework/base-controller/base-edit-controller';

import {BaseFields} from '../base-fields';

@Component({
  selector: 'af-edit-fields',
  templateUrl: './edit-fields.component.html',
  styleUrls: ['./edit-fields.component.css']
})
export class EditFieldsComponent extends BaseFields implements OnInit {

  @Input() public baseRoute: string;
  @Input() public component: BaseEditController<any>;

  constructor() {
    super();
  }

  ngOnInit() {
    this.generateTitle();
  }

  protected generateTitle(): void {
    if (!this.fullTitle) {
      if (this.component.isInsertMode()) {
        this.title = 'Inserir ' + this.title;
      } else if (this.component.isUpdateMode()) {
        this.title = 'Atualizar ' + this.title;
      } else if (this.component.isViewMode()) {
        this.title = 'Visualizar ' + this.title;
      }
    }
  }

}

