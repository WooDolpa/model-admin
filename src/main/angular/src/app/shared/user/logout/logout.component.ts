import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {NotificationService} from "../../utils/notification.service";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../../../+auth/auth.service";

declare var $:any;

@Component({
  selector: 'sa-logout',
  template: `
<div id="logout" (click)="showPopup()" class="btn-header transparent pull-right">
        <span> <a routerlink="/auth/login" title="Sign Out" data-action="userLogout"
                  data-logout-msg="로그아웃하시겠습니까?"><i
          class="fa fa-sign-out"></i></a> </span>
    </div>
  `,
  styles: []
})
export class LogoutComponent implements OnInit {

  res : any;

  constructor(private router: Router,
              private notificationService: NotificationService,
              private cookieService : CookieService,
              private authService : AuthService) { }

  showPopup(){
    this.notificationService.smartMessageBox({
      title : "<i class='fa fa-sign-out txt-color-orangeDark'></i> 로그아웃 <span class='txt-color-orangeDark'><strong>" + $('#show-shortcut').text() + "</strong></span> ",
      content : "로그아웃하시겠습니까?",
      buttons : '[아니요][예]'

    }, (ButtonPressed) => {
      if (ButtonPressed === "예") {
        this.logout();
      }
    });
  }

  logout(){
    this.authService.logout().subscribe(res => {
      this.res = res;
      if(this.res.code === '0'){
        this.cookieService.delete('token');
        this.router.navigate(['/auth/login']);
      }
    });

  }

  ngOnInit() {

  }



}
