package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateCategoryRequest;
import org.gad.inventory_service.dto.request.UpdateCategoryRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.CategoryService;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.gad.inventory_service.utils.Constants.*;

@Validated
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findAllCategories() {
        return categoryService.findAllCategories()
                .collectList()
                .map(categories -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_CATEGORIES_OK)
                                .data(categories)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/name")
    public Mono<ResponseEntity<DataResponse>> findCategoryByName(@RequestParam @NotBlank(message = MESSAGE_NAME_CANNOT_BE_EMPTY) String name) {
        return categoryService.findCategoryByName(name)
                .map(category -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_CATEGORY_OK)
                                .data(category)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> findCategoryByUuid(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return categoryService.findByUuid(uuid)
                .map(category -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_CATEGORY_OK)
                                .data(category)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        return categoryService.saveCategory(createCategoryRequest)
                .map(category -> {
                    URI location = UtilsMethods.createUri(CATEGORY_URI, category.uuidCategory());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_CATEGORY_CREATED)
                            .data(category)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{uuid}")
    public Mono<ResponseEntity<DataResponse>> updateCategory(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid,
                                                             @RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        return categoryService.updateCategory(uuid, updateCategoryRequest)
                .map(category -> {
                    URI location = UtilsMethods.createUri(CATEGORY_URI, category.uuidCategory());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message(MESSAGE_CATEGORY_UPDATED)
                            .data(category)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{uuid}")
    public Mono<ResponseEntity<Void>> deleteCategory(@PathVariable @Pattern(regexp = REGEX_UUID, message = MESSAGE_INCORRECT_UUID_FORMAT) String uuid) {
        return categoryService.deleteCategoryByUuid(uuid)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
