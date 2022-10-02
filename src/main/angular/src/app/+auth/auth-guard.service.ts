import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot} from "@angular/router";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "./auth.service";

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {

    constructor(private router : Router,
                private cookieService : CookieService,
                private authService : AuthService) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url : string = state.url;
        return this.checkLogin(url);
    }

    canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.canActivate(route, state);
    }

    checkLogin(url : string) : boolean {

        if(this.cookieService.get('token') && sessionStorage.getItem('name')){
            this.authService.isLoggedIn = true;
        }

        if(this.authService.isLoggedIn){
            return true;
        }

        this.authService.redirectUrl = url;
        this.router.navigate(['/auth/login']);

        if(this.cookieService.get('token')){
            this.cookieService.delete('token');
        }

        return false;
    }
}
