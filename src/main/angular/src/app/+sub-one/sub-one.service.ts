import { Injectable } from '@angular/core';
import {ApiGatewayService} from "../api-gateway.service";
import {CheckTokenService} from "../check.token.service";

@Injectable()
export class SubOneService {

  private subOneUri : string = "sub1";

  res : any;

  constructor(private apiGatewayService : ApiGatewayService,
              private checkTokenService : CheckTokenService) { }

  getItemList(group, type, keyword, page, size){
    this.res = this.apiGatewayService.get(`${this.subOneUri}/item/list?type=${type}&group=${group}&keyword=${keyword}&page=${page}&size=${size}`);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  getGroupList () {
    this.res = this.apiGatewayService.get(`${this.subOneUri}/group/list`);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  regItem(model){

    let data = {
      awardName : model.awardName,
      awardType : model.group,
      url : model.url,
      rank : model.rank,
      dataStatus: model.dataStatus
    };

    let jsonBody = JSON.stringify(data);

    this.res = this.apiGatewayService.post(`${this.subOneUri}/item/reg`, jsonBody);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  updItem(model){

    let data = {
      awardNo : model.awardNo,
      awardName : model.awardName,
      awardType: model.group,
      url : model.url,
      rank : model.rank,
      dataStatus : model.dataStatus
    };

    let jsonBody = JSON.stringify(data);

    this.res = this.apiGatewayService.post(`${this.subOneUri}/item/update`, jsonBody);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  deleteItem(awardNo){

    let data = {
      awardNo : awardNo
    };

    let jsonBody = JSON.stringify(data);

    this.res = this.apiGatewayService.post(`${this.subOneUri}/item/delete`, jsonBody);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  getTypeList(type, keyword, page, size){
    this.res = this.apiGatewayService.get(`${this.subOneUri}/type/list?type=${type}&keyword=${keyword}&page=${page}&size=${size}`);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  updType(model){

    let data = {
      id : model.id,
      name : model.name,
      rank : model.rank,
      dataStatus : model.dataStatus
    };

    let jsonBody = JSON.stringify(data);

    this.res = this.apiGatewayService.post(`${this.subOneUri}/type/update`, jsonBody);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

}
