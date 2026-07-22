import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { TableModule } from 'primeng/table';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Api } from '../../../api/api';
import { apiloanGetall, apiloanReturn } from '../../../api/functions';

@Component({
    selector: 'app-loan-list',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        InputTextModule,
        ButtonModule,
        TagModule,
        TableModule
    ],
    templateUrl: './loan-list.html',
    styleUrl: './loan-list.css'
})
export class LoanList implements OnInit {
    private confirmationService = inject(ConfirmationService);
    private messageService = inject(MessageService);

    listLoan: any[] = [];
    listOverdue: any[] = [];
    searchText: string = '';
    loading: boolean = false;

    constructor(private api: Api) {}

    ngOnInit(): void {
        this.loadLoans();
    }

    loadLoans(): void {
        this.loading = true;
        this.api.invoke(apiloanGetall, {
            search: this.searchText || undefined
        }).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listLoan = data.listLoan;
            this.listOverdue = data.listOverdue;
        }).finally(() => {
            this.loading = false;
        });
    }

    onSearch(): void {
        this.loadLoans();
    }

    confirmReturn(event: Event, idLoan: string): void {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: 'Confirm book return?',
            header: 'Confirmation',
            icon: 'pi pi-info-circle',
            rejectButtonProps: { label: 'Cancel', severity: 'secondary', outlined: true },
            acceptButtonProps: { label: 'Accept', severity: 'primary' },
            accept: () => {
                this.api.invoke(apiloanReturn, { idLoan }).then((response: any) => {
                    const data = typeof response === 'string' ? JSON.parse(response) : response;
                    switch (data.type) {
                        case 'success':
                            this.messageService.add({ severity: 'success', summary: 'Success', detail: data.listMessage[0] });
                            this.loadLoans();
                            break;
                        case 'error':
                            this.messageService.add({ severity: 'error', summary: 'Error', detail: data.listMessage[0] });
                            break;
                    }
                }).catch(() => {
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Something went wrong.' });
                });
            },
            reject: () => {}
        });
    }

    isOverdue(loan: any): boolean {
        return this.listOverdue.some(o => o.idLoan === loan.idLoan);
    }
}