/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface ApiloanGetall$Params {
    search?: string;
}

export function apiloanGetall(http: HttpClient, rootUrl: string, params?: ApiloanGetall$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    const rb = new RequestBuilder(rootUrl, apiloanGetall.PATH, 'get');
    if (params) {
        rb.query('search', params.search, {});
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

apiloanGetall.PATH = '/loan/getall';