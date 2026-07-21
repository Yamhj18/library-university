/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

export interface ApibookInsert$Params {
    body?: {
        'idCategory'?: string;
        'code'?: string;
        'title'?: string;
        'author'?: string;
        'stockTotal'?: number;
        'image'?: Blob;
    }
}

export function apibookInsert(http: HttpClient, rootUrl: string, params?: ApibookInsert$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    const rb = new RequestBuilder(rootUrl, apibookInsert.PATH, 'post');
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

apibookInsert.PATH = '/book/insert';