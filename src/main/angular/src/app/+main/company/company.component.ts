import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {MainService} from "../main.service";
import {NgForm} from "@angular/forms";
import {NotificationService} from "../../shared/utils/notification.service";
import {BsModalRef, BsModalService} from "ngx-bootstrap";

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.css', '../../shared/ngx-datatable-css/smartadmin-ngx-datatable.css'],
  encapsulation: ViewEncapsulation.None
})
export class CompanyComponent implements OnInit {

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
    cpNo : 0,
    cpName : '',
    img : ''
  };

  updBtnStatus:boolean = false;

  imgUrl : string = "";
  imgFile : File;

  modalRef : BsModalRef;

  @ViewChild('imgModal') imgModal;
  constructor(private mainService : MainService,
              private notificationService : NotificationService,
              private modalService : BsModalService) { }

  ngOnInit() {
    this.getCompany();
  }

  getCompany(){
    this.mainService.getCompany().subscribe(res => {
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

  onPage(e){}

  onSelect(e){
    this.model.cpNo = e.selected[0].cpNo;
    this.model.cpName = e.selected[0].cpName;
    this.updBtnStatus = true;
  }

  updFormSubmit(form : NgForm){

    this.mainService.updateCompany(this.model, this.imgFile).subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        this.notificationService.smallBox({
          title: "수정 되었습니다.",
          content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
          color: "#659265",
          iconSmall: "fa fa-check fa-2x fadeInRight animated",
          timeout: 4000
        });

        this.getCompany();

      }else{

        this.notificationService.smartMessageBox({
          title : "알수없는 오류",
          content : "삭제하는 과정에서 오류가 발생하였습니다.\n관리자에게 문의해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

      }

      form.resetForm();
      this.cancelUpdForm();

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

  fileUpload(e){
    this.imgFile = <File>e.target.files[0];
  }

  reset(){
    this.model.cpNo = 0;
    this.model.cpName = '';
    this.model.img = '';

    this.imgFile = null;
  }
}
