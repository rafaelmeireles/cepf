import {Component, OnInit} from '@angular/core';
import {BaseEditController} from '../../../angular-framework/base-controller/base-edit-controller';
import {ContaCorrente} from '../conta-corrente';
import {EnumService} from '../../enum-service';

@Component({
  selector: 'app-conta-edit',
  templateUrl: './conta-edit.component.html'
})
export class ContaEditComponent extends BaseEditController<ContaCorrente> implements OnInit {

  constructor(public enumService: EnumService) {
    super(ContaCorrente);
  }

  ngOnInit() {

    super.ngOnInit();
  }

}
