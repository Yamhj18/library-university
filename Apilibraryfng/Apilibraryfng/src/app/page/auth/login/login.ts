import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { MessageService } from 'primeng/api';
import { Api } from '../../../api/api';
import { AuthService } from '../../../api/auth.service';
import { apiauthLogin, ApiauthLogin$Params } from '../../../api/functions';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        InputTextModule,
        PasswordModule,
        ButtonModule,
        MessageModule
    ],
    templateUrl: './login.html',
    styleUrl: './login.css'
})
export class Login {
    private router     = inject(Router);
    private authService = inject(AuthService);
    private messageService = inject(MessageService);

    frmLogin: FormGroup;
    errorMessage: string = '';

    get emailFb()    { return this.frmLogin.controls['email']; }
    get passwordFb() { return this.frmLogin.controls['password']; }

    constructor(
        private formBuilder: FormBuilder,
        private api: Api
    ) {
        this.frmLogin = this.formBuilder.group({
            'email':    ['', [Validators.required, Validators.email]],
            'password': ['', [Validators.required]]
        });
    }

    goToRegister(): void {
        this.router.navigate(['/auth/register']);
    }

    sendLogin(): void {
        if (!this.frmLogin.valid) {
            this.frmLogin.markAllAsTouched();
            return;
        }

        this.errorMessage = '';

        const params: ApiauthLogin$Params = {
            body: {
                email:    this.emailFb.value,
                password: this.passwordFb.value
            }
        };

        this.api.invoke(apiauthLogin, params).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            if (data.type === 'success') {
                this.authService.saveSession(data.token, data.role, data.fullName);
                if (data.role === 'ADMIN') {
                    this.router.navigate(['/catalog']);
                } else {
                    this.router.navigate(['/catalog']);
                }
            } else {
                this.errorMessage = data.listMessage[0];
            }
        }).catch(() => {
            this.errorMessage = 'Error al conectar con el servidor.';
        });
    }
}