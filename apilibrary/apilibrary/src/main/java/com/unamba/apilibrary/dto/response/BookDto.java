package com.unamba.apilibrary.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private String idBook;
    private String code;
    private String title;
    private String author;
    private String description;
    private Integer publicationYear;
    private Integer stockTotal;
    private Integer stockAvailable;
    private String status;
    private String idCategory;
    private String imageUrl;
    private String categoryName;
}
