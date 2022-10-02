import {RouterModule, Routes} from "@angular/router";
import {InfoComponent} from "./info/info.component";
import {PasswordComponent} from "./password/password.component";
import {NgModule} from "@angular/core";

const routes: Routes = [
    {
        path: 'info',
        component: InfoComponent,
        data:{pageTitle:'마이페이지'}
    },
    {
        path:'password',
        component: PasswordComponent,
        data: {pageTitle: '비밀번호 변경'}
    }

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AccountRouting { }
