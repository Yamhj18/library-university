/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface ApiloanReturn$Params {
    idLoan: string;
}

export function apiloanReturn(http: HttpClient, rootUrl: string, params: ApiloanReturn$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    const rb = new RequestBuilder(rootUrl, apiloanReturn.PATH.replace('{idLoan}', params.idLoan), 'put');

    return http.request(
        rb.build({ responseType: 'text', accept: '*/*', context })
    ).pipe(
        filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
        map((r: HttpResponse<any>) => {
            return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
        })
    );
}

apiloanReturn.PATH = '/loan/return/{idLoan}';