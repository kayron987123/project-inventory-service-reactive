package org.gad.inventory_service.service.impl;

import org.gad.inventory_service.dto.request.CreateProductRequest;
import org.gad.inventory_service.dto.request.UpdateProductRequest;
import org.gad.inventory_service.exception.BrandNotFoundException;
import org.gad.inventory_service.exception.CategoryNotFoundException;
import org.gad.inventory_service.exception.ProductNotFoundException;
import org.gad.inventory_service.exception.ProviderNotFoundException;
import org.gad.inventory_service.model.Brand;
import org.gad.inventory_service.model.Category;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.model.Provider;
import org.gad.inventory_service.repository.BrandRepository;
import org.gad.inventory_service.repository.CategoryRepository;
import org.gad.inventory_service.repository.ProductRepository;
import org.gad.inventory_service.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.gad.inventory_service.utils.Constants.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    Product product;
    Category category;
    Brand brand;
    Provider provider;
    CreateProductRequest createProductRequest;
    UpdateProductRequest updateProductRequest;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .idCategory("cat123")
                .name("Electronics")
                .build();

        brand = Brand.builder()
                .idBrand("brand123")
                .name("TechBrand")
                .build();

        provider = Provider.builder()
                .idProvider("prov123")
                .name("TechProvider")
                .ruc("12345678901")
                .dni("12345678")
                .address("123 Tech Street")
                .phone("123456789")
                .email("tet@gmail.com")
                .build();

        product = Product.builder()
                .idProduct("123456")
                .name("Laptop")
                .description("High performance laptop")
                .price(new BigDecimal("1200.00"))
                .categoryId("cat123")
                .brandId("brand123")
                .providerId("prov123")
                .build();
        createProductRequest = CreateProductRequest.builder()
                .name("Laptop")
                .description("High performance laptop")
                .price(new BigDecimal("1200.00"))
                .categoryName("cat123")
                .brandName("brand123")
                .providerName("prov123")
                .build();
        updateProductRequest = UpdateProductRequest.builder()
                .name("Updated Laptop")
                .description("Updated description")
                .price(new BigDecimal("1300.00"))
                .categoryName("cat123")
                .brandName("brand123")
                .providerName("prov123")
                .build();
    }

    @Test
    void findAllProducts_ShouldReturnFluxOfProductDTO_WhenProductsExist() {
        when(productRepository.findAll()).thenReturn(Flux.just(product));

        StepVerifier.create(productService.findAllProducts())
                .expectNextMatches(dto -> dto.idProduct().equals("123456") &&
                        dto.name().equals("Laptop") &&
                        dto.description().equals("High performance laptop") &&
                        dto.price().compareTo(new BigDecimal("1200.00")) == 0)
                .verifyComplete();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findAllProducts_ShouldThrow_WhenNoProductsExist() {
        when(productRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(productService.findAllProducts())
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_FLUX))
                .verify();

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findProductById_ShouldReturnProductDTO_WhenProductExists() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));

        StepVerifier.create(productService.findProductById("123456"))
                .expectNextMatches(dto -> dto.idProduct().equals("123456") &&
                        dto.name().equals("Laptop") &&
                        dto.description().equals("High performance laptop") &&
                        dto.price().compareTo(new BigDecimal("1200.00")) == 0)
                .verifyComplete();
        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void findProductById_ShouldThrow_WhenProductDoesNotExist() {
        when(productRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productService.findProductById("non-existent-id"))
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + "non-existent-id"))
                .verify();
        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void findProductsByCriteria_ShouldReturnFluxOfProductDTO_WhenCriteriaMatch() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(provider));
        when(productRepository.findByCriteria(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Flux.just(product));

        StepVerifier.create(productService.findProductsByCriteria("Electronics", "TechBrand", "TechProvider", "Laptop"))
                .expectNextMatches(dto -> dto.idProduct().equals("123456") &&
                        dto.name().equals("Laptop") &&
                        dto.description().equals("High performance laptop") &&
                        dto.price().compareTo(new BigDecimal("1200.00")) == 0)
                .verifyComplete();
        verify(categoryRepository, times(1)).findCategoryByNameContainingIgnoreCase(anyString());
    }

    @Test
    void findProductsByCriteria_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(productService.findProductsByCriteria("Laptop", "NonExistentCategory", "TechBrand", "TechProvider"))
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_NAME + "NonExistentCategory"))
                .verify();
        verify(productRepository, times(0)).findByCriteria(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void findProductsByCriteria_ShouldThrow_WhenBrandDoesNotExist() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(provider));

        StepVerifier.create(productService.findProductsByCriteria("Laptop", "Electronics", "NonExistentBrand", "TechProvider"))
                .expectErrorMatches(throwable -> throwable instanceof BrandNotFoundException &&
                        throwable.getMessage().equals(BRAND_NOT_FOUND_NAME + "NonExistentBrand"))
                .verify();
        verify(productRepository, times(0)).findByCriteria(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void findProductsByCriteria_ShouldThrow_WhenProviderDoesNotExist() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productService.findProductsByCriteria("Laptop", "Electronics", "TechBrand", "NonExistentProvider"))
                .expectErrorMatches(throwable -> throwable instanceof ProviderNotFoundException &&
                        throwable.getMessage().equals(PROVIDER_NOT_FOUND_NAME + "NonExistentProvider"))
                .verify();
        verify(productRepository, times(0)).findByCriteria(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void findProductsByCriteria_ShouldThrow_WhenNoProductsMatchCriteria() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(provider));
        when(productRepository.findByCriteria(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Flux.empty());

        StepVerifier.create(productService.findProductsByCriteria("NonExistent", "NonExistent", "NonExistent", "NonExistent"))
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_FLUX_CRITERIA))
                .verify();
        verify(productRepository, times(1)).findByCriteria(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void createProduct_ShouldReturnProductDTO_WhenProductIsCreated() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(provider));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.createProduct(createProductRequest))
                .expectNextMatches(dto -> dto.idProduct().equals("123456") &&
                        dto.name().equals("Laptop") &&
                        dto.description().equals("High performance laptop") &&
                        dto.price().compareTo(new BigDecimal("1200.00")) == 0)
                .verifyComplete();
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrow_WhenCategoryDoesNotExist() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.error(new CategoryNotFoundException(CATEGORY_NOT_FOUND_NAME + "cat123")));

        StepVerifier.create(productService.createProduct(createProductRequest))
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_NAME + "cat123"))
                .verify();
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrow_WhenBrandDoesNotExist() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.error(new BrandNotFoundException(BRAND_NOT_FOUND_NAME + "brand123")));

        StepVerifier.create(productService.createProduct(createProductRequest))
                .expectErrorMatches(throwable -> throwable instanceof BrandNotFoundException &&
                        throwable.getMessage().equals(BRAND_NOT_FOUND_NAME + "brand123"))
                .verify();
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void createProduct_ShouldThrow_WhenProviderDoesNotExist() {
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productService.createProduct(createProductRequest))
                .expectErrorMatches(throwable -> throwable instanceof ProviderNotFoundException &&
                        throwable.getMessage().equals(PROVIDER_NOT_FOUND_NAME + "prov123"))
                .verify();
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldReturnProductDTO_WhenProductIsUpdated() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(provider));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productService.updateProduct("123456", updateProductRequest))
                .expectNextMatches(dto -> dto.idProduct().equals("123456") &&
                        dto.name().equals("Updated Laptop") &&
                        dto.description().equals("Updated description") &&
                        dto.price().compareTo(new BigDecimal("1300.00")) == 0)
                .verifyComplete();
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrow_WhenProductDoesNotExist() {
        when(productRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProduct("non-existent-id", updateProductRequest))
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + "non-existent-id"))
                .verify();
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrow_WhenCategoryDoesNotExist() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProduct("123456", updateProductRequest))
                .expectErrorMatches(throwable -> throwable instanceof CategoryNotFoundException &&
                        throwable.getMessage().equals(CATEGORY_NOT_FOUND_NAME + "cat123"))
                .verify();
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrow_WhenBrandDoesNotExist() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProduct("123456", updateProductRequest))
                .expectErrorMatches(throwable -> throwable instanceof BrandNotFoundException &&
                        throwable.getMessage().equals(BRAND_NOT_FOUND_NAME + "brand123"))
                .verify();
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrow_WhenProviderDoesNotExist() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(categoryRepository.findCategoryByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(category));
        when(brandRepository.findBrandByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.just(brand));
        when(providerRepository.findProviderByNameContainingIgnoreCase(anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(productService.updateProduct("123456", updateProductRequest))
                .expectErrorMatches(throwable -> throwable instanceof ProviderNotFoundException &&
                        throwable.getMessage().equals(PROVIDER_NOT_FOUND_NAME + "prov123"))
                .verify();
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void deleteProductById_ShouldReturnMonoVoid_WhenProductIsDeleted() {
        when(productRepository.findById(anyString())).thenReturn(Mono.just(product));
        when(productRepository.deleteById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProductById("123456"))
                .verifyComplete();
        verify(productRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteProductById_ShouldThrow_WhenProductDoesNotExist() {
        when(productRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProductById("non-existent-id"))
                .expectErrorMatches(throwable -> throwable instanceof ProductNotFoundException &&
                        throwable.getMessage().equals(PRODUCT_NOT_FOUND_ID + "non-existent-id"))
                .verify();
        verify(productRepository, times(0)).deleteById(anyString());
    }
}