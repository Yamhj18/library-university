import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { Api } from '../../api/api';
import { apiconfigGetall, apiconfigUpdate } from '../../api/functions';

@Component({
    selector: 'app-settings',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputNumberModule,
        ButtonModule
    ],
    templateUrl: './settings.html',
    styleUrl: './settings.css'
})
export class Settings implements OnInit {
    private messageService = inject(MessageService);
    private formBuilder = inject(FormBuilder);
    private api = inject(Api);

    frmSettings: FormGroup;

    get loanDaysFb() { return this.frmSettings.controls['LOAN_DAYS_LIMIT']; }
    get maxLoansFb() { return this.frmSettings.controls['MAX_ACTIVE_LOANS']; }

    constructor() {
        this.frmSettings = this.formBuilder.group({
            'LOAN_DAYS_LIMIT': [5, [Validators.required, Validators.min(1)]],
            'MAX_ACTIVE_LOANS': [3, [Validators.required, Validators.min(1)]]
        });
    }

    ngOnInit(): void {
        this.loadConfigs();
    }

    loadConfigs(): void {
        this.api.invoke(apiconfigGetall).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            if (data.listConfig) {
                const configs: any = {};
                data.listConfig.forEach((c: any) => {
                    configs[c.keyName] = parseInt(c.value, 10);
                });
                
                this.frmSettings.patchValue({
                    'LOAN_DAYS_LIMIT': configs['LOAN_DAYS_LIMIT'] || 5,
                    'MAX_ACTIVE_LOANS': configs['MAX_ACTIVE_LOANS'] || 3
                });
            }
        });
    }

    saveConfigs(): void {
        if (!this.frmSettings.valid) {
            this.frmSettings.markAllAsTouched();
            return;
        }

        const bodyParams = {
            body: {
                configs: {
                    'LOAN_DAYS_LIMIT': this.loanDaysFb.value.toString(),
                    'MAX_ACTIVE_LOANS': this.maxLoansFb.value.toString()
                }
            }
        };

        this.api.invoke(apiconfigUpdate, bodyParams).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            if (data.type === 'success') {
                this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Configuración actualizada correctamente.' });
            } else {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo actualizar la configuración.' });
            }
        }).catch(() => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Algo salió mal.' });
        });
    }
}
