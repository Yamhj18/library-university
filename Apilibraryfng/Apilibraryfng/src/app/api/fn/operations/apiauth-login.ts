/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface ApiauthLogin$Params {
    body?: {
        'email'?: string;
        'password'?: string;
    }
}

export function apiauthLogin(http: HttpClient, rootUrl: string, params?: ApiauthLogin$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    const rb = new RequestBuilder(rootUrl, apiauthLogin.PATH, 'post');
    if (params) {
        rb.body(params.body, 'application/json');
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

apiauthLogin.PATH = '/auth/login';