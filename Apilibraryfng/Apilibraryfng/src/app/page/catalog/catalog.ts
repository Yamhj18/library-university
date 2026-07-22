import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { Dialog } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { FileUploadModule } from 'primeng/fileupload';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { Api } from '../../api/api';
import { AuthService } from '../../api/auth.service';
import { apibookGetall, apicategoryGetall, apibookUpdate, apibookDelete } from '../../api/functions';
import { environment } from '../../environments/environments';

@Component({
    selector: 'app-catalog',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        InputTextModule,
        ButtonModule,
        SelectModule,
        TagModule,
        CardModule,
        Dialog,
        InputNumberModule,
        FileUploadModule
    ],
    templateUrl: './catalog.html',
    styleUrl: './catalog.css'
})
export class Catalog implements OnInit {
    private confirmationService = inject(ConfirmationService);
    private messageService = inject(MessageService);
    private router = inject(Router);

    listBook: any[] = [];
    filteredBooks: any[] = [];
    listCategory: any[] = [];
    listCategoryForEdit: any[] = [];
    searchText: string = '';
    selectedCategory: any = null;
    urlBase: string = environment.urlBase;

    // Availability Filter
    selectedAvailability: any = { value: null, name: 'Cualquier Disponibilidad' };
    listAvailability: any[] = [
        { value: null, name: 'Cualquier Disponibilidad' },
        { value: 'Available', name: 'Disponible' },
        { value: 'Unavailable', name: 'No disponible' }
    ];

    // Book Edit Dialog State
    displayEditDialog: boolean = false;
    editingBook: any = null;
    editFormBook: any = {};
    selectedEditCategory: any = null;
    selectedEditImage: Blob | null = null;
    imageEditPreview: string | null = null;

    constructor(
        private api: Api, 
        public auth: AuthService
    ) {}

    ngOnInit(): void {
        this.loadCategories();
        this.loadBooks();
    }

    loadCategories(): void {
        this.api.invoke(apicategoryGetall).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listCategoryForEdit = data.listCategory || [];
            this.listCategory = [{ idCategory: null, name: 'Todas las categorías' }, ...this.listCategoryForEdit];
        });
    }

    loadBooks(): void {
        this.api.invoke(apibookGetall, {
            search: this.searchText || undefined,
            idCategory: this.selectedCategory?.idCategory || undefined
        }).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listBook = data.listBook || [];
            this.applyAvailabilityFilter();
        });
    }

    applyAvailabilityFilter(): void {
        if (this.selectedAvailability && this.selectedAvailability.value !== null) {
            this.filteredBooks = this.listBook.filter(
                (book) => book.status === this.selectedAvailability.value
            );
        } else {
            this.filteredBooks = this.listBook;
        }
    }

    getAvailableCount(): number {
        return this.listBook.reduce((acc, book) => acc + (book.stockAvailable || 0), 0);
    }

    getLoanedCount(): number {
        return this.listBook.reduce((acc, book) => acc + Math.max(0, (book.stockTotal || 0) - (book.stockAvailable || 0)), 0);
    }

    navigateToAddBook(): void {
        this.router.navigate(['/book/insert']);
    }

    onSearch(): void {
        this.loadBooks();
    }

    onCategoryChange(): void {
        this.loadBooks();
    }

    onAvailabilityChange(): void {
        this.applyAvailabilityFilter();
    }

    getImageUrl(book: any): string {
        return book.imageUrl ? `${this.urlBase}${book.imageUrl}` : 'https://placehold.co/200x280?text=No+Image';
    }

    // Register loan action
    registerLoan(book: any): void {
        this.router.navigate(['/loan/insert'], { state: { book } });
    }

    // Delete book action
    deleteBook(book: any, event: Event): void {
        event.stopPropagation();
        this.confirmationService.confirm({
            target: event.target as EventTarget,
            message: `¿Estás seguro de que deseas eliminar el libro "${book.title}"?`,
            header: 'Confirmar Eliminación',
            icon: 'pi pi-exclamation-triangle',
            rejectButtonProps: { label: 'Cancelar', severity: 'secondary', outlined: true },
            acceptButtonProps: { label: 'Eliminar', severity: 'danger' },
            accept: () => {
                this.api.invoke(apibookDelete, { idBook: book.idBook }).then(() => {
                    this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Libro eliminado exitosamente.' });
                    this.loadBooks();
                }).catch(() => {
                    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo eliminar el libro. Asegúrese de que no tenga préstamos activos.' });
                });
            }
        });
    }

    // Edit book actions
    openEditDialog(book: any, event: Event): void {
        event.stopPropagation();
        this.editingBook = book;
        this.editFormBook = {
            title: book.title,
            author: book.author,
            code: book.code,
            stockTotal: book.stockTotal || book.stock || 1,
            publicationYear: book.publicationYear,
            description: book.description,
            idCategory: book.idCategory
        };
        this.selectedEditCategory = this.listCategoryForEdit.find(c => c.idCategory === book.idCategory) || null;
        this.selectedEditImage = null;
        this.imageEditPreview = null;
        this.displayEditDialog = true;
    }

    onEditImageSelect(event: any): void {
        const file: File = event.currentFiles ? event.currentFiles[0] : event.files[0];
        this.selectedEditImage = file;

        const reader = new FileReader();
        reader.onload = (e: any) => {
            this.imageEditPreview = e.target.result;
        };
        reader.readAsDataURL(file);
    }

    saveBookUpdate(): void {
        if (!this.editFormBook.title || !this.editFormBook.author || !this.editFormBook.code || !this.selectedEditCategory) {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Por favor complete todos los campos obligatorios.' });
            return;
        }

        const formData = new FormData();
        formData.append('code', this.editFormBook.code);
        formData.append('title', this.editFormBook.title);
        formData.append('author', this.editFormBook.author);
        formData.append('description', this.editFormBook.description || '');
        formData.append('publicationYear', String(this.editFormBook.publicationYear || ''));
        formData.append('stockTotal', String(this.editFormBook.stockTotal));
        formData.append('idCategory', this.selectedEditCategory.idCategory);
        if (this.selectedEditImage) {
            formData.append('image', this.selectedEditImage);
        }

        this.api.invoke(apibookUpdate, {
            idBook: this.editingBook.idBook,
            body: formData
        }).then(() => {
            this.messageService.add({ severity: 'success', summary: 'Éxito', detail: 'Libro actualizado exitosamente.' });
            this.displayEditDialog = false;
            this.loadBooks();
        }).catch((err) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'No se pudo actualizar el libro.' });
        });
    }
}
