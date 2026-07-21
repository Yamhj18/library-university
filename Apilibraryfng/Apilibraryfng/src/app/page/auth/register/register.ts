import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { MessageService } from 'primeng/api';
import { Api } from '../../../api/api';
import { apiauthRegister, ApiauthRegister$Params, apischoolGetall } from '../../../api/functions';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        InputTextModule,
        PasswordModule,
        ButtonModule,
        SelectModule
    ],
    templateUrl: './register.html',
    styleUrl: './register.css'
})
export class Register implements OnInit {
    private router         = inject(Router);
    private messageService = inject(MessageService);

    frmRegister: FormGroup;
    errorMessage:   string = '';
    successMessage: string = '';
    
    schools: any[] = [];
    isLoadingSchools: boolean = false;

    get fullNameFb()    { return this.frmRegister.controls['fullName']; }
    get dniFb()         { return this.frmRegister.controls['dni']; }
    get studentCodeFb() { return this.frmRegister.controls['studentCode']; }
    get idSchoolFb()    { return this.frmRegister.controls['idSchool']; }
    get emailFb()       { return this.frmRegister.controls['email']; }
    get phoneNumberFb() { return this.frmRegister.controls['phoneNumber']; }

    constructor(
        private formBuilder: FormBuilder,
        private api: Api
    ) {
        this.frmRegister = this.formBuilder.group({
            'fullName':    ['', [Validators.required]],
            'dni':         ['', [Validators.required, Validators.pattern('^[0-9]{8}$')]],
            'studentCode': ['', [Validators.required]],
            'idSchool':    ['', [Validators.required]],
            'email':       ['', [Validators.required, Validators.email]],
            'phoneNumber': ['', [Validators.pattern('^([0-9]{9})?$')]]
        });
    }

    ngOnInit(): void {
        this.loadSchools();
    }
    
    loadSchools(): void {
        this.isLoadingSchools = true;
        this.api.invoke(apischoolGetall).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            if (data.type === 'success') {
                this.schools = data.listSchool || [];
            }
        }).catch(() => {
            this.errorMessage = 'No se pudieron cargar las escuelas.';
        }).finally(() => {
            this.isLoadingSchools = false;
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
                fullName:    this.fullNameFb.value,
                dni:         this.dniFb.value,
                studentCode: this.studentCodeFb.value,
                idSchool:    this.idSchoolFb.value,
                email:       this.emailFb.value,
                phoneNumber: this.phoneNumberFb.value
            } as any
        };

        this.api.invoke(apiauthRegister, params).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            if (data.type === 'success') {
                this.successMessage = 'Registro exitoso. Tu DNI será tu contraseña.';
                this.frmRegister.reset();
                setTimeout(() => this.router.navigate(['/auth/login']), 3000);
            } else {
                this.errorMessage = data.listMessage[0];
            }
        }).catch(() => {
            this.errorMessage = 'Error al conectar con el servidor.';
        });
    }
}