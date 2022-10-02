import {Injectable} from "@angular/core";
import {ApiGatewayService} from "../api-gateway.service";
import {CheckTokenService} from "../check.token.service";

@Injectable()
export class MainService {

    private mainUrl = "main";

    res : any;

    constructor(private apiGatewayService: ApiGatewayService,
                private checkTokenService : CheckTokenService) { }

    /**
     * 회사 정보 조회
     */
    getCompany(){
        this.res = this.apiGatewayService.get(`${this.mainUrl}/getCompany`);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }

    /**
     * 회사 정보 수정
     * @param model
     */
    updateCompany(model, file){

        const formData = new FormData();

        formData.append("cpNo", model.cpNo);
        formData.append("cpName", model.cpName);
        formData.append("img", model.img);

        if(file){
            formData.append('company_img',file,file.name);
        }

        this.res = this.apiGatewayService.imgPost(`${this.mainUrl}/updateCompany`,formData);
        this.checkTokenService.checkToken(this.res);
        return this.res;

    }

    /**
     * 네비게이션 조회
     */
    getNavigationList(dataStatus){
        this.res = this.apiGatewayService.get(`${this.mainUrl}/getNavigationList?dataStatus=${dataStatus}`);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }

    /**
     * 네비게이션 수정
     * @param model
     */
    updateNavigation(model){

        let data = {
            naviNo : model.naviNo,
            title : model.title,
            dataStatus : model.dataStatus
        };

        let jsonBody = JSON.stringify(data);

        this.res = this.apiGatewayService.post(`${this.mainUrl}/updateNavigation`, jsonBody);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }

    /**
     * 슬라이드 조회
     * @param dataStatus
     * @param keyword
     * @param offset
     * @param size
     */
    getSliderList(dataStatus, keyword, offset, size){
        this.res = this.apiGatewayService.get(`${this.mainUrl}/slider/list?dataStatus=${dataStatus}&keyword=${keyword}&offset=${offset}&size=${size}`);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }

    /**
     * 슬라이드 등록
     * @param model
     * @param file
     */
    insertSlider(model, file){

        const formData = new FormData();

        formData.append('title', model.title);
        formData.append('content', model.content);
        formData.append('img', model.img);
        formData.append("dataStatus",model.dataStatus);

        if(file){
            formData.append('slider_img',file,file.name);
        }

        this.res = this.apiGatewayService.imgPost(`${this.mainUrl}/slider/add`, formData);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }

    /**
     * 슬라이드 수정
     * @param model
     * @param file
     */
    updateSlider(model, file){

        const formData = new FormData();

        formData.append("sliderNo", model.no);
        formData.append('title', model.title);
        formData.append('content', model.content);
        formData.append("sort", model.sort);
        formData.append('img', model.img);
        formData.append("dataStatus",model.dataStatus);

        if(file){
            formData.append('slider_img',file,file.name);
        }

        this.res = this.apiGatewayService.imgPost(`${this.mainUrl}/slider/update`, formData);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }

    /**
     * 슬라이드 삭제
     * @param model
     */
    deleteSlider(model){

        let data = {
          sliderNo : model.no
        };

        let jsonBody = JSON.stringify(data);

        this.res = this.apiGatewayService.post(`${this.mainUrl}/slider/delete`, jsonBody);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }

    /**
     * contents 조회
     */
    findContent(){
        this.res = this.apiGatewayService.get(`${this.mainUrl}/contents`);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }


    updateContent(model){

        let data = {
          ctNo : model.no,
          ctTxt : model.txt
        };

        let jsonBody = JSON.stringify(data);

        this.res = this.apiGatewayService.post(`${this.mainUrl}/update/contents`, jsonBody);
        this.checkTokenService.checkToken(this.res);
        return this.res;
    }
}
