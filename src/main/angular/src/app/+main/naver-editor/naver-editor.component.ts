import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-naver-editor',
  templateUrl: './naver-editor.component.html',
  styleUrls: ['./naver-editor.component.css']
})
export class NaverEditorComponent implements OnInit {

  @Input() config : any;
  oEditors : any;

  @ViewChild("test", {read : ElementRef}) test : ElementRef;

  constructor() { }

  ngOnInit() {

    if(!this.config){
     this.config = {
      id: "ir1"
     };
    }

    if (window['nhn']){
      this.initEditor();
    }else{
      this.loadScript();
    }

  }

  loadScript() {
    const url = '/assets/smarteditor2/js/service/HuskyEZCreator.js';
    const script = document.createElement('script');
    script.onload = () => {
      this.initEditor();
    };
    script.type = 'text/javascript';
    script.src = url;
    document.body.appendChild(script);
  }

  initEditor() {
    this.oEditors = [];
    window['nhn'].husky.EZCreator.createInIFrame({
      oAppRef: this.oEditors,
      elPlaceHolder: this.config.id,
      sSkinURI: "/assets/smarteditor2/SmartEditor2Skin.html",
      htParams : {
        bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
        bUseVerticalResizer : true,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
        bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
        //bSkipXssFilter : true,		// client-side xss filter 무시 여부 (true:사용하지 않음 / 그외:사용)
        //aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
        fOnBeforeUnload : function(){
          //alert("완료!");
        },
        I18N_LOCALE : "ko_KR"
      }, //boolean
      fOnAppLoad : function(){
        //예제 코드
        //oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
      },
      fCreator: "createSEditor2"
    });
  }

  change(){

    this.oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD",[]);

    console.log(this.test.nativeElement);

  }

}
