import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { RadioButtonModule } from 'primeng/radiobutton';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { DatePickerModule } from 'primeng/datepicker';
import { TextareaModule } from 'primeng/textarea';
import { InputNumberModule } from 'primeng/inputnumber';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Api } from '../../../api/api';
import { AuthService } from '../../../api/auth.service';
import { apibookGetall, apiloanInsert, apistudentSearch, ApiloanInsert$Params } from '../../../api/functions';

@Component({
    selector: 'app-loan-insert',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        RadioButtonModule,
        AutoCompleteModule,
        DatePickerModule,
        TextareaModule,
        InputNumberModule
    ],
    templateUrl: './loan-insert.html',
    styleUrl: './loan-insert.css'
})
export class LoanInsert implements OnInit {
    private confirmationService = inject(ConfirmationService);
    private messageService = inject(MessageService);
    private router = inject(Router);
    auth = inject(AuthService);

    frmLoanInsert: FormGroup;
    listBookSuggestions: any[] = [];
    listStudentSuggestions: any[] = [];
    minDate: Date = new Date();

    // List of Guarantee Types for select dropdown
    listGuaranteeTypes = [
        { name: 'DNI', value: 'DNI' },
        { name: 'Carnet Universitario', value: 'CarnetUniversitario' }
    ];

    get searchBookFb()          { return this.frmLoanInsert.controls['searchBook']; }
    get searchStudentFb()       { return this.frmLoanInsert.controls['searchStudent']; }
    get bookTitleFb()           { return this.frmLoanInsert.controls['bookTitle']; }
    get bookCodeFb()            { return this.frmLoanInsert.controls['bookCode']; }
    get bookAuthorFb()          { return this.frmLoanInsert.controls['bookAuthor']; }
    get bookCategoryFb()        { return this.frmLoanInsert.controls['bookCategory']; }
    get quantityFb()            { return this.frmLoanInsert.controls['quantity']; }
    
    get studentNameFb()         { return this.frmLoanInsert.controls['studentName']; }
    get studentCodeFb()         { return this.frmLoanInsert.controls['studentCode']; }
    get studentDniFb()          { return this.frmLoanInsert.controls['studentDni']; }
    get studentSchoolFb()       { return this.frmLoanInsert.controls['studentSchool']; }
    get studentPhoneFb()        { return this.frmLoanInsert.controls['studentPhone']; }
    get studentEmailFb()        { return this.frmLoanInsert.controls['studentEmail']; }

    get guaranteeTypeFb()       { return this.frmLoanInsert.controls['guaranteeType']; }
    get guaranteeNumberFb()     { return this.frmLoanInsert.controls['guaranteeNumber']; }
    get estimatedReturnDateFb() { return this.frmLoanInsert.controls['estimatedReturnDate']; }
    get observationsFb()        { return this.frmLoanInsert.controls['observations']; }

    constructor(
        private formBuilder: FormBuilder,
        private api: Api
    ) {
        this.frmLoanInsert = this.formBuilder.group({
            'searchBook':          ['', [Validators.required]],
            'searchStudent':       ['', [Validators.required]],
            'bookTitle':           [{value: '', disabled: true}],
            'bookCode':            [{value: '', disabled: true}],
            'bookAuthor':          [{value: '', disabled: true}],
            'bookCategory':        [{value: '', disabled: true}],
            'quantity':            [1, [Validators.required, Validators.min(1)]],
            
            'studentName':         [{value: '', disabled: true}],
            'studentCode':         [{value: '', disabled: true}],
            'studentDni':          [{value: '', disabled: true}],
            'studentSchool':       [{value: '', disabled: true}],
            'studentPhone':        [{value: '', disabled: true}],
            'studentEmail':        [{value: '', disabled: true}],
            
            'guaranteeType':       ['CarnetUniversitario', [Validators.required]],
            'guaranteeNumber':     ['', [Validators.required]],
            'estimatedReturnDate': ['', [Validators.required]],
            'observations':        ['']
        });
    }

    ngOnInit(): void {
        // Auto-select book if passed from navigation state (e.g. Catalog click)
        const state = window.history.state;
        if (state && state.book) {
            this.selectBook(state.book);
        }
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

    onBookSelect(event: any): void {
        const book = event.value;
        this.selectBook(book);
    }

    private selectBook(book: any): void {
        this.frmLoanInsert.patchValue({
            searchBook: book,
            bookTitle: book.title,
            bookCode: book.code,
            bookAuthor: book.author,
            bookCategory: book.categoryName || 'N/A'
        });
    }

    searchStudent(event: any): void {
        this.api.invoke(apistudentSearch, { q: event.query }).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listStudentSuggestions = (data.listStudent || [])
                .map((s: any) => ({
                    ...s,
                    displayName: `[${s.studentCode}] ${s.fullName}`
                }));
        }).catch(() => {
            this.listStudentSuggestions = [];
        });
    }

    onStudentSelect(event: any): void {
        const student = event.value;
        this.frmLoanInsert.patchValue({
            studentName: student.fullName,
            studentCode: student.studentCode,
            studentDni: student.dni,
            studentSchool: student.schoolName || 'N/A',
            studentPhone: student.phoneNumber || 'N/A',
            studentEmail: student.email
        });
        this.autoFillGuaranteeNumber();
    }

    onGuaranteeTypeChange(): void {
        this.autoFillGuaranteeNumber();
    }

    private autoFillGuaranteeNumber(): void {
        const type = this.guaranteeTypeFb.value;
        if (type === 'DNI') {
            this.frmLoanInsert.patchValue({
                guaranteeNumber: this.studentDniFb.value || ''
            });
        } else if (type === 'CarnetUniversitario') {
            this.frmLoanInsert.patchValue({
                guaranteeNumber: this.studentCodeFb.value || ''
            });
        }
    }

    clearFields(): void {
        this.frmLoanInsert.reset({
            quantity: 1,
            guaranteeType: 'CarnetUniversitario'
        });
        this.frmLoanInsert.markAsPristine();
        this.frmLoanInsert.markAsUntouched();
    }

    sendInsert(event: Event): void {
        if (!this.frmLoanInsert.valid) {
            this.frmLoanInsert.markAllAsTouched();
            this.frmLoanInsert.markAsDirty();
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Por favor complete todos los campos requeridos.' });
            return;
        }

        const selectedBook = this.searchBookFb.value;
        const selectedStudent = this.searchStudentFb.value;

        if (!selectedBook || !selectedBook.idBook) {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Seleccione un libro de la lista de sugerencias.' });
            return;
        }

        if (!selectedStudent || !selectedStudent.idUser) {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Seleccione un estudiante de la lista de sugerencias.' });
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
                
                // Format date as YYYY-MM-DD local time, avoiding timezone offsets
                const year = d.getFullYear();
                const month = String(d.getMonth() + 1).padStart(2, '0');
                const day = String(d.getDate()).padStart(2, '0');
                const dateStr = `${year}-${month}-${day}`;

                const bodyParams: ApiloanInsert$Params = {
                    body: {
                        idBook:              selectedBook.idBook,
                        idUser:              selectedStudent.idUser,
                        quantity:            this.quantityFb.value,
                        guaranteeType:       this.guaranteeTypeFb.value,
                        guaranteeNumber:     this.guaranteeNumberFb.value,
                        estimatedReturnDate: dateStr,
                        observations:        this.observationsFb.value
                    }
                };

                this.api.invoke(apiloanInsert, bodyParams).then((response: any) => {
                    const data = typeof response === 'string' ? JSON.parse(response) : response;
                    switch (data.type) {
                        case 'success':
                            this.messageService.add({ severity: 'success', summary: 'Correcto', detail: data.listMessage[0] });
                            this.clearFields();
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