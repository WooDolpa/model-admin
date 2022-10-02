import { Component, OnInit } from '@angular/core';
import {MainService} from "../../../+main/main.service";

@Component({
  selector: 'sa-footer',
  templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {

  constructor(private mainService : MainService) {}

  res : any;
  companyName : string = '';

  ngOnInit() {
    this.getCompany();
  }

  getCompany(){
    this.mainService.getCompany().subscribe(res => {
        this.res = res;
        this.companyName = 'CopyRights © '+this.res.data[0].cpName;
    });
  }

}
