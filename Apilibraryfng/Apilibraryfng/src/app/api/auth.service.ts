import { Injectable, signal } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly TOKEN_KEY = 'library_token';
    private readonly ROLE_KEY  = 'library_role';
    private readonly NAME_KEY  = 'library_name';

    isLoggedIn = signal<boolean>(this.hasToken());
    userRole   = signal<string>(this.getRole());
    userName   = signal<string>(this.getName());

    saveSession(token: string, role: string, fullName: string): void {
        localStorage.setItem(this.TOKEN_KEY, token);
        localStorage.setItem(this.ROLE_KEY, role);
        localStorage.setItem(this.NAME_KEY, fullName);
        this.isLoggedIn.set(true);
        this.userRole.set(role);
        this.userName.set(fullName);
    }

    clearSession(): void {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.ROLE_KEY);
        localStorage.removeItem(this.NAME_KEY);
        this.isLoggedIn.set(false);
        this.userRole.set('');
        this.userName.set('');
    }

    getToken(): string {
        return localStorage.getItem(this.TOKEN_KEY) || '';
    }

    getRole(): string {
        return localStorage.getItem(this.ROLE_KEY) || '';
    }

    getName(): string {
        return localStorage.getItem(this.NAME_KEY) || '';
    }

    hasToken(): boolean {
        return !!localStorage.getItem(this.TOKEN_KEY);
    }

    isAdmin(): boolean {
        return this.getRole() === 'ADMIN';
    }

    isStudent(): boolean {
        return this.getRole() === 'STUDENT';
    }
}