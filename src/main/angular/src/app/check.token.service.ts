import {Injectable} from "@angular/core";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";

@Injectable()
export class CheckTokenService {

    constructor(private cookieService : CookieService,
                private router : Router) {
    }

    res : any;

    checkToken(res : any){
        this.res = res;
        if(this.res.code === -1 || this.res.code === -2){
            this.cookieService.delete('token');
            alert("장시간 입력이 없어 로그아웃 되었습니다.\n다시 로그인해주세요.");
            this.router.navigate(['/auth/login']);
        }
    }
}
