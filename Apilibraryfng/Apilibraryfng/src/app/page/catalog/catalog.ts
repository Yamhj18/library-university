import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TagModule } from 'primeng/tag';
import { CardModule } from 'primeng/card';
import { Api } from '../../api/api';
import { apibookGetall, apicategoryGetall } from '../../api/functions';

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
        CardModule
    ],
    templateUrl: './catalog.html',
    styleUrl: './catalog.css'
})
export class Catalog implements OnInit {
    listBook: any[] = [];
    listCategory: any[] = [];
    searchText: string = '';
    selectedCategory: any = null;
    urlBase: string = 'http://localhost:8080';

    constructor(private api: Api) {}

    ngOnInit(): void {
        this.loadCategories();
        this.loadBooks();
    }

    loadCategories(): void {
        this.api.invoke(apicategoryGetall).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listCategory = [{ idCategory: null, name: 'All categories' }, ...data.listCategory];
        });
    }

    loadBooks(): void {
        this.api.invoke(apibookGetall, {
            search: this.searchText || undefined,
            idCategory: this.selectedCategory?.idCategory || undefined
        }).then((response: any) => {
            const data = typeof response === 'string' ? JSON.parse(response) : response;
            this.listBook = data.listBook;
        });
    }

    onSearch(): void {
        this.loadBooks();
    }

    onCategoryChange(): void {
        this.loadBooks();
    }

    getStatusSeverity(status: string): 'success' | 'danger' {
    return status === 'Available' ? 'success' : 'danger';
    }

    getImageUrl(book: any): string {
        return book.imageUrl ? `${this.urlBase}${book.imageUrl}` : 'https://placehold.co/200x280?text=No+Image';
    }
}