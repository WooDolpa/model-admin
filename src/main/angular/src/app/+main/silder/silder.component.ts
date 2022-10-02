import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {MainService} from "../main.service";
import {NgForm} from "@angular/forms";
import {NotificationService} from "../../shared/utils/notification.service";
import {BsModalRef, BsModalService} from "ngx-bootstrap";

@Component({
  selector: 'app-silder',
  templateUrl: './silder.component.html',
  styleUrls: ['../../shared/ngx-datatable-css/smartadmin-ngx-datatable.css','./silder.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class SilderComponent implements OnInit {

  isActive : string = "";
  keyword : string = "";

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

  regBtnStatus : boolean = false;
  updBtnStatus : boolean = false;

  model = {
    no : 0,
    title : '',
    content : '',
    sort : 0,
    img : '',
    dataStatus : ''
  };

  imgUrl : string = "";
  imgFile : File;

  modalRef : BsModalRef;

  @ViewChild('imgModal') imgModal;
  constructor(private mainService : MainService,
              private notificationService : NotificationService,
              private modalService : BsModalService) { }

  ngOnInit() {
    this.getSliderList();
  }

  getSliderList(){
    this.mainService.getSliderList(this.isActive, this.keyword, this.offset, this.controls.pageSize).subscribe(res => {
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

  searchTypeChange(){
    this.getSliderList();
  }

  searchSubmit(){
    this.getSliderList();
  }

  onPage(e){
    this.offset = e.offset;
    this.getSliderList();
  }

  openRegForm(){
    if(this.updBtnStatus){
      this.reset();
      this.updBtnStatus = false;
    }
    this.regBtnStatus = true;
  }

  fileUpload(e){
    this.imgFile = <File>e.target.files[0];
  }

  regFormSubmit(ngForm : NgForm){

    this.mainService.insertSlider(this.model, this.imgFile).subscribe(res => {
      this.res = res;
      if(this.res.code === '0'){

        this.reset();
        this.regBtnStatus = false;

        ngForm.onReset();

        this.notificationService.smallBox({
          title: "등록 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.getSliderList();

      }else if(this.res.code == -18){

        this.notificationService.smartMessageBox({
          title : "등록",
          content : "활성화 여부를 선택해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else{

        this.notificationService.smartMessageBox({
          title : "알수없는 오류",
          content : "등록하는 과정에서 오류가 발생하였습니다.\n관리자에게 문의해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }

    })

  }

  cancelRegForm(){
    this.regBtnStatus = false;
    this.reset();
  }

  onSelect(e){

    if(this.regBtnStatus){
      this.reset();
      this.regBtnStatus = false;
    }

    this.model.no = e.selected[0].sliderNo;
    this.model.title = e.selected[0].title;
    this.model.content = e.selected[0].content;
    this.model.sort = e.selected[0].sort;
    this.model.dataStatus = e.selected[0].dataStatus;
    this.imgUrl = e.selected[0].imgUrl;

    this.updBtnStatus = true;
  }

  updateFormSubmit(ngForm : NgForm){

    this.mainService.updateSlider(this.model, this.imgFile).subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        this.reset();
        this.selected = [];
        this.updBtnStatus = false;

        ngForm.onReset();

        this.notificationService.smallBox({
          title: "수정 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.getSliderList();

      }else if(this.res.code == -9){

        this.notificationService.smartMessageBox({
          title : "올바르지 않은 순번입니다.",
          content : "문자 또는 공백이 아닌 숫자로 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else if(this.res.code == -21){

        this.notificationService.smartMessageBox({
          title : "허용되지 않은 순번",
          content : "불가능한 순번입니다.\n다시 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }else{

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

  deleteSlider(){

    if(this.selected.length == 0){

      this.notificationService.smartMessageBox({
        title : "삭제 실패",
        content : "삭제하려는 항목을 선택해주세요.",
        buttons : '[확인]'
      }, (ButtonPressed) => {
        if(ButtonPressed === '확인'){}
      });

    }

    this.notificationService.smartMessageBox({
      title : "삭제",
      content : "해당 항목을 삭제하시겠습니까?",
      buttons : '[취소][삭제]'
    }, (ButtonPressed) => {
      if(ButtonPressed === '삭제'){

        this.mainService.deleteSlider(this.model).subscribe(res => {

          this.res = res;

          if(this.res.code === '0'){

            this.notificationService.smallBox({
              title: "수정 되었습니다.",
              content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
              color: "#659265",
              iconSmall: "fa fa-check fa-2x fadeInRight animated",
              timeout: 4000
            });

            this.reset();
            this.selected = [];
            this.updBtnStatus = false;
            this.getSliderList();

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
    });

  }

  cancelUpdForm(){
    this.reset();
    this.selected = [];
    this.updBtnStatus = false;
  }

  openImgModal(imgUrl){

    this.imgUrl = imgUrl;
    this.modalRef = this.modalService.show(this.imgModal);

  }

  reset(){
    this.model.title = '';
    this.model.content = '';
    this.model.sort = 0;
    this.model.img = '';
    this.model.dataStatus = '';
    this.model.no = 0;

    this.imgFile = null;
  }
}
