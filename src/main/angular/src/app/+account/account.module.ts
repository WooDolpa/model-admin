import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InfoComponent } from './info/info.component';
import { PasswordComponent } from './password/password.component';
import {AccountService} from "./account.service";
import {AccountRouting} from "./account.routing";
import {SmartadminModule} from "../shared/smartadmin.module";
import {SmartadminValidationModule} from "../shared/forms/validation/smartadmin-validation.module";

@NgModule({
  imports: [
    CommonModule,
    SmartadminModule,
    SmartadminValidationModule,
    AccountRouting
  ],
  declarations: [InfoComponent, PasswordComponent],
  providers: [AccountService]
})
export class AccountModule{ }
