/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface ApibookGetall$Params {
    search?: string;
    idCategory?: string;
}

export function apibookGetall(http: HttpClient, rootUrl: string, params?: ApibookGetall$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    const rb = new RequestBuilder(rootUrl, apibookGetall.PATH, 'get');
    if (params) {
        rb.query('search', params.search, {});
        rb.query('idCategory', params.idCategory, {});
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

apibookGetall.PATH = '/book/getall';