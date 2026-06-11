import { Routes } from '@angular/router';
import { Catalog } from './page/catalog/catalog';
import { BookInsert } from './page/book/book-insert/book-insert';
import { LoanInsert } from './page/loan/loan-insert/loan-insert';
import { LoanList } from './page/loan/loan-list/loan-list';
import { Login } from './page/auth/login/login';
import { Register } from './page/auth/register/register';
import { authGuard } from './guard/auth.guard';
import { adminGuard } from './guard/admin.guard';
import { guestGuard } from './guard/guest.guard';

export const routes: Routes = [
    { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
    { path: 'auth/login',    component: Login,    canActivate: [guestGuard] },
    { path: 'auth/register', component: Register, canActivate: [guestGuard] },
    { path: 'catalog',       component: Catalog,  canActivate: [authGuard] },
    { path: 'book/insert',   component: BookInsert, canActivate: [adminGuard] },
    { path: 'loan/insert',   component: LoanInsert, canActivate: [adminGuard] },
    { path: 'loan/list',     component: LoanList,   canActivate: [adminGuard] },
    { path: '**', redirectTo: 'auth/login' }
];