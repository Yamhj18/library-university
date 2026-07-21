import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterModule, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
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
        { label: 'Dashboard',          icon: 'pi pi-chart-pie', routerLink: '/dashboard' },
        { label: 'Catálogo',           icon: 'pi pi-book',      routerLink: '/catalog' },
        { label: 'Préstamos',          icon: 'pi pi-send',      routerLink: '/loan/list' },
        { label: 'Estudiantes',        icon: 'pi pi-users',     routerLink: '/student/list' },
        { label: 'Reglamento',         icon: 'pi pi-file',      routerLink: '/rules' },
        { label: 'Configuración',      icon: 'pi pi-cog',       routerLink: '/settings' }
    ];

    studentNavItems: MenuItem[] = [
        { label: 'Catálogo',           icon: 'pi pi-book',      routerLink: '/catalog' },
        { label: 'Mis Préstamos',      icon: 'pi pi-clock',     routerLink: '/student/my-loans' },
        { label: 'Mi Perfil',          icon: 'pi pi-user',      routerLink: '/student/profile' },
        { label: 'Reglamento',         icon: 'pi pi-file',      routerLink: '/rules' }
    ];

    get navItems(): MenuItem[] {
        return this.authService.isAdmin() ? this.adminNavItems : this.studentNavItems;
    }

    get profileItems(): MenuItem[] {
        return [
            { label: 'Mi Perfil', icon: 'pi pi-user', routerLink: '/student/profile' },
            { separator: true },
            { label: 'Cerrar sesión', icon: 'pi pi-sign-out', command: () => this.logout() }
        ];
    }

    logout(): void {
        this.authService.clearSession();
        this.router.navigate(['/auth/login']);
    }
}