import {Component, OnInit} from '@angular/core';
import {ShareService} from "../../../share.service";
import {MainService} from "../../../+main/main.service";


@Component({

  selector: 'sa-navigation',
  templateUrl: './navigation.component.html'
})
export class NavigationComponent implements OnInit {

  textBoolean : boolean = false;
  sub_title_one : string = "";
  sub_title_two : string = "";

  res : any;

  constructor(private shareService : ShareService,
              private mainService : MainService) {
    this.shareService.changeEmitted$.subscribe(text => {
      if(text === "Y"){
        this.getNavigationList();
      }
    });
  }

  ngOnInit() {
    this.getNavigationList();
  }

  getNavigationList(){

    const dataStatus = "1";
    this.textBoolean = false;

    this.mainService.getNavigationList(dataStatus).subscribe(res => {

      this.res = res;
      if(this.res.code === '0'){

          this.textBoolean = true;
          this.sub_title_one  = "";
          this.sub_title_two  = "";

          if(this.res.data[0] != null){
            this.sub_title_one = this.res.data[0].title;
          }

          if(this.res.data[1] != null){
            this.sub_title_two = this.res.data[1].title;
          }

      }

    });
  }

}
