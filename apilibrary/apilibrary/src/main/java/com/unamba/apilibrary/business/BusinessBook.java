package com.unamba.apilibrary.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.unamba.apilibrary.dto.request.RequestBookInsert;
import com.unamba.apilibrary.dto.response.ResponseBookGetAll;
import com.unamba.apilibrary.dto.response.ResponseBookInsert;
import com.unamba.apilibrary.entity.EntityBook;
import com.unamba.apilibrary.helper.GenericHelper;
import com.unamba.apilibrary.repository.RepositoryBook;
import com.unamba.apilibrary.repository.RepositoryCategory;
import com.unamba.apilibrary.staticdata.EnumBookStatus;

@Service
public class BusinessBook {
    private final RepositoryBook repositoryBook;
    private final RepositoryCategory repositoryCategory;

    public BusinessBook(
            RepositoryBook repositoryBook,
            RepositoryCategory repositoryCategory) {
        this.repositoryBook = repositoryBook;
        this.repositoryCategory = repositoryCategory;
    }

    public ResponseBookGetAll getAll(String search, String idCategory) {
        ResponseBookGetAll response = new ResponseBookGetAll();

        String searchParam = (search == null || search.isBlank()) ? null : search;
        String categoryParam = (idCategory == null || idCategory.isBlank()) ? null : idCategory;

        List<EntityBook> list = repositoryBook.findBySearchAndCategory(searchParam, categoryParam);

        list.forEach(b -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idBook", b.getIdBook());
            map.put("code", b.getCode());
            map.put("title", b.getTitle());
            map.put("author", b.getAuthor());
            map.put("stock", b.getStock());
            map.put("status", b.getStatus());
            map.put("idCategory", b.getIdCategory());
            map.put("imageUrl", (b.getImageName() != null)
                    ? "/bookimage/" + b.getImageName() + "." + b.getImageExtension()
                    : null);

            if (b.getParentCategory() != null) {
                map.put("categoryName", b.getParentCategory().getName());
            }

            response.getListBook().add(map);
        });

        response.success();
        return response;
    }

    public ResponseBookInsert insert(RequestBookInsert request) throws IOException {
        ResponseBookInsert response = new ResponseBookInsert();

        EntityBook entity = new EntityBook();
        entity.setIdBook(UUID.randomUUID().toString());
        entity.setIdCategory(request.getIdCategory());
        entity.setCode(request.getCode());
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        entity.setStock(request.getStock());
        entity.setStatus(EnumBookStatus.AVAILABLE.toString());
        entity.setCreatedAt(new java.sql.Date(new Date().getTime()));
        entity.setUpdatedAt(entity.getCreatedAt());

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            Path basePath = Paths.get("storage/bookimage");

            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }

            String imageId = UUID.randomUUID().toString();
            String originalName = request.getImage().getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();

            Files.copy(request.getImage().getInputStream(),
                    basePath.resolve(imageId + "." + extension));

            entity.setImageName(imageId);
            entity.setImageExtension(extension);
        }

        repositoryBook.save(entity);

        response.success();
        response.listMessage.add("Book registered successfully.");
        return response;
    }
}