import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {MainRoutingModule} from "./main.routing";
import {MainService} from "./main.service";
import {CompanyComponent} from "./company/company.component";
import {SmartadminModule} from "../shared/smartadmin.module";
import {NgxDatatableModule} from "@swimlane/ngx-datatable";
import {NavigationComponent} from './navigation/navigation.component';
import {SilderComponent} from './silder/silder.component';
import {ContentsComponent} from './contents/contents.component';
import {SanitizeHtmlPipe} from "../sanitizeHtmlPipe";
import {CKEditorModule} from "ng2-ckeditor";

@NgModule({
    imports: [
        CommonModule,
        SmartadminModule,
        NgxDatatableModule,
        CKEditorModule,
        MainRoutingModule
    ],
    declarations: [CompanyComponent, NavigationComponent, SilderComponent, ContentsComponent, SanitizeHtmlPipe],
    exports: [SanitizeHtmlPipe],
    providers: [MainService]
})
export class MainModule { }
