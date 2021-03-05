import {NgModule} from '@angular/core';
import {ExtraOptions, PreloadAllModules, RouterModule, Routes} from '@angular/router';

import {LoginComponent} from '../angular-framework/security/login/login.component';

import {TemplateComponent} from './layout-af/template/template.component';
import {NotfoundComponent} from '../angular-framework/component/notfound/notfound.component';

import {CONTENT_ROUTES} from './shared.routes';

const appRoutes: Routes = [

    { path : 'login', component: LoginComponent },
    { path: '', component: TemplateComponent, data: { title: 'full Views' }, children: CONTENT_ROUTES },
    // { path: '', pathMatch: 'full', redirectTo: 'cepf'},
    // { path: 'cepf', component: TemplateComponent, data: { title: 'full Views' }, children: CONTENT_ROUTES },
    { path : '**', component: NotfoundComponent }

];

const routerOptions: ExtraOptions = {
  useHash: false,
  scrollPositionRestoration: 'enabled',
  anchorScrolling: 'enabled',
  scrollOffset: [0, 64],
  preloadingStrategy: PreloadAllModules
};

@NgModule({
  // imports: [RouterModule.forRoot(appRoutes, { preloadingStrategy: PreloadAllModules})],
  imports: [RouterModule.forRoot(appRoutes, {
    useHash: false,
    // scrollPositionRestoration: 'enabled',
    anchorScrolling: 'enabled',
    // scrollOffset: [0, 64],
    // enableTracing: true,
    preloadingStrategy: PreloadAllModules
  })],
  exports: [RouterModule]
})

export class AppRoutingModule {
}
