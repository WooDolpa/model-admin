import { Component, OnInit } from '@angular/core';
import {AccountService} from "../account.service";
import {NotificationService} from "../../shared/utils/notification.service";
import {AuthService} from "../../+auth/auth.service";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.css']
})
export class PasswordComponent implements OnInit {

  validatorOptions = {
    feedbackIcons: {
      invalid: 'glyphicon glyphicon-remove',
      validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
      password: {
        validators: {
          notEmpty: {
            message: '현재 비밀번호를 입력해주세요'
          },
          stringLength: {
            min: 4,
            max: 18,
            message: '4~18자리의 문자를 입력해주세요'
          }
        }
      },
      newPassword: {
        validators: {
          notEmpty: {
            message: '새로운 비밀번호를 입력해주세요'
          },
          stringLength: {
            min: 4,
            max: 18,
            message: '4~18자리의 문자를 입력해주세요'
          },
          different: {
            message: '현재 비밀번호와 다른 비밀번호로 입력해주세요',
            field: 'password'
          }
        }
      },
      newPasswordConfirm: {
        validators: {
          notEmpty: {
            message: '새로운 비밀번호를 한번 더 입력해주세요'
          },
          identical: {
            field: 'newPassword',
            message: '새로운 비밀번호와 같지 않습니다'
          }
        }
      }
    }
  };

  res : any;

  model = {
    adminNo : 0,
    id : ''
  };

  pwdModel = {
    password : '',
    newPassword : '',
    newPasswordConfirm: ''
  };

  constructor(private accountService : AccountService,
              private notificationService : NotificationService,
              private authService : AuthService,
              private cookieService : CookieService,
              private router : Router) { }

  ngOnInit() {
    this.getAccountInfo();
  }

  getAccountInfo(){

    this.accountService.findAccount().subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        this.model.adminNo = this.res.data.adminNo;
        this.model.id = this.res.data.id;

      }else if(this.res.code == -7){
        alert("현재 비밀번호가 일치하지 않습니다.\n다시 입력해주세요.");
        return;
      }

    });
  }

  changePassword(){

    this.accountService.updatePassword(this.model.adminNo, this.pwdModel).subscribe(res => {

      this.res = res;

      if(this.res.code === '0'){

        this.notificationService.smartMessageBox({
          title : "비밀번호를 변경하였습니다.",
          content : "다시 로그인하시겠습니까?",
          buttons : '[아니요, 메인화면으로..][네]'
        }, (ButtonPressed) => {
          if(ButtonPressed === '네'){
            this.authService.logout().subscribe( res => {
              this.res = res;
              if(this.res.code === '0'){
                this.cookieService.delete('token');
                sessionStorage.clear();
                this.router.navigate(['/auth/login']);
              } else {
                this.router.navigate(['/auth/login']);
              }
            });
          }else if(ButtonPressed === '아니요, 메인화면으로..'){
            this.router.navigate(['/main/company']);
          }
        });

      }

    });

  }



}
