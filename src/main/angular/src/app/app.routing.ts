/**
 * Created by griga on 7/11/16.
 */


import {Routes, RouterModule} from '@angular/router';
import {MainLayoutComponent} from "./shared/layout/app-layouts/main-layout.component";
import {AuthLayoutComponent} from "./shared/layout/app-layouts/auth-layout.component";
import {ModuleWithProviders} from "@angular/core";
import {AuthGuard} from "./+auth/auth-guard.service";

export const routes: Routes = [
    {
        path: '',
        component: MainLayoutComponent,
        canActivate: [AuthGuard],
        children: [
            {
                path: 'main',
                loadChildren: 'app/+main/main.module#MainModule',
                data: {pageTitle: "메인"}
            },
            {
                path: 'sub1',
                loadChildren: 'app/+sub-one/sub-one.module#SubOneModule',
                data: {pageTitle: ''}
            },
            {
                path: 'sub2',
                loadChildren: 'app/+sub-two/sub-two.module#SubTwoModule',
                data: {pageTitle: ''}
            },
            {
                path: 'account',
                loadChildren: 'app/+account/account.module#AccountModule',
                data: {pageTitle: '회원 관리'}
            }
        ]
    },
    {
        path: 'auth',
        component: AuthLayoutComponent,
        loadChildren: 'app/+auth/auth.module#AuthModule'
    }

];

export const routing: ModuleWithProviders = RouterModule.forRoot(routes, {useHash : true});
