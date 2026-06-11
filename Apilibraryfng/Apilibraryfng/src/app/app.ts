import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterModule, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { AvatarModule } from 'primeng/avatar';
import { MenuItem, MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { AuthService } from './api/auth.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [
        CommonModule,
        RouterOutlet,
        RouterModule,
        ButtonModule,
        MenuModule,
        AvatarModule,
        ToastModule,
        ConfirmDialogModule
    ],
    templateUrl: './app.html',
    styleUrls: ['./app.css']
})
export class App {
    authService    = inject(AuthService);
    private router = inject(Router);

    isAuthPage = computed(() => {
        return !this.authService.isLoggedIn();
    });

    adminNavItems: MenuItem[] = [
        { label: 'Catálogo',           icon: 'pi pi-book',   routerLink: '/catalog' },
        { label: 'Registrar libro',    icon: 'pi pi-plus',   routerLink: '/book/insert' },
        { label: 'Registrar préstamo', icon: 'pi pi-send',   routerLink: '/loan/insert' },
        { label: 'Devoluciones',       icon: 'pi pi-replay', routerLink: '/loan/list' }
    ];

    studentNavItems: MenuItem[] = [
        { label: 'Catálogo', icon: 'pi pi-book', routerLink: '/catalog' }
    ];

    get navItems(): MenuItem[] {
        return this.authService.isAdmin() ? this.adminNavItems : this.studentNavItems;
    }

    get profileItems(): MenuItem[] {
        return [
            { label: this.authService.userName(), icon: 'pi pi-user', disabled: true },
            { separator: true },
            { label: 'Cerrar sesión', icon: 'pi pi-sign-out', command: () => this.logout() }
        ];
    }

    logout(): void {
        this.authService.clearSession();
        this.router.navigate(['/auth/login']);
    }
}