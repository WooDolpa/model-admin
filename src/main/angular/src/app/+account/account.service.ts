import { Injectable } from '@angular/core';
import {ApiGatewayService} from "../api-gateway.service";
import {CheckTokenService} from "../check.token.service";
import {CookieService} from "ngx-cookie-service";
import {p} from "@angular/core/src/render3";

@Injectable()
export class AccountService {

  private userUrl : string = "user";

  res : any;

  constructor(private apiGatewayService : ApiGatewayService,
              private cookieService : CookieService,
              private tokenCheckService : CheckTokenService) { }

  findAccount(){

    //token
    const token = this.cookieService.get('token');

    this.res = this.apiGatewayService.get(`${this.userUrl}/findUser?token=${token}`);

    this.tokenCheckService.checkToken(this.res);
    return this.res;
  }

  updatePassword(adminNo, model){

    let data  = {
      adminNo : adminNo,
      password : model.password,
      newPassword : model.newPassword,
      newPasswordConfirm : model.newPasswordConfirm
    };

    let jsonBody = JSON.stringify(data);

    this.res = this.apiGatewayService.post(`${this.userUrl}/update/password`, jsonBody);
    this.tokenCheckService.checkToken(this.res);

    return this.res;
  }


  changeUserInfo(model){

    let data = {
      adminNo : model.adminNo,
      id : model.id,
      name : model.name,
      email : model.email
    };

    let jsonBody = JSON.stringify(data);

    this.res = this.apiGatewayService.post(`${this.userUrl}/update/user`, jsonBody);
    this.tokenCheckService.checkToken(this.res);

    return this.res;
  }
}
