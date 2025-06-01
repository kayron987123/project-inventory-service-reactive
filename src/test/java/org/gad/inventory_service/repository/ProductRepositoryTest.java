package org.gad.inventory_service.repository;

import org.gad.inventory_service.model.Brand;
import org.gad.inventory_service.model.Category;
import org.gad.inventory_service.model.Product;
import org.gad.inventory_service.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class ProductRepositoryTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProviderRepository providerRepository;
    private String categoryId;
    private String brandId;
    private String providerId;

    @Test
    void mongoDBContainerIsRunning() {
        StepVerifier.create(Mono.just(mongoDBContainer.isRunning()))
                .expectNext(true)
                .verifyComplete();
    }

    @BeforeEach
    void setUp() {
        productRepository.deleteAll().block();
        categoryRepository.deleteAll().block();
        brandRepository.deleteAll().block();
        providerRepository.deleteAll().block();

        Category category = Category.builder()
                .name("Electronics")
                .build();

        Brand brand = Brand.builder()
                .name("Samsung")
                .build();

        Provider provider = Provider.builder()
                .name("TechProvider")
                .ruc("12345678901")
                .dni("12345678")
                .address("123 Tech Street")
                .phone("123456789")
                .email("tech@mail.com")
                .build();

        categoryId = Objects.requireNonNull(categoryRepository.save(category).block()).getIdCategory();
        brandId = Objects.requireNonNull(brandRepository.save(brand).block()).getIdBrand();
        providerId = Objects.requireNonNull(providerRepository.save(provider).block()).getIdProvider();

        createTestProduct("Samsung Galaxy S21", categoryId, brandId, providerId, new BigDecimal("699.99"));
        createTestProduct("Samsung TV 55", categoryId, brandId, providerId, new BigDecimal("899.99"));
        createTestProduct("iPhone 13", categoryId, brandId, providerId, new BigDecimal("999.99"));
    }

    @Test
    void findProductByNameContainingIgnoreCase_ShouldReturnProduct_WhenNameMatches() {
        Mono<Product> productResult = productRepository.findProductByNameContainingIgnoreCase("iPhone");

        StepVerifier.create(productResult)
                .assertNext(product -> {
                    assertEquals("iPhone 13", product.getName());
                    assertNotNull(product.getIdProduct());
                    assertNotNull(product.getCategoryId());
                    assertNotNull(product.getBrandId());
                    assertNotNull(product.getProviderId());
                    assertEquals(0, product.getPrice().compareTo(new BigDecimal("999.99")));
                })
                .verifyComplete();
    }

    @Test
    void findBrandByNameContainingIgnoreCase_ShouldReturnEmpty_WhenNameDoesNotMatches() {
        Mono<Product> productResult = productRepository.findProductByNameContainingIgnoreCase("NonExistentProduct");

        StepVerifier.create(productResult)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findByCriteria_ShouldReturnProducts_WhenCriteriaMatches() {
        Flux<Product> productsResult = productRepository.findByCriteria("Samsung", categoryId, brandId, providerId);
        System.out.println(Objects.requireNonNull(productsResult.collectList().block()));

        StepVerifier.create(productsResult)
                .assertNext(product -> {
                    assertEquals("Samsung Galaxy S21", product.getName());
                    assertEquals(0, product.getPrice().compareTo(new BigDecimal("699.99")));
                })
                .assertNext(product -> {
                    assertEquals("Samsung TV 55", product.getName());
                    assertEquals(0, product.getPrice().compareTo(new BigDecimal("899.99")));
                })
                .verifyComplete();
    }

    @Test
    void findByCriteria_ShouldReturnEmpty_WhenNoCriteriaMatches() {
        Flux<Product> productsResult = productRepository.findByCriteria("NonExistent", categoryId, brandId, providerId);

        StepVerifier.create(productsResult)
                .expectNextCount(0)
                .verifyComplete();
    }

    private void createTestProduct(String name, String categoryId, String brandId, String providerId, BigDecimal price) {
        Product product = Product.builder()
                .name(name)
                .description("Test product description")
                .price(price)
                .categoryId(categoryId)
                .brandId(brandId)
                .providerId(providerId)
                .build();
        productRepository.save(product).block();
    }
}