import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AuthService } from '../../../api/auth.service';
import { Api } from '../../../api/api';
import { apiloanMyloans } from '../../../api/functions';

@Component({
    selector: 'app-student-profile',
    standalone: true,
    imports: [CommonModule, TableModule, TagModule],
    templateUrl: './student-profile.html',
    styleUrl: './student-profile.css'
})
export class StudentProfile implements OnInit {
    private authService = inject(AuthService);
    private api = inject(Api);

    studentName: string = '';
    listMyLoans: any[] = [];
    listMyOverdue: any[] = [];

    ngOnInit(): void {
        this.studentName = this.authService.getName();
        this.loadMyLoans();
    }

    loadMyLoans(): void {
        this.api.invoke(apiloanMyloans).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            if (data.type === 'success') {
                this.listMyLoans = data.listLoan || [];
                this.listMyOverdue = data.listOverdue || [];
            }
        });
    }

    isOverdue(idLoan: string): boolean {
        return this.listMyOverdue.some(o => o.idLoan === idLoan);
    }
}
