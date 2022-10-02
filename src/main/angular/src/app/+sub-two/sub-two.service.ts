import { Injectable } from '@angular/core';
import {ApiGatewayService} from "../api-gateway.service";
import {CheckTokenService} from "../check.token.service";

@Injectable()
export class SubTwoService {

  private subTwoUri : string = "sub2";

  res : any;

  constructor(private apiGatewayService : ApiGatewayService,
              private checkTokenService : CheckTokenService) { }

  getItemList(groupId){
    this.res = this.apiGatewayService.get(`${this.subTwoUri}/item/list?groupId=${groupId}`);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  insertItem(model, file){

    const formData = new FormData();

    formData.append('groupId', model.group);
    formData.append('title', model.title);
    formData.append('description', model.description);
    formData.append('img', model.img);
    formData.append("url",model.url);

    if(file){
      formData.append('img_file',file,file.name);
    }

    this.res = this.apiGatewayService.imgPost(`${this.subTwoUri}/item/add`, formData);
    this.checkTokenService.checkToken(this.res);
    return this.res;

  }

  updateItem(model, file){

    const formData = new FormData();

    formData.append('id', model.no);
    formData.append("groupId", model.group);
    formData.append('title', model.title);
    formData.append('description', model.description);
    formData.append('img', model.img);
    formData.append('rank', model.rank);
    formData.append("url",model.url);

    if(file){
      formData.append('img_file',file,file.name);
    }

    this.res = this.apiGatewayService.imgPost(`${this.subTwoUri}/item/update`, formData);
    this.checkTokenService.checkToken(this.res);
    return this.res;

  }

  deleteItem (no){

    this.res = this.apiGatewayService.delete(`${this.subTwoUri}/item/delete?galleryNo=${no}`);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  getItemText(){
    this.res = this.apiGatewayService.get(`${this.subTwoUri}/item/text`);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  updateItemText(model){

    const data = {
      txtNo : model.no,
      txtContents : model.contents
    };

    const jsonBody = JSON.stringify(data);

    this.res = this.apiGatewayService.post(`${this.subTwoUri}/item/text/update`, jsonBody);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  getTypeList(type, keyword, page, size) {
    this.res = this.apiGatewayService.get(`${this.subTwoUri}/type/list?type=${type}&keyword=${keyword}&page=${page}&size=${size}`);
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

    this.res = this.apiGatewayService.post(`${this.subTwoUri}/type/update`, jsonBody);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

  groupList(){
    this.res = this.apiGatewayService.get(`${this.subTwoUri}/group/list`);
    this.checkTokenService.checkToken(this.res);
    return this.res;
  }

}
