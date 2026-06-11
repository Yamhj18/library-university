/* eslint-disable */

export interface Category {
    idCategory: string;
    name: string;
}

export interface Book {
    idBook: string;
    idCategory: string;
    code: string;
    title: string;
    author: string;
    stock: number;
    status: string;
    imageUrl?: string;
    categoryName?: string;
}

export interface Loan {
    idLoan: string;
    idBook: string;
    code: string;
    studentCode: string;
    studentName: string;
    faculty: string;
    phoneNumber: string;
    guaranteeType: string;
    guaranteeNumber: string;
    loanDate: string;
    estimatedReturnDate: string;
    status: string;
    bookTitle?: string;
    bookCode?: string;
    bookAuthor?: string;
}