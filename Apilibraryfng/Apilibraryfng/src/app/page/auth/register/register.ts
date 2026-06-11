import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { Api } from '../../../api/api';
import { apiauthRegister, ApiauthRegister$Params } from '../../../api/functions';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        InputTextModule,
        PasswordModule,
        ButtonModule
    ],
    templateUrl: './register.html',
    styleUrl: './register.css'
})
export class Register {
    private router         = inject(Router);
    private messageService = inject(MessageService);

    frmRegister: FormGroup;
    errorMessage:   string = '';
    successMessage: string = '';

    get fullNameFb() { return this.frmRegister.controls['fullName']; }
    get emailFb()    { return this.frmRegister.controls['email']; }
    get passwordFb() { return this.frmRegister.controls['password']; }

    constructor(
        private formBuilder: FormBuilder,
        private api: Api
    ) {
        this.frmRegister = this.formBuilder.group({
            'fullName': ['', [Validators.required]],
            'email':    ['', [Validators.required, Validators.email]],
            'password': ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    goToLogin(): void {
        this.router.navigate(['/auth/login']);
    }

    sendRegister(): void {
        if (!this.frmRegister.valid) {
            this.frmRegister.markAllAsTouched();
            return;
        }

        this.errorMessage   = '';
        this.successMessage = '';

        const params: ApiauthRegister$Params = {
            body: {
                fullName: this.fullNameFb.value,
                email:    this.emailFb.value,
                password: this.passwordFb.value
            }
        };

        this.api.invoke(apiauthRegister, params).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            if (data.type === 'success') {
                this.successMessage = data.listMessage[0];
                this.frmRegister.reset();
                setTimeout(() => this.router.navigate(['/auth/login']), 2000);
            } else {
                this.errorMessage = data.listMessage[0];
            }
        }).catch(() => {
            this.errorMessage = 'Error al conectar con el servidor.';
        });
    }
}