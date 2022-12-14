import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {DatatableComponent} from "@swimlane/ngx-datatable";
import {SubOneService} from "../../+sub-one/sub-one.service";
import {NotificationService} from "../../shared/utils/notification.service";
import {SubTwoService} from "../sub-two.service";

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['../../shared/ngx-datatable-css/smartadmin-ngx-datatable.css','./group.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class GroupComponent implements OnInit {

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

  keyword : string = "";
  type : string = "";
  searchResult : boolean = true;
  updBtnStatus : boolean = false;

  model = {
    id : 0,
    name : "",
    rank : 0,
    dataStatus : "1"
  };

  @ViewChild(DatatableComponent) table: DatatableComponent;
  constructor(private subTwoService : SubTwoService,
              private notificationService : NotificationService) { }

  ngOnInit() {
    this.getTypeList();
  }

  getTypeList () {

    this.subTwoService.getTypeList(this.type, this.keyword, this.offset, this.controls.pageSize).subscribe(res => {

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
        this.rows =[];
        this.count = 0;
        this.searchResult = false;
      }

    });

  }

  changeSearchType() {
    this.getTypeList();
  }

  searchSubmit() {
    this.getTypeList();
  }

  onPage(e){
    this.offset = e.offset;
    this.getTypeList();
  }

  onSelect(e){

    this.resetModel();
    this.model.id = e.selected[0].id;
    this.model.name = e.selected[0].name;
    this.model.rank = e.selected[0].rank;
    this.model.dataStatus = e.selected[0].dataStatus;
    this.updBtnStatus = true;

  }

  updFormSubmit(){

    this.subTwoService.updType(this.model).subscribe(res => {

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
        this.getTypeList();
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

  cancelUpdForm(){
    this.resetModel();
    this.selected = [];
    this.updBtnStatus = false;
  }

  resetModel(){
    this.model.id = 0;
    this.model.name = "";
    this.model.rank = 0;
    this.model.dataStatus = "1";
  }

}
