/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface ApiloanInsert$Params {
    body?: {
        'idBook'?: string;
        'studentCode'?: string;
        'studentName'?: string;
        'faculty'?: string;
        'phoneNumber'?: string;
        'guaranteeType'?: string;
        'guaranteeNumber'?: string;
        'estimatedReturnDate'?: string;
    }
}

export function apiloanInsert(http: HttpClient, rootUrl: string, params?: ApiloanInsert$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    const rb = new RequestBuilder(rootUrl, apiloanInsert.PATH, 'post');
    if (params) {
        rb.body(params.body, 'multipart/form-data');
    }

    return http.request(
        rb.build({ responseType: 'text', accept: '*/*', context })
    ).pipe(
        filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
        map((r: HttpResponse<any>) => {
            return (r as HttpResponse<any>).clone({ body: undefined }) as StrictHttpResponse<void>;
        })
    );
}

apiloanInsert.PATH = '/loan/insert';