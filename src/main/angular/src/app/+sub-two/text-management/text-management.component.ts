import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {SubTwoService} from "../sub-two.service";
import {NotificationService} from "../../shared/utils/notification.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-text-management',
  templateUrl: './text-management.component.html',
  styleUrls: ['./text-management.component.css','../../shared/ngx-datatable-css/smartadmin-ngx-datatable.css'],
  encapsulation: ViewEncapsulation.None
})
export class TextManagementComponent implements OnInit {

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

  res : any;

  model = {
    no : 0,
    contents : ''
  };

  updBtnStatus:boolean = false;

  constructor(private subTwoService : SubTwoService,
              private notificationService : NotificationService) { }

  ngOnInit() {
    this.getItemText();
  }

  getItemText(){
    this.subTwoService.getItemText().subscribe(res => {

      this.res = res;
      if(this.res.code === '0'){
        this.count = this.res.data.totalCount;
        this.rows = this.res.data.list;
      }else{
        this.count = 0;
        this.rows = [];
      }

    });
  }

  onPage(e){}

  onSelect(e){

    this.model.no = e.selected[0].txtNo;
    this.model.contents = e.selected[0].txtContents;

    this.updBtnStatus = true;
  }

  updFormSubmit(form : NgForm){

    this.subTwoService.updateItemText(this.model).subscribe(res => {

      this.res = res;
      if(this.res.code === '0'){

        form.onReset();
        this.cancelUpdForm();

        this.notificationService.smallBox({
          title: "수정 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.getItemText();

      }else if(this.res.code == -30){

        this.notificationService.smartMessageBox({
          title : "텍스트 수정",
          content : "텍스트 항목을 선택해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -31){

        this.notificationService.smartMessageBox({
          title : "텍스트 수정",
          content : "텍스트를 입력해주세요.",
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

  cancelUpdForm(){
    this.reset();
    this.selected = [];
    this.updBtnStatus = false;
  }

  reset(){
    this.model.no = 0;
    this.model.contents = '';
  }

}
