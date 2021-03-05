import {Component, OnInit} from '@angular/core';

import {UtilController} from 'src/angular-framework/base-controller/util-controller';
import {AuthService} from '../../../angular-framework/security/auth-service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent extends UtilController implements OnInit {

  constructor(private authService: AuthService) {
    super('#menubar');
  }

  ngOnInit() {
    // console.log('on init MenuComponent');
  }

  public hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }

}
