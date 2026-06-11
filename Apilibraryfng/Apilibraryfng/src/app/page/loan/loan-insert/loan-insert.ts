import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { RadioButtonModule } from 'primeng/radiobutton';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { DatePickerModule } from 'primeng/datepicker';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Api } from '../../../api/api';
import { apibookGetall, apiloanInsert, apifacultyGetall, ApiloanInsert$Params } from '../../../api/functions';

@Component({
    selector: 'app-loan-insert',
    standalone: true,
    imports: [
        FormsModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        RadioButtonModule,
        AutoCompleteModule,
        DatePickerModule
    ],
    templateUrl: './loan-insert.html',
    styleUrl: './loan-insert.css'
})
export class LoanInsert implements OnInit {
    private confirmationService = inject(ConfirmationService);
    private messageService = inject(MessageService);

    frmLoanInsert: FormGroup;
    listBookSuggestions: any[] = [];
    listFaculty: any[] = [];
    minDate: Date = new Date();

    get bookFb()                { return this.frmLoanInsert.controls['book']; }
    get studentCodeFb()         { return this.frmLoanInsert.controls['studentCode']; }
    get studentNameFb()         { return this.frmLoanInsert.controls['studentName']; }
    get facultyFb()             { return this.frmLoanInsert.controls['faculty']; }
    get phoneNumberFb()         { return this.frmLoanInsert.controls['phoneNumber']; }
    get guaranteeTypeFb()       { return this.frmLoanInsert.controls['guaranteeType']; }
    get guaranteeNumberFb()     { return this.frmLoanInsert.controls['guaranteeNumber']; }
    get estimatedReturnDateFb() { return this.frmLoanInsert.controls['estimatedReturnDate']; }

    constructor(
        private formBuilder: FormBuilder,
        private api: Api
    ) {
        this.frmLoanInsert = this.formBuilder.group({
            'book':                ['', [Validators.required]],
            'studentCode':         ['', [Validators.required]],
            'studentName':         ['', [Validators.required]],
            'faculty':             ['', [Validators.required]],
            'phoneNumber':         ['', [Validators.required]],
            'guaranteeType':       ['DNI', [Validators.required]],
            'guaranteeNumber':     ['', [Validators.required]],
            'estimatedReturnDate': ['', [Validators.required]]
        });
    }

    ngOnInit(): void {
        this.loadFaculties();
    }

    private loadFaculties(): void {
        this.api.invoke(apifacultyGetall).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listFaculty = data.listFaculty;
        });
    }

    searchBook(event: any): void {
        this.api.invoke(apibookGetall, { search: event.query }).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listBookSuggestions = (data.listBook || [])
                .filter((b: any) => b.status === 'Available')
                .map((b: any) => ({
                    ...b,
                    displayName: `[${b.code}] ${b.title} - ${b.author}`
                }));
        }).catch(() => {
            this.listBookSuggestions = [];
        });
    }

    sendInsert(event: Event): void {
        if (!this.frmLoanInsert.valid) {
            this.frmLoanInsert.markAllAsTouched();
            this.frmLoanInsert.markAsDirty();
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Por favor complete todos los campos requeridos.' });
            return;
        }

        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: '¿Confirmar registro del préstamo?',
            header: 'Confirmación',
            icon: 'pi pi-info-circle',
            rejectButtonProps: { label: 'Cancelar', severity: 'secondary', outlined: true },
            acceptButtonProps: { label: 'Aceptar', severity: 'primary' },
            accept: () => {
                const d: Date = this.estimatedReturnDateFb.value;
                const dateStr = d.toISOString().split('T')[0];

                const bodyParams: ApiloanInsert$Params = {
                    body: {
                        idBook:              this.bookFb.value.idBook,
                        studentCode:         this.studentCodeFb.value,
                        studentName:         this.studentNameFb.value,
                        faculty:             this.facultyFb.value.name,
                        phoneNumber:         this.phoneNumberFb.value,
                        guaranteeType:       this.guaranteeTypeFb.value,
                        guaranteeNumber:     this.guaranteeNumberFb.value,
                        estimatedReturnDate: dateStr
                    }
                };

                this.api.invoke(apiloanInsert, bodyParams).then((response: any) => {
                    const data = typeof response === 'string' ? JSON.parse(response) : response;
                    switch (data.type) {
                        case 'success':
                            this.messageService.add({ severity: 'success', summary: 'Correcto', detail: data.listMessage[0] });
                            this.frmLoanInsert.reset({ guaranteeType: 'DNI' });
                            break;
                        case 'error':
                            this.messageService.add({ severity: 'error', summary: 'Error', detail: data.listMessage[0] });
                            break;
                    }
                }).catch(() => {
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Algo ocurrió mal.' });
                });
            },
            reject: () => {}
        });
    }
}