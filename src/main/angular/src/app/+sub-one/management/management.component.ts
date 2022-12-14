import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {SubOneService} from "../sub-one.service";
import {DatatableComponent} from "@swimlane/ngx-datatable";
import {NgForm} from "@angular/forms";
import {NotificationService} from "../../shared/utils/notification.service";

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['../../shared/ngx-datatable-css/smartadmin-ngx-datatable.css','./management.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ManagementComponent implements OnInit {

  res : any;

  groupRows = [];

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

  type : string = "";
  group : string = "";
  keyword : string = "";

  searchResult : boolean = true;
  regBtnStatus : boolean = false;
  updBtnStatus : boolean = false;

  model = {
    awardNo : 0,
    group : 1,
    awardName : "",
    url : '',
    rank : "",
    dataStatus : "1"
  };

  @ViewChild(DatatableComponent) table: DatatableComponent;
  constructor(private subOneService : SubOneService,
              private notificationService : NotificationService) { }

  ngOnInit() {
    this.getItemList();
    this.groupList();
  }

  getItemList(){

    this.subOneService.getItemList(this.group, this.type, this.keyword, this.offset, this.controls.pageSize).subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        if(this.res.data.totalCount > 0){
          this.searchResult = true;
        }else{
          this.searchResult = false;
        }
        this.count = this.res.data.totalCount;
        this.rows = this.res.data.list;

      }else{
        this.rows = [];
        this.count = 0;
        this.searchResult = false;
      }
    });
  }

  groupList(){

    this.subOneService.getGroupList().subscribe(res => {

      this.res = res;
      if(this.res.code === '0'){
        this.groupRows = this.res.data.list;
      }else{
        this.groupRows = [];
      }

    });

  }

  searchSubmit(){
    this.getItemList();
  }

  changeSearchType(){
    this.getItemList();
  }

  changeSearchGroup() {
    this.getItemList();
  }

  updatePageSize(value){

    if(!this.controls.filter){
      this.controls.pageSize = parseInt(value);
      this.offset = 0;
      this.getItemList();
    }

    this.table.limit = this.controls.pageSize;
    window.dispatchEvent(new Event('resize'));
  }

  onPage(e){
    this.offset = e.offset;
    this.getItemList();
  }

  onSelect(e){

    if(this.regBtnStatus){
      this.resetModel();
      this.regBtnStatus = false;
    }

    this.model.awardNo = e.selected[0].awardNo;
    this.model.group = e.selected[0].awardType;
    this.model.rank = e.selected[0].rank;
    this.model.awardName = e.selected[0].awardName;
    this.model.url = e.selected[0].url;
    this.model.dataStatus = e.selected[0].dataStatus;

    this.updBtnStatus = true;

  }

  regBtnEvent(){

    if(this.updBtnStatus){
      this.selected = [];
      this.resetModel();
      this.updBtnStatus = false;
    }
    this.regBtnStatus = true;
  }

  regFormSubmit(ngForm : NgForm){

    this.subOneService.regItem(this.model).subscribe(res => {

      this.res = res;

      if (this.res.code === '0'){
        this.notificationService.smallBox({
          title: "?????? ???????????????.",
          content: "<i class='fa fa-clock-o'></i> <i>4?????? ???????????? ???????????????..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });
        this.resetModel();
        this.regBtnStatus = false;
        this.getItemList();
      }

    });

  }

  updFormSubmit(){

    this.subOneService.updItem(this.model).subscribe(res => {

      this.res = res;

      if(this.res.code == -9){
        this.notificationService.smartMessageBox({
          title : "?????? ?????? ??????",
          content : "?????? ?????? ????????? ??????????????????.\n?????? ??????????????????.",
          buttons : '[??????]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '??????'){}
        });
        return;
      }else if (this.res.code === '0'){
        this.notificationService.smallBox({
          title: "?????? ???????????????.",
          content: "<i class='fa fa-clock-o'></i> <i>4?????? ???????????? ???????????????..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });
        this.resetModel();
        this.updBtnStatus = false;
        this.getItemList();
      }else{

        this.notificationService.smartMessageBox({
          title : "???????????? ??????",
          content : "???????????? ???????????? ????????? ?????????????????????.\n??????????????? ??????????????????.",
          buttons : '[??????]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '??????'){}
        });

      }

    });

  }

  cancelRegForm(){
    this.resetModel();
    this.regBtnStatus = false;
  }

  cancelUpdForm(){
    this.resetModel();
    this.selected = [];
    this.updBtnStatus = false;
  }

  deleteItem(){

    if(this.selected.length == 0){
      this.notificationService.smartMessageBox({
        title : "?????? ??????",
        content : "????????? ????????? ??????????????????.",
        buttons : '[??????]'
      }, (ButtonPressed) => {
        if(ButtonPressed === '??????'){}
      });
      return;
    }

    this.notificationService.smartMessageBox({
      title: '??????',
      content: '????????? ????????? ?????????????????????????',
      buttons: '[??????][??????]'
    }, (ButtonPressed) => {

      if(ButtonPressed === '??????'){

        this.subOneService.deleteItem(this.model.awardNo).subscribe(res => {

          this.res = res;
          if(this.res.code === '0'){

            this.resetModel();
            this.selected = [];
            this.regBtnStatus = false;
            this.updBtnStatus = false;

            this.getItemList();

          }

        });

        this.notificationService.smallBox({
          title: "?????? ???????????????.",
          content: "<i class='fa fa-clock-o'></i> <i>4?????? ???????????? ???????????????..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

      }

    });
  }

  resetModel(){
    this.model.awardNo = 0;
    this.model.group = 1;
    this.model.awardName = "";
    this.model.rank = "";
    this.model.dataStatus = "1";
  }
}
