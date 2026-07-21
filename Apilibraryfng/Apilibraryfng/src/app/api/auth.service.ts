import { Injectable, signal, inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private platformId = inject(PLATFORM_ID);
    private isBrowser = isPlatformBrowser(this.platformId);

    private readonly TOKEN_KEY = 'library_token';
    private readonly ROLE_KEY  = 'library_role';
    private readonly NAME_KEY  = 'library_name';

    isLoggedIn = signal<boolean>(false);
    userRole   = signal<string>('');
    userName   = signal<string>('');

    constructor() {
        if (this.isBrowser) {
            this.isLoggedIn.set(this.hasToken());
            this.userRole.set(this.getRole());
            this.userName.set(this.getName());
        }
    }

    saveSession(token: string, role: string, fullName: string): void {
        if (this.isBrowser) {
            localStorage.setItem(this.TOKEN_KEY, token);
            localStorage.setItem(this.ROLE_KEY, role);
            localStorage.setItem(this.NAME_KEY, fullName);
        }
        this.isLoggedIn.set(true);
        this.userRole.set(role);
        this.userName.set(fullName);
    }

    clearSession(): void {
        if (this.isBrowser) {
            localStorage.removeItem(this.TOKEN_KEY);
            localStorage.removeItem(this.ROLE_KEY);
            localStorage.removeItem(this.NAME_KEY);
        }
        this.isLoggedIn.set(false);
        this.userRole.set('');
        this.userName.set('');
    }

    getToken(): string {
        return this.isBrowser ? (localStorage.getItem(this.TOKEN_KEY) || '') : '';
    }

    getRole(): string {
        return this.isBrowser ? (localStorage.getItem(this.ROLE_KEY) || '') : '';
    }

    getName(): string {
        return this.isBrowser ? (localStorage.getItem(this.NAME_KEY) || '') : '';
    }

    hasToken(): boolean {
        return this.isBrowser ? !!localStorage.getItem(this.TOKEN_KEY) : false;
    }

    isAdmin(): boolean {
        return this.getRole() === 'ADMIN';
    }

    isStudent(): boolean {
        return this.getRole() === 'STUDENT';
    }
}