import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManagementComponent } from './management/management.component';
import {SubTwoRoutingModule} from "./sub-two-routing.module";
import {SubTwoService} from "./sub-two.service";
import {SmartadminModule} from "../shared/smartadmin.module";
import { TextManagementComponent } from './text-management/text-management.component';
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import { GroupComponent } from './group/group.component';
import {AlertModule} from "ngx-bootstrap";

@NgModule({
  imports: [
    CommonModule,
    SmartadminModule,
    NgxDatatableModule,
    AlertModule,
    SubTwoRoutingModule
  ],
  declarations: [ManagementComponent, TextManagementComponent, GroupComponent],
  providers: [SubTwoService]
})
export class SubTwoModule { }
