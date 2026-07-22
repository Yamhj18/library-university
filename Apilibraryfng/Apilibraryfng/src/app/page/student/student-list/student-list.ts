import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Api } from '../../../api/api';
import { apistudentGetall, apischoolGetall, apistudentDelete } from '../../../api/functions';

@Component({
    selector: 'app-student-list',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        TableModule,
        TagModule
    ],
    templateUrl: './student-list.html',
    styleUrl: './student-list.css'
})
export class StudentList implements OnInit {
    listStudent: any[] = [];
    listSchool: any[] = [];
    searchText: string = '';
    selectedSchool: any = null;
    loading: boolean = false;

    constructor(
        private api: Api,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) {}

    ngOnInit(): void {
        this.loadSchools();
        this.loadStudents();
    }

    loadSchools(): void {
        this.api.invoke(apischoolGetall).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listSchool = [{ idSchool: null, name: 'Todas las escuelas' }, ...(data.listSchool || [])];
        });
    }

    loadStudents(): void {
        this.loading = true;
        this.api.invoke(apistudentGetall, {
            search: this.searchText || undefined,
            idSchool: this.selectedSchool?.idSchool || undefined
        }).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listStudent = data.listStudent || [];
        }).finally(() => {
            this.loading = false;
        });
    }

    onSearch(): void {
        this.loadStudents();
    }

    onSchoolChange(): void {
        this.loadStudents();
    }

    deleteStudent(event: Event, idStudent: string): void {
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: '¿Está seguro de eliminar a este estudiante?',
            header: 'Confirmación',
            icon: 'pi pi-exclamation-triangle',
            acceptButtonProps: { label: 'Eliminar', severity: 'danger' },
            rejectButtonProps: { label: 'Cancelar', outlined: true, severity: 'secondary' },
            accept: () => {
                this.api.invoke(apistudentDelete, { idStudent }).then((response: any) => {
                    const data = typeof response === 'string' ? JSON.parse(response) : response;
                    if (data.type === 'success') {
                        this.messageService.add({ severity: 'success', summary: 'Éxito', detail: data.listMessage[0] });
                        this.loadStudents();
                    } else {
                        this.messageService.add({ severity: 'error', summary: 'Error', detail: data.listMessage[0] });
                    }
                }).catch(() => {
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar el estudiante.' });
                });
            }
        });
    }
}
