/* eslint-disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ApiConfiguration } from './api-configuration';
import { StrictHttpResponse } from './strict-http-response';
import { AuthService } from './auth.service';

export type ApiFnOptional<P, R> = (http: HttpClient, rootUrl: string, params?: P, context?: HttpContext) => Observable<StrictHttpResponse<R>>;
export type ApiFnRequired<P, R> = (http: HttpClient, rootUrl: string, params: P, context?: HttpContext) => Observable<StrictHttpResponse<R>>;

@Injectable({ providedIn: 'root' })
export class Api {
    constructor(
        private config: ApiConfiguration,
        private http: HttpClient,
        private authService: AuthService
    ) {}

    private _rootUrl?: string;

    get rootUrl(): string {
        return this._rootUrl || this.config.rootUrl;
    }

    set rootUrl(rootUrl: string) {
        this._rootUrl = rootUrl;
    }

    async invoke<P, R>(fn: any, params?: P, context?: HttpContext): Promise<R> {
        const resp = await this.invoke$Response<P, R>(fn, params, context);
        return resp.body;
    }

    invoke$Response<P, R>(fn: any, params?: P, context?: HttpContext): Promise<StrictHttpResponse<R>> {
        const token = this.authService.getToken();
        const headers: any = {};
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const httpWithAuth = new Proxy(this.http, {
            get: (target, prop) => {
                if (prop === 'request') {
                    return (method: any, url: any, options: any = {}) => {
                        options.headers = {
                            ...(options.headers || {}),
                            ...headers
                        };
                        return (target as any).request(method, url, options);
                    };
                }
                return (target as any)[prop];
            }
        });

        const obs = fn(httpWithAuth, this.rootUrl, params, context).pipe(
            filter((r: any) => r instanceof HttpResponse),
            map((r: any) => r as StrictHttpResponse<R>)
        );
        return firstValueFrom(obs);
    }
}