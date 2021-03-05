import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {HTTP_INTERCEPTORS} from '@angular/common/http';

import {MatAutocompleteModule, MatDatepickerModule, MatInputModule, MatNativeDateModule} from '@angular/material';

import {ToastrModule} from 'ngx-toastr';
import {NgxMaskModule} from 'ngx-mask';
import {NgxCurrencyModule} from 'ngx-currency';
import {LoadingBarHttpClientModule} from '@ngx-loading-bar/http-client';
import {LoadingBarRouterModule} from '@ngx-loading-bar/router';
import {JwtModule} from '@auth0/angular-jwt';

import {DataTableComponent} from './component/data-table/data-table.component';
import {ListFieldsComponent} from './component/fields/list-fields/list-fields.component';
import {EditFieldsComponent} from './component/fields/edit-fields/edit-fields.component';
import {FormGroupComponent} from './component/form-group/form-group.component';
import {CardComponent} from './component/card/card.component';
import {DialogComponent} from './component/dialog/dialog.component';

import {JwtInterceptor} from './security/jwt.interceptor';
import {LoginComponent} from './security/login/login.component';
import {RoleListComponent} from './security/role/role-list/role-list.component';
import {RoleEditComponent} from './security/role/role-edit/role-edit.component';
import {UserResetPasswordComponent} from './security/user/reset-password/user-reset-password.component';
import {AutocompleteComponent} from './component/autocomplete/autocomplete.component';

export function getToken() {
  return localStorage.getItem('token');
}

@NgModule({
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    MatAutocompleteModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ToastrModule.forRoot({progressBar: true}),
    NgxMaskModule.forRoot(),
    LoadingBarHttpClientModule,
    LoadingBarRouterModule,
    JwtModule.forRoot({
      config: {
        // tokenGetter: () => localStorage.getItem('token'),
        tokenGetter: getToken,
        whitelistedDomains: ['localhost:8080'],
        blacklistedRoutes: ['localhost:8080/api/auth/signin']
      }
    })
  ],
  declarations: [
    DataTableComponent,
    ListFieldsComponent,
    EditFieldsComponent,
    FormGroupComponent,
    CardComponent,
    LoginComponent,
    DialogComponent,
    RoleListComponent,
    RoleEditComponent,
    UserResetPasswordComponent,
    AutocompleteComponent
  ],
  exports: [
    CommonModule,
    ToastrModule,
    NgxMaskModule,
    NgxCurrencyModule,
    LoadingBarHttpClientModule,
    LoadingBarRouterModule,
    DataTableComponent,
    ListFieldsComponent,
    EditFieldsComponent,
    FormGroupComponent,
    CardComponent,
    LoginComponent,
    DialogComponent,
    UserResetPasswordComponent,
    AutocompleteComponent,
    MatAutocompleteModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  providers: [
    DatePipe,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true}
  ]
})
export class BaseModule {}
