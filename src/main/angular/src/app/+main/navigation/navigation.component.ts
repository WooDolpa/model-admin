import {Component, EventEmitter, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {MainService} from "../main.service";
import {NgForm} from "@angular/forms";
import {ShareService} from "../../share.service";
import {NotificationService} from "../../shared/utils/notification.service";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css','../../shared/ngx-datatable-css/smartadmin-ngx-datatable.css'],
  encapsulation: ViewEncapsulation.None
})
export class NavigationComponent implements OnInit {

  rows = [];
  selected = [];

  dataStatus : string = "";

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
    naviNo : 0,
    title : '',
    dataStatus : ''
  };

  updBtnStatus : boolean = false;

  constructor(private mainService : MainService,
              private shareService : ShareService,
              private notificationService : NotificationService) { }

  ngOnInit() {
    this.getNavigationList();
  }

  getNavigationList(){
    this.mainService.getNavigationList(this.dataStatus).subscribe(res => {
      this.res = res;
      if(this.res.code === '0'){
        this.rows = this.res.data;
        this.count = this.res.data.length;
      }else{
        this.rows = [];
        this.count = 0;
      }
    });
  }

  searchSubmit(){
    this.getNavigationList();
  }


  onPage(e){}

  onSelect(e){
    this.model.naviNo = e.selected[0].naviNo;
    this.model.title = e.selected[0].title;
    this.model.dataStatus = e.selected[0].dataStatus;
    this.updBtnStatus = true;
  }

  updFormSubmit(form : NgForm){
    this.mainService.updateNavigation(this.model).subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        this.notificationService.smallBox({
          title: "수정 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.updBtnStatus = false;
        this.selected = [];
        this.reset();
        // 네비게이션 다시 적용
        this.shareService.emitChange('Y');
        this.getNavigationList();
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
    this.updBtnStatus = false;
    this.selected = [];
    this.reset();
  }

  reset(){
    this.model.naviNo = 0;
    this.model.title = '';
    this.model.dataStatus = '';
  }
}
