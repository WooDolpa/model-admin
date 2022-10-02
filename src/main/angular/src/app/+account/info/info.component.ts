import { Component, OnInit } from '@angular/core';
import {AccountService} from "../account.service";
import {NotificationService} from "../../shared/utils/notification.service";

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

  res : any;

  model = {
    adminNo : 0,
    id : '',
    name : '',
    email : ''
  };

  validatorOptions = {
    feedbackIcons: {
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      name: {
        validators: {
          notEmpty: {
            message: '관리자명을 입력해주세요.'
          }
        }
      },
      email: {
        validators: {
          notEmpty: {
            message: '이메일을 입력해주세요.'
          },
          regexp: {
            regexp: '^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$',
            message: '이메일 형식이 아닙니다.'
          }
        }
      }
    }
  };

  constructor(private accountService : AccountService,
              private notificationService : NotificationService) { }

  ngOnInit() {
    this.getAccountInfo();
  }

  getAccountInfo(){
    this.accountService.findAccount().subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){
        this.model.adminNo = this.res.data.adminNo;
        this.model.id = this.res.data.id;
        this.model.name = this.res.data.name;
        this.model.email = this.res.data.email;
      }

    });
  }

  changeUserInfo(){

    this.notificationService.smartMessageBox({
      title: '회원정보 변경',
      content: '회원정보를 변경하시겠습니까?',
      buttons: '[취소][변경]'
    }, (ButtonPressed) => {

      if(ButtonPressed === '변경'){

        this.accountService.changeUserInfo(this.model).subscribe(res => {

          this.res = res;

          if(this.res.code === '0'){

            this.notificationService.smallBox({
              title: "변경 되었습니다.",
              content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
              color: "#659265",
              iconSmall: "fa fa-check fa-2x fadeInRight animated",
              timeout: 4000
            });



          }else if(this.res.code == -14){
            alert("이름을 입력해주세요.");
            return;
          }else if(this.res.code == -15){
            alert("이메일 정보를 입력해주세여요.");
            return;
          }else{
            alert("시스템 오류가 발생하였습니다.\n관리자에게 문의해주세요.");
            return;
          }

        });
      }

    });

  }

}
