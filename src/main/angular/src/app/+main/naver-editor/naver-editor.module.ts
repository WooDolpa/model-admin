import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {NaverEditorComponent} from "./naver-editor.component";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [NaverEditorComponent],
  exports: [NaverEditorComponent]
})

export class NaverEditorModule { }
