import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {UtilController} from '../../../angular-framework/base-controller/util-controller';
import {AuthService} from '../../../angular-framework/security/auth-service';
import {User} from '../../../angular-framework/security/user/user';

@Component({
  selector: 'af-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent extends UtilController implements OnInit {

  public user: User = null;

  constructor(private router: Router, private authService: AuthService) {
    super('#header');
  }

  ngOnInit() {
    this.user = this.authService.getUser();
    // console.log('on init HeaderComponent');
  }

  logout() {
      this.authService.logout();
      this.router.navigate(['/login']);
  }

}
