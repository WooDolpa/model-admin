import { Component, OnInit } from '@angular/core';
import {SubTwoService} from "../sub-two.service";
import {NotificationService} from "../../shared/utils/notification.service";

@Component({
  selector: 'app-management',
  templateUrl: './management.component.html',
  styleUrls: ['./management.component.css']
})
export class ManagementComponent implements OnInit {

  res : any;
  rows = [];
  groupRows = [];

  groupId : string  = "";

  regBtnStatus : boolean = false;
  updBtnStatus : boolean = false;

  model = {
    no : 0,
    group : 1,
    title : '',
    description : '',
    img : '',
    rank : 0,
    url : ''
  };

  imgFile : File;
  imgUrl : string = '';

  constructor(private subTwoService : SubTwoService,
              private notificationService : NotificationService) {}

  ngOnInit() {
    this.getItemList();
    this.groupList();
  }

  getItemList(){
    this.subTwoService.getItemList(this.groupId).subscribe(res => {
      this.res = res;
      if(this.res.data.totalCount > 0){
        this.rows = this.res.data.list;
      }else{
        this.rows = [];
      }

    });
  }

  groupList(){

    this.subTwoService.groupList().subscribe(res => {
      this.res = res;
      if(this.res.data.totalCount > 0){
        this.groupRows = this.res.data.list;
      }else{
        this.groupRows = [];
      }
    });
  }

  changeSearchType(){
    this.getItemList();
  }

  fileUpload(e){
    this.imgFile = <File>e.target.files[0];
  }

  openRegForm(){

    if(this.updBtnStatus){
      this.reset();
      this.updBtnStatus = false;
    }

    this.regBtnStatus = true;
  }

  regFormSubmit(){

    this.subTwoService.insertItem(this.model, this.imgFile).subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        this.reset();
        this.regBtnStatus = false;

        this.notificationService.smallBox({
          title: "등록 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.getItemList();

      }else if(this.res.code == -22){

        this.notificationService.smartMessageBox({
          title : "오류",
          content : "제목을 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -23){

        this.notificationService.smartMessageBox({
          title : "오류",
          content : "설명 정보가 없습니다.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -25){

        this.notificationService.smartMessageBox({
          title : "오류",
          content : "이미지 정보가 없습니다.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else {

        this.notificationService.smartMessageBox({
          title : "알수없는 오류",
          content : "등록하는 과정에서 오류가 발생하였습니다.\n관리자에게 문의해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }

    });

  }

  cancelRegForm(){
    this.reset();
    this.regBtnStatus = false;
  }

  openUpdateForm(row){

    if(this.regBtnStatus){
      this.reset();
      this.regBtnStatus = false;
    }

    this.model.no = row.id;
    this.model.group = row.groupId;
    this.model.title = row.title;
    this.model.description = row.description;
    this.model.url = row.url;
    this.imgUrl = row.img;
    this.model.rank = row.rank;

    this.updBtnStatus = true;
  }

  updateFormSubmit(){

    this.subTwoService.updateItem(this.model, this.imgFile).subscribe(res => {
      this.res = res;

      if(this.res.code === '0'){

        this.reset();
        this.updBtnStatus = false;

        this.notificationService.smallBox({
          title: "수정 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.getItemList();

      }else if(this.res.code == -9){
        this.notificationService.smartMessageBox({
          title : "순번 변경 오류",
          content : "해당 순번 변경이 불가능합니다.\n다시 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -22){

        this.notificationService.smartMessageBox({
          title : "오류",
          content : "제목을 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -23){

        this.notificationService.smartMessageBox({
          title : "오류",
          content : "설명 정보가 없습니다.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -25){

        this.notificationService.smartMessageBox({
          title : "오류",
          content : "이미지 정보가 없습니다.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else {

        this.notificationService.smartMessageBox({
          title : "알수없는 오류",
          content : "수정하는 과정에서 오류가 발생하였습니다.\n관리자에게 문의해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }

    });
  }

  cancelUpdateForm(){
    this.reset();
    this.updBtnStatus = false;
  }

  delete(){

    this.notificationService.smartMessageBox({
      title : "삭제",
      content : "삭제하시겠습니까?",
      buttons : '[취소][확인]'
    }, (ButtonPressed) => {
      if(ButtonPressed === '확인'){

        this.subTwoService.deleteItem(this.model.no).subscribe(res => {

          this.res = res;

          if(this.res.code === '0'){

            this.reset();
            this.updBtnStatus = false;

            this.notificationService.smallBox({
              title: "삭제 되었습니다.",
              content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
              color: "#659265",
              iconSmall: "fa fa-check fa-2x fadeInRight animated",
              timeout: 4000
            });

            this.getItemList();

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

      }else{}
    });

  }

  reset(){
    this.model.no = 0;
    this.model.group = 1;
    this.model.title = '';
    this.model.description = '';
    this.model.img = '';
    this.model.rank = 0;
    this.model.url = '';

    this.imgFile = null;
    this.imgUrl = '';
  }
}
