import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MainService} from "../main.service";
import {NotificationService} from "../../shared/utils/notification.service";

@Component({
  selector: 'app-contents',
  templateUrl: './contents.component.html',
  styleUrls: ['../../shared/ngx-datatable-css/smartadmin-ngx-datatable.css','./contents.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ContentsComponent implements OnInit {

  res : any;
  rows = [];
  selected = [];

  loadingIndicator: boolean = true;
  reorderable: boolean = true;

  count: number = 0;
  offset: number = 0;

  controls: any = {
    pageSize:  10,
    filter: '',
  };

  model = {
    no : 0,
    txt : ''
  };

  config = {
    allowedContent : false,
    extraPlugins : 'divarea',
    forcePasteAsPlainText : true
  };

  regBtnStatus : boolean = false;
  updBtnStatus : boolean = false;

  constructor(private mainService : MainService,
              private notificationService : NotificationService) { }

  ngOnInit() {
    this.findContent();
  }

  findContent(){

    this.mainService.findContent().subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){
        this.count = this.res.data.totalCount;
        this.rows = this.res.data.data;
      }else{
        this.rows = [];
        this.count = 0;
      }

    });
  }

  onPage(e){}

  openRegForm(){
    this.regBtnStatus = true;
  }

  onSelect(e){

    this.model.no = e.selected[0].ctNo;
    this.model.txt = e.selected[0].ctTxt;
    this.updBtnStatus = true;

  }

  cancelRegForm(){
    this.reset();
    this.regBtnStatus = false;

  }

  updateFormSubmit(){

    this.mainService.updateContent(this.model).subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        this.reset();
        this.cancelUpdateForm();

        this.notificationService.smallBox({
          title: "수정 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.findContent();

      }else if(this.res.code == -28){

        this.notificationService.smartMessageBox({
          title : "내용 수정",
          content : "항목을 선택해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -29){

        this.notificationService.smartMessageBox({
          title : "내용 수정",
          content : "내용을 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else{

        this.notificationService.smartMessageBox({
          title : "알수없는 오류",
          content : "삭제하는 과정에서 오류가 발생하였습니다.\n관리자에게 문의해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }

    });

  }

  cancelUpdateForm(){
    this.reset();
    this.selected = [];
    this.updBtnStatus = false;
  }


  reset(){
    this.model.no = 0;
    this.model.txt = '';
  }

}
