import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AuthService} from "./auth.service";
import {AuthComponent} from "./auth.component";
import {AuthRouting} from "./auth.routing";

@NgModule({
  imports: [
    CommonModule,
    AuthRouting
  ],
  declarations: [AuthComponent],
  providers: [AuthService]
})
export class AuthModule { }
