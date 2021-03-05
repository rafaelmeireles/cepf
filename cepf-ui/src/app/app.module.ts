import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {LOCALE_ID, NgModule} from '@angular/core';
import locale from '@angular/common/locales/pt';
import {registerLocaleData} from '@angular/common';

import {SidebarModule} from 'ng-sidebar';

import {NgxPipeFunctionModule} from 'ngx-pipe-function';

import {BaseModule} from '../angular-framework/base.module';
import {NotfoundComponent} from '../angular-framework/component/notfound/notfound.component';

import {HeaderComponent} from './layout-af/header/header.component';
import {TemplateComponent} from './layout-af/template/template.component';
import {FooterComponent} from './layout-af/footer/footer.component';

import {MenuComponent} from './layout/menu/menu.component';
import {AppComponent} from './app.component';
import {HomeComponent} from './layout/home/home.component';
import {AppRoutingModule} from './app.routes';

import {ClienteListComponent} from './cliente/cliente-list/cliente-list.component';
import {ClienteEditComponent} from './cliente/cliente-edit/cliente-edit.component';
import {InvestidorListComponent} from './investidor/investidor-list/investidor-list.component';
import {InvestidorEditComponent} from './investidor/investidor-edit/investidor-edit.component';
import {UserListComponent} from './security/user/user-list/user-list.component';
import {UserEditComponent} from './security/user/user-edit/user-edit.component';
import {PessoaEditComponent} from './pessoa/pessoa-edit/pessoa-edit.component';
import {ContaEditComponent} from './conta/conta-edit/conta-edit.component';
import {OperacaoListComponent} from './operacao/operacao-list/operacao-list.component';
import {OperacaoListFiltersComponent} from './operacao/operacao-list/operacao-list-filters.component';
import {OperacaoListDataTableComponent} from './operacao/operacao-list/operacao-list-data-table.component';
import {OperacaoEditComponent} from './operacao/operacao-edit/operacao-edit.component';
import {AporteEditComponent} from './aporte/aporte-edit/aporte-edit.component';
import {ExtratoComponent} from './conta/extrato/extrato.component';
import {ParcelaListComponent} from './parcela/parcela-list/parcela-list.component';
import {ResgateEditComponent} from './resgate/resgate-edit/resgate-edit.component';
import {ApuracaoResultadoEditComponent} from './apuracao-resultado/apuracao-resultado-edit/apuracao-resultado-edit.component';
import {ParcelaEditComponent} from './parcela/parcela-edit/parcela-edit.component';
import {LancamentoComponent} from './conta/lancamento/lancamento.component';
import {OperacaoAutorizarComponent} from './operacao/operacao-autorizar/operacao-autorizar.component';

registerLocaleData(locale);

@NgModule({
  declarations: [
    AppComponent,
    TemplateComponent,
    HeaderComponent,
    MenuComponent,
    HomeComponent,
    FooterComponent,
    NotfoundComponent,
    UserListComponent,
    UserEditComponent,
    ClienteListComponent,
    ClienteEditComponent,
    InvestidorListComponent,
    InvestidorEditComponent,
    PessoaEditComponent,
    ContaEditComponent,
    OperacaoListComponent,
    OperacaoListFiltersComponent,
    OperacaoListDataTableComponent,
    OperacaoEditComponent,
    AporteEditComponent,
    ResgateEditComponent,
    ExtratoComponent,
    ParcelaListComponent,
    ParcelaEditComponent,
    ApuracaoResultadoEditComponent,
    LancamentoComponent,
    // OperacaoCancelarComponent,
    OperacaoAutorizarComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BaseModule,
    SidebarModule.forRoot(),
    NgxPipeFunctionModule
  ],
  providers: [
    {provide: LOCALE_ID, useValue: 'pt'}
  ],
  exports: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
