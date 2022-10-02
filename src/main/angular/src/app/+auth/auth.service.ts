import {Injectable} from "@angular/core";
import {ApiGatewayService} from "../api-gateway.service";

@Injectable()
export class AuthService {

    isLoggedIn : boolean = false;
    redirectUrl : string;

    private loginUrl = 'auth/login';
    private logoutUrl = 'auth/logout';

    constructor(private apiGatewayService : ApiGatewayService) {
    }

    login(id, password){
        let loginInfo = {
            id : id,
            password : password
        };

        let loginBody = JSON.stringify(loginInfo);

        return this.apiGatewayService.post(this.loginUrl, loginBody);
    }

    logout(){
        this.isLoggedIn = false;
        return this.apiGatewayService.delete(`${this.logoutUrl}/`);
    }

    tempPassword(){
        return this.apiGatewayService.get(`auth/temp/password`);
    }

}
