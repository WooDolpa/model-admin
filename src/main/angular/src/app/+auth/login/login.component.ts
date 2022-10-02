import { Component, OnInit } from '@angular/core';
import {AuthService} from "../auth.service";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {NotificationService} from "../../shared/utils/notification.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userId : string;
  password : string;

  loginErrorMsg : boolean = false;
  serverErrorMsg : boolean = false;

  res : any;

  constructor(private authService : AuthService,
              private cookieService: CookieService,
              private notificationService : NotificationService,
              private router: Router) { }

  ngOnInit() {
    this.userId = "";
    this.password = "";
  }

  login() {

    this.authService.login(this.userId, this.password).subscribe(res => {

      this.res = res;

      if(this.res.code === "0"){
        // 성공
        let expireTime = 60/(24*60);
        this.authService.isLoggedIn = true;
        this.cookieService.set('token', this.res.data.token, expireTime);
        sessionStorage.setItem('name',this.res.data.name);

        this.router.navigate(['/main/company']);

      }else if(this.res.code == -6){

        this.notificationService.smartMessageBox({
          title : "로그인",
          content : "존재하지 않은 회원입니다.\n다시 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

        this.userId = "";
        this.password = "";

      }else if(this.res.code == -7){

        this.notificationService.smartMessageBox({
          title : "로그인",
          content : "비밀번호가 일치하지 않습니다.\n다시 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

        this.password = "";

      }else if(this.res.code == -12) {

        this.notificationService.smartMessageBox({
          title : "로그인",
          content : "아이디를 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

        return;

      }else if(this.res.code == -13){

        this.notificationService.smartMessageBox({
          title : "로그인",
          content : "비밀번호를 입력해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

        return;

      }else{

        this.notificationService.smartMessageBox({
          title : "로그인",
          content : "시스템 오류가 발생하였습니다.\n관리자에게 문의해주세요.",
          buttons : '[확인]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '확인'){}
        });

        this.userId = "";
        this.password = "";

      }

    });
  }

  tempPassword(){

    this.notificationService.smartMessageBox({
      title: '임시 비밀번호',
      content: '등록된 이메일로 전송됩니다.',
      buttons: '[취소][전송]'
    }, (ButtonPressed) => {

      if(ButtonPressed === '전송'){

        this.authService.tempPassword().subscribe(res => {

          this.res = res;
          if(this.res.code === '0'){
            this.notificationService.smallBox({
              title: "전송 되었습니다.",
              content: "<i class='fa fa-clock-o'></i> <i>4초뒤 자동으로 사라집니다..</i>",
              color: "#659265",
              iconSmall: "fa fa-check fa-2x fadeInRight animated",
              timeout: 4000
            });
          }else{
            this.notificationService.smartMessageBox({
              title : "임시 비밀번호 발송",
              content : "시스템 오류가 발생하였습니다.\n관리자에게 문의해주세요.",
              buttons : '[확인]'
            }, (ButtonPressed) => {
              if(ButtonPressed === '확인'){}
            });
          }

        });
      }

    });

  }

}
