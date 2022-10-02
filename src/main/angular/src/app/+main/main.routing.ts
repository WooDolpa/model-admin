import {RouterModule, Routes} from "@angular/router";
import {CompanyComponent} from "./company/company.component";
import {NgModule} from "@angular/core";
import {NavigationComponent} from "./navigation/navigation.component";
import {SilderComponent} from "./silder/silder.component";
import {ContentsComponent} from "./contents/contents.component";

const routes: Routes = [
    {
        path : 'company',
        component : CompanyComponent,
        data: {pageTitle: '회사정보'}
    },
    {
        path: 'navigation',
        component: NavigationComponent,
        data: {pageTitle: '네비게이션'}
    },
    {
        path: 'slider',
        component: SilderComponent,
        data: {pageTitle: '슬라이드'}
    },
    {
        path: 'contents',
        component: ContentsComponent,
        data: {pageTitle: '내용'}
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class MainRoutingModule { }
