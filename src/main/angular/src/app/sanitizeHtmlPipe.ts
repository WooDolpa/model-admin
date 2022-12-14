import {Pipe, PipeTransform} from "@angular/core";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

@Pipe({
   name : 'sanitizeHtml'
})

export class SanitizeHtmlPipe implements PipeTransform{

    constructor(private _sanitizaer: DomSanitizer) {
    }

    transform(v: string):SafeHtml {
        return this._sanitizaer.bypassSecurityTrustHtml(v);
    }

}
