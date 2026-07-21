import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../api/auth.service';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    let clonedRequest = req;
    
    if (authService.hasToken()) {
        clonedRequest = req.clone({
            setHeaders: {
                Authorization: `Bearer ${authService.getToken()}`
            }
        });
    }

    return next(clonedRequest).pipe(
        catchError(err => {
            if (err.status === 401 || err.status === 403) {
                authService.clearSession();
                router.navigate(['/auth/login']);
            }
            return throwError(() => err);
        })
    );
};
