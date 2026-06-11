import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import { FileUploadModule } from 'primeng/fileupload';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Api } from '../../../api/api';
import { apicategoryGetall, apibookInsert, ApibookInsert$Params } from '../../../api/functions';

@Component({
    selector: 'app-book-insert',
    standalone: true,
    imports: [
        FormsModule,
        ReactiveFormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        InputNumberModule,
        FileUploadModule
    ],
    templateUrl: './book-insert.html',
    styleUrl: './book-insert.css'
})
export class BookInsert implements OnInit {
    private confirmationService = inject(ConfirmationService);
    private messageService = inject(MessageService);

    frmBookInsert: FormGroup;
    listCategory: any[] = [];
    selectedImage: Blob | null = null;
    imagePreview: string | null = null;

    get categoryFb() { return this.frmBookInsert.controls['category']; }
    get codeFb()     { return this.frmBookInsert.controls['code']; }
    get titleFb()    { return this.frmBookInsert.controls['title']; }
    get authorFb()   { return this.frmBookInsert.controls['author']; }
    get stockFb()    { return this.frmBookInsert.controls['stock']; }

    constructor(
        private formBuilder: FormBuilder,
        private api: Api
    ) {
        this.frmBookInsert = this.formBuilder.group({
            'category': ['', [Validators.required]],
            'code':     ['', [Validators.required]],
            'title':    ['', [Validators.required]],
            'author':   ['', [Validators.required]],
            'stock':    [null, [Validators.required, Validators.min(1)]]
        });
    }

    ngOnInit(): void {
        this.loadCategories();
    }

    private loadCategories(): void {
        this.api.invoke(apicategoryGetall).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listCategory = data.listCategory;
        });
    }

    onImageSelect(event: any): void {
        const file: File = event.currentFiles ? event.currentFiles[0] : event.files[0];
        this.selectedImage = file;

        const reader = new FileReader();
        reader.onload = (e: any) => {
            this.imagePreview = e.target.result;
        };
        reader.readAsDataURL(file);
    }

    sendInsert(event: Event): void {
        if (!this.frmBookInsert.valid) {
            this.frmBookInsert.markAllAsTouched();
            this.frmBookInsert.markAsDirty();
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please complete all required fields.' });
            return;
        }

        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: 'Confirm book registration?',
            header: 'Confirmation',
            icon: 'pi pi-info-circle',
            rejectButtonProps: { label: 'Cancel', severity: 'secondary', outlined: true },
            acceptButtonProps: { label: 'Accept', severity: 'primary' },
            accept: () => {
                const bodyParams: ApibookInsert$Params = {
                    body: {
                        idCategory: this.categoryFb.value.idCategory,
                        code:       this.codeFb.value,
                        title:      this.titleFb.value,
                        author:     this.authorFb.value,
                        stock:      this.stockFb.value,
                        image:      this.selectedImage ?? undefined
                    }
                };

                this.api.invoke(apibookInsert, bodyParams).then((response: any) => {
                    const data = typeof response === 'string' ? JSON.parse(response) : response;
                    switch (data.type) {
                        case 'success':
                            this.messageService.add({ severity: 'success', summary: 'Success', detail: data.listMessage[0] });
                            this.frmBookInsert.reset();
                            this.selectedImage = null;
                            this.imagePreview = null;
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
}