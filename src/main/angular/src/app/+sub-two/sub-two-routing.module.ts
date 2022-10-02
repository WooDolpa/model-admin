import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {ManagementComponent} from "./management/management.component";
import {TextManagementComponent} from "./text-management/text-management.component";
import {GroupComponent} from "./group/group.component";


const routes: Routes = [
  {
    path: 'management',
    component: ManagementComponent,
    data:{pageTitle:''}
  },
  {
    path: 'group',
    component: GroupComponent,
    data:{pageTitle: ''}
  },
  {
    path: 'text',
    component: TextManagementComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SubTwoRoutingModule { }
