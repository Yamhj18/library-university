/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface ApiauthRegister$Params {
    body?: {
        'fullName'?: string;
        'email'?: string;
        'password'?: string;
    }
}

export function apiauthRegister(http: HttpClient, rootUrl: string, params?: ApiauthRegister$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    const rb = new RequestBuilder(rootUrl, apiauthRegister.PATH, 'post');
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

apiauthRegister.PATH = '/auth/register';