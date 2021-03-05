import { Component, OnInit } from '@angular/core';
import { UtilController } from 'src/angular-framework/base-controller/util-controller';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent extends UtilController implements OnInit {

  constructor() {
    super();
  }

  ngOnInit() {
  }

}
