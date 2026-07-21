import { Routes } from '@angular/router';
import { authGuard } from './guard/auth.guard';
import { adminGuard } from './guard/admin.guard';
import { guestGuard } from './guard/guest.guard';
import { studentGuard } from './guard/student.guard';

export const routes: Routes = [
    { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
    
    // Auth Routes
    { path: 'auth/login',    loadComponent: () => import('./page/auth/login/login').then(m => m.Login),    canActivate: [guestGuard] },
    { path: 'auth/register', loadComponent: () => import('./page/auth/register/register').then(m => m.Register), canActivate: [guestGuard] },
    
    // Shared Routes
    { path: 'catalog',       loadComponent: () => import('./page/catalog/catalog').then(m => m.Catalog),  canActivate: [authGuard] },
    { path: 'rules',         loadComponent: () => import('./page/rules/rules').then(m => m.Rules),    canActivate: [authGuard] },
    
    // Admin Routes
    { path: 'dashboard',     loadComponent: () => import('./page/dashboard/dashboard').then(m => m.Dashboard), canActivate: [adminGuard] },
    { path: 'book/insert',   loadComponent: () => import('./page/book/book-insert/book-insert').then(m => m.BookInsert), canActivate: [adminGuard] },
    { path: 'loan/insert',   loadComponent: () => import('./page/loan/loan-insert/loan-insert').then(m => m.LoanInsert), canActivate: [adminGuard] },
    { path: 'loan/list',     loadComponent: () => import('./page/loan/loan-list/loan-list').then(m => m.LoanList),   canActivate: [adminGuard] },
    { path: 'student/list',  loadComponent: () => import('./page/student/student-list/student-list').then(m => m.StudentList), canActivate: [adminGuard] },
    { path: 'settings',      loadComponent: () => import('./page/settings/settings').then(m => m.Settings),    canActivate: [adminGuard] },
    
    // Student Routes
    { path: 'student/my-loans', loadComponent: () => import('./page/student/student-profile/student-profile').then(m => m.StudentProfile), canActivate: [studentGuard] },
    { path: 'student/profile',  loadComponent: () => import('./page/student/student-profile/student-profile').then(m => m.StudentProfile), canActivate: [studentGuard] },
    
    { path: '**', redirectTo: 'auth/login' }
];