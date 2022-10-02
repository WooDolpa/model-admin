import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {LayoutService} from "../../layout/layout.service";
import {Router} from "@angular/router";

@Component({

  selector: 'sa-login-info',
  templateUrl: './login-info.component.html',
})
export class LoginInfoComponent implements OnInit {

  adminName : string;

  constructor(private router : Router) {
  }

  ngOnInit() {
    this.adminName = sessionStorage.getItem('name');
  }

  move(){
    this.router.navigate(['/account/info']);
  }
}
