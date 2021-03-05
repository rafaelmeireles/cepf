import {Routes} from '@angular/router';

import {AuthGuard} from '../angular-framework/security/auth.guard';
import {RoleListComponent} from '../angular-framework/security/role/role-list/role-list.component';
import {RoleEditComponent} from '../angular-framework/security/role/role-edit/role-edit.component';
import {UserResetPasswordComponent} from '../angular-framework/security/user/reset-password/user-reset-password.component';
import {RoleEnum} from '../angular-framework/security/role/role.enum';

import {UserEditComponent} from './security/user/user-edit/user-edit.component';
import {UserListComponent} from './security/user/user-list/user-list.component';

import {HomeComponent} from './layout/home/home.component';

import {ClienteListComponent} from './cliente/cliente-list/cliente-list.component';
import {ClienteEditComponent} from './cliente/cliente-edit/cliente-edit.component';
import {InvestidorListComponent} from './investidor/investidor-list/investidor-list.component';
import {InvestidorEditComponent} from './investidor/investidor-edit/investidor-edit.component';

import {OperacaoListComponent} from './operacao/operacao-list/operacao-list.component';
import {OperacaoEditComponent} from './operacao/operacao-edit/operacao-edit.component';
import {AporteEditComponent} from './aporte/aporte-edit/aporte-edit.component';
import {ExtratoComponent} from './conta/extrato/extrato.component';
import {ParcelaListComponent} from './parcela/parcela-list/parcela-list.component';
import {ParcelaEditComponent} from './parcela/parcela-edit/parcela-edit.component';
import {ResgateEditComponent} from './resgate/resgate-edit/resgate-edit.component';
import {ApuracaoResultadoEditComponent} from './apuracao-resultado/apuracao-resultado-edit/apuracao-resultado-edit.component';
import {LancamentoComponent} from './conta/lancamento/lancamento.component';
import {OperacaoCancelarComponent} from './operacao/operacao-cancelar/operacao-cancelar.component';
import {OperacaoAutorizarComponent} from './operacao/operacao-autorizar/operacao-autorizar.component';

export const CONTENT_ROUTES: Routes = [
    // security
    { path : 'users', component: UserListComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]}},
    { path : 'users/insert', component: UserEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'users/reset-password', component: UserResetPasswordComponent, canActivate: [AuthGuard] },
    { path : 'users/:id', component: UserEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'users/:id/view', component: UserEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'roles', component: RoleListComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'roles/insert', component: RoleEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'roles/:id', component: RoleEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'roles/:id/view', component: RoleEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },

    // cliente
    { path : 'clientes', component: ClienteListComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}},
    { path : 'clientes/insert', component: ClienteEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },
    { path : 'clientes/:id', component: ClienteEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },
    { path : 'clientes/:id/view', component: ClienteEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },

    // investidor
    { path : 'investidores', component: InvestidorListComponent,
      canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'investidores/insert', component: InvestidorEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'investidores/aporte', component: AporteEditComponent,
      canActivate: [AuthGuard], pathMatch: 'full' , data: {roles: [RoleEnum.INVESTIDOR]} },
    { path : 'investidores/resgate', component: ResgateEditComponent,
      canActivate: [AuthGuard], pathMatch: 'full' , data: {roles: [RoleEnum.INVESTIDOR]} },
    { path : 'investidores/apuracao-resultado', component: ApuracaoResultadoEditComponent,
      canActivate: [AuthGuard], pathMatch: 'full' , data: {roles: [RoleEnum.INVESTIDOR]} },
    { path : 'investidores/:id', component: InvestidorEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.ADMINISTRADOR]} },
    { path : 'investidores/:id/view', component: InvestidorEditComponent, canActivate: [AuthGuard],
      data: {roles: [RoleEnum.ADMINISTRADOR]} },

    // operacao
    { path : 'operacoes', component: OperacaoListComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}, pathMatch: 'full' },
    // { path : 'insert', component: OperacaoEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}, outlet: 'routerOutletView' },
    { path : 'operacoes/insert', component: OperacaoEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}, pathMatch: 'full' },
    { path : 'operacoes/autorizar', component: OperacaoAutorizarComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}, pathMatch: 'full' },
    { path : 'operacoes/cancelar', component: OperacaoCancelarComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}, pathMatch: 'full' },
    { path : 'operacoes/:id', component: OperacaoEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}, pathMatch: 'full' },
    { path : 'operacoes/:id/view', component: OperacaoEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]}, pathMatch: 'full' },

  // parcela
  { path : 'parcelas', component: ParcelaListComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },
  // { path : 'parcelas/insert', component: OperacaoEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },
  // { path : 'parcelas/:id', component: OperacaoEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },
  { path : 'parcelas/:id/view', component: ParcelaEditComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },

  // contabil
  { path : 'contabil/extrato', component: ExtratoComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },
  { path : 'contabil/lancamento', component: LancamentoComponent, canActivate: [AuthGuard], data: {roles: [RoleEnum.INVESTIDOR]} },

  { path : '', component: HomeComponent, canActivate: [AuthGuard], pathMatch: 'full'}
];
