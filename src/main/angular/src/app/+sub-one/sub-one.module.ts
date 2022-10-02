import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ManagementComponent} from "./management/management.component";
import {SubOneService} from "./sub-one.service";
import {SubOneRoutingModule} from "./sub-one-routing.module";
import {SmartadminModule} from "../shared/smartadmin.module";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {AlertModule} from "ngx-bootstrap";
import { GroupComponent } from './group/group.component';

@NgModule({
  imports: [
    CommonModule,
    SmartadminModule,
    NgxDatatableModule,
    AlertModule,
    SubOneRoutingModule
  ],
  declarations: [ManagementComponent, GroupComponent],
  providers: [SubOneService]
})
export class SubOneModule { }
