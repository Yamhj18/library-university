import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../api/auth.service';

export const guestGuard: CanActivateFn = () => {
    const authService = inject(AuthService);
    const router      = inject(Router);

    if (!authService.hasToken()) {
        return true;
    }

    router.navigate(['/catalog']);
    return false;
};