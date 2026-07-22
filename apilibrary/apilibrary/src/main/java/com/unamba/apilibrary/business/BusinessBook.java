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
import org.springframework.transaction.annotation.Transactional;

import com.unamba.apilibrary.dto.request.RequestBookInsert;
import com.unamba.apilibrary.dto.request.RequestBookUpdate;
import com.unamba.apilibrary.dto.response.ResponseBookGetAll;
import com.unamba.apilibrary.dto.response.ResponseBookInsert;
import com.unamba.apilibrary.dto.response.ResponseGenericMessage;
import com.unamba.apilibrary.dto.response.BookDto;
import com.unamba.apilibrary.entity.EntityBook;
import com.unamba.apilibrary.repository.RepositoryBook;
import com.unamba.apilibrary.staticdata.EnumBookStatus;

@Service
@Transactional
public class BusinessBook {
    private final RepositoryBook repositoryBook;

    public BusinessBook(RepositoryBook repositoryBook) {
        this.repositoryBook = repositoryBook;
    }

    public ResponseBookGetAll getAll(String search, String idCategory) {
        ResponseBookGetAll response = new ResponseBookGetAll();

        String searchParam = (search == null || search.isBlank()) ? null : search;
        String categoryParam = (idCategory == null || idCategory.isBlank()) ? null : idCategory;

        List<EntityBook> list = repositoryBook.findBySearchAndCategory(searchParam, categoryParam);

        list.forEach(b -> {
            response.getListBook().add(mapBook(b));
        });

        response.success();
        return response;
    }

    public ResponseBookGetAll getById(String idBook) {
        ResponseBookGetAll response = new ResponseBookGetAll();

        EntityBook book = repositoryBook.findById(idBook).orElse(null);
        if (book == null) {
            response.listMessage.add("Libro no encontrado.");
            return response;
        }

        response.getListBook().add(mapBook(book));
        response.success();
        return response;
    }

    public ResponseBookInsert insert(RequestBookInsert request) throws IOException {
        ResponseBookInsert response = new ResponseBookInsert();

        if (repositoryBook.existsByCode(request.getCode())) {
            response.listMessage.add("El código del libro ya existe.");
            return response;
        }

        EntityBook entity = new EntityBook();
        entity.setIdBook(UUID.randomUUID().toString());
        entity.setIdCategory(request.getIdCategory());
        entity.setCode(request.getCode());
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        entity.setDescription(request.getDescription());
        entity.setPublicationYear(request.getPublicationYear());
        entity.setStockTotal(request.getStockTotal());
        entity.setStockAvailable(request.getStockTotal()); // Inicialmente todos están disponibles
        entity.setStatus(EnumBookStatus.AVAILABLE.toString());
        entity.setCreatedAt(new java.sql.Date(new Date().getTime()));
        entity.setUpdatedAt(entity.getCreatedAt());

        saveBookImage(entity, request.getImage());

        repositoryBook.save(entity);

        response.success();
        response.listMessage.add("Libro registrado exitosamente.");
        return response;
    }

    public ResponseGenericMessage update(String idBook, RequestBookUpdate request) throws IOException {
        ResponseGenericMessage response = new ResponseGenericMessage();

        EntityBook entity = repositoryBook.findById(idBook).orElse(null);
        if (entity == null) {
            response.listMessage.add("Libro no encontrado.");
            return response;
        }

        // Check unique code (excluding current book)
        if (repositoryBook.existsByCodeAndIdBookNot(request.getCode(), idBook)) {
            response.listMessage.add("El código del libro ya existe en otro registro.");
            return response;
        }

        // Calculate new stock available
        int currentLent = entity.getStockTotal() - entity.getStockAvailable();
        int newStockTotal = request.getStockTotal();
        int newStockAvailable = newStockTotal - currentLent;

        if (newStockAvailable < 0) {
            response.listMessage.add("El nuevo stock total no puede ser menor a los libros actualmente prestados (" + currentLent + ").");
            return response;
        }

        entity.setIdCategory(request.getIdCategory());
        entity.setCode(request.getCode());
        entity.setTitle(request.getTitle());
        entity.setAuthor(request.getAuthor());
        entity.setDescription(request.getDescription());
        entity.setPublicationYear(request.getPublicationYear());
        entity.setStockTotal(newStockTotal);
        entity.setStockAvailable(newStockAvailable);
        entity.setStatus(newStockAvailable > 0
                ? EnumBookStatus.AVAILABLE.toString()
                : EnumBookStatus.UNAVAILABLE.toString());
        entity.setUpdatedAt(new java.sql.Date(new Date().getTime()));

        // Update image only if a new one is provided
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            saveBookImage(entity, request.getImage());
        }

        repositoryBook.save(entity);

        response.success();
        response.listMessage.add("Libro actualizado exitosamente.");
        return response;
    }

    public ResponseGenericMessage delete(String idBook) {
        ResponseGenericMessage response = new ResponseGenericMessage();

        EntityBook entity = repositoryBook.findById(idBook).orElse(null);
        if (entity == null) {
            response.listMessage.add("Libro no encontrado.");
            return response;
        }

        int currentLent = entity.getStockTotal() - entity.getStockAvailable();
        if (currentLent > 0) {
            response.listMessage.add("No se puede eliminar un libro con préstamos activos (" + currentLent + " ejemplares prestados).");
            return response;
        }

        entity.setStatus(EnumBookStatus.UNAVAILABLE.toString());
        entity.setStockAvailable(0);
        entity.setUpdatedAt(new java.sql.Date(new Date().getTime()));
        repositoryBook.save(entity);

        response.success();
        response.listMessage.add("Libro eliminado exitosamente.");
        return response;
    }

    private void saveBookImage(EntityBook entity, org.springframework.web.multipart.MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            Path basePath = Paths.get("storage/bookimage");

            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }

            String imageId = UUID.randomUUID().toString();
            String originalName = image.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();

            Files.copy(image.getInputStream(),
                    basePath.resolve(imageId + "." + extension));

            entity.setImageName(imageId);
            entity.setImageExtension(extension);
        }
    }

    private BookDto mapBook(EntityBook b) {
        BookDto dto = new BookDto();
        dto.setIdBook(b.getIdBook());
        dto.setCode(b.getCode());
        dto.setTitle(b.getTitle());
        dto.setAuthor(b.getAuthor());
        dto.setDescription(b.getDescription());
        dto.setPublicationYear(b.getPublicationYear());
        dto.setStockTotal(b.getStockTotal());
        dto.setStockAvailable(b.getStockAvailable());
        dto.setStatus(b.getStatus());
        dto.setIdCategory(b.getIdCategory());
        dto.setImageUrl((b.getImageName() != null)
                ? "/bookimage/" + b.getImageName() + "." + b.getImageExtension()
                : null);

        if (b.getParentCategory() != null) {
            dto.setCategoryName(b.getParentCategory().getName());
        }

        return dto;
    }
}