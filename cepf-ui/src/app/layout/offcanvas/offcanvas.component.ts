import {Component, OnInit} from '@angular/core';

import {UtilController} from '../../../angular-framework/base-controller/util-controller';

// @Component({
//   selector: 'app-offcanvas',
//   templateUrl: './offcanvas.component.html',
//   styleUrls: ['./offcanvas.component.css']
// })
export class OffcanvasComponent extends UtilController implements OnInit {

  constructor() {
    super('#offcanvas-right');
  }

  ngOnInit() {
    super.ngOnInit();
  }

}
