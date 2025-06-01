package org.gad.inventory_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.dto.request.CreateProductRequest;
import org.gad.inventory_service.dto.ProductDTO;
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
import org.gad.inventory_service.service.ProductService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.Mappers;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.gad.inventory_service.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProviderRepository providerRepository;

    @Override
    public Flux<ProductDTO> findAllProducts() {
        return productRepository.findAll()
                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_FLUX)))
                .map(product -> Mappers.productToDTO(product, product.getCategoryId(), product.getBrandId(), product.getProviderId()))
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_PRODUCT, error.getMessage()));
    }

    @Override
    public Mono<ProductDTO> findProductById(String id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + id)))
                .map(productSaved -> Mappers.productToDTO(productSaved,
                        productSaved.getCategoryId(), productSaved.getBrandId(), productSaved.getProviderId()))
                .doOnError(error -> log.error(Constants.ERROR_SEARCHING_PRODUCT, error.getMessage()));
    }

    @Override
    public Flux<ProductDTO> findProductsByCriteria(String name,
                                                   String categoryName,
                                                   String brandName,
                                                   String providerName) {
        Mono<Category> categoryMono = findCategoryByName(categoryName);
        Mono<Brand> brandMono = findBrandByName(brandName);
        Mono<Provider> providerMono = findProviderByName(providerName);

        return Mono.zip(categoryMono, brandMono, providerMono)
                .flatMapMany(tuple -> {
                    Category category = tuple.getT1();
                    Brand brand = tuple.getT2();
                    Provider provider = tuple.getT3();

                    return productRepository.findByCriteria(name, category.getIdCategory(), brand.getIdBrand(), provider.getIdProvider())
                            .map(product -> Mappers.productToDTO(product, category.getName(), brand.getName(), provider.getName()));
                })
                .doOnError(error -> log.error(ERROR_SEARCHING_PRODUCT, error.getMessage()));
    }

    @Override
    public Mono<ProductDTO> createProduct(CreateProductRequest createProductRequest) {
        Mono<Category> categoryMono = findCategoryByName(createProductRequest.categoryName());
        Mono<Brand> brandMono = findBrandByName(createProductRequest.brandName());
        Mono<Provider> providerMono = findProviderByName(createProductRequest.providerName());

        return Mono.zip(categoryMono, brandMono, providerMono)
                .flatMap(tuple -> {
                    Category category = tuple.getT1();
                    Brand brand = tuple.getT2();
                    Provider provider = tuple.getT3();
                    Product product = new Product();

                    return productRepository.save(buildProductFromRequest(product, createProductRequest.name(),
                                    createProductRequest.description(), UtilsMethods.formatPrice(createProductRequest.price()),
                                    category.getIdCategory(), brand.getIdBrand(), provider.getIdProvider()))
                            .map(productSaved -> Mappers.productToDTO(productSaved, category.getName(), brand.getName(), provider.getName()));
                })
                .doOnError(error -> log.error(ERROR_CREATING_PRODUCTS, error.getMessage()));
    }

    @Override
    public Mono<ProductDTO> updateProduct(String id, UpdateProductRequest updateProductRequest) {
        Mono<Product> existingProductMono = findById(id);
        Mono<Category> categoryMono = findCategoryByName(updateProductRequest.categoryName());
        Mono<Brand> brandMono = findBrandByName(updateProductRequest.brandName());
        Mono<Provider> providerMono = findProviderByName(updateProductRequest.providerName());

        return existingProductMono
                .zipWith(Mono.zip(categoryMono, brandMono, providerMono))
                .flatMap(tuple -> {
                    Product existingProduct = tuple.getT1();
                    Category category = tuple.getT2().getT1();
                    Brand brand = tuple.getT2().getT2();
                    Provider provider = tuple.getT2().getT3();

                    return productRepository.save(buildProductFromRequest(existingProduct, updateProductRequest.name(),
                                    updateProductRequest.description(), UtilsMethods.formatPrice(updateProductRequest.price()),
                                    category.getIdCategory(), brand.getIdBrand(), provider.getIdProvider()))
                            .map(productSaved -> Mappers.productToDTO(productSaved, category.getName(), brand.getName(), provider.getName()));
                })
                .doOnError(error -> log.error(ERROR_UPDATING_PRODUCT, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteProductById(String id) {
        Mono<Product> existingProductMono = findById(id);
        return productRepository.deleteById(existingProductMono.map(Product::getIdProduct))
                .doOnError(error -> log.error(ERROR_DELETING_PRODUCT, error.getMessage()));
    }

    private Mono<Product> findById(String id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(PRODUCT_NOT_FOUND_ID + id)));
    }

    private Mono<Category> findCategoryByName(String name) {
        return categoryRepository.findCategoryByNameContainingIgnoreCase(name)
                .switchIfEmpty(Mono.error(new CategoryNotFoundException(CATEGORY_NOT_FOUND_NAME + name)));
    }

    private Mono<Brand> findBrandByName(String name) {
        return brandRepository.findBrandByNameContainingIgnoreCase(name)
                .switchIfEmpty(Mono.error(new BrandNotFoundException(BRAND_NOT_FOUND_NAME + name)));
    }

    private Mono<Provider> findProviderByName(String name) {
        return providerRepository.findProviderByNameContainingIgnoreCase(name)
                .switchIfEmpty(Mono.error(new ProviderNotFoundException(PROVIDER_NOT_FOUND_NAME + name)));
    }

    private Product buildProductFromRequest(Product product, String name, String description, BigDecimal price, String categoryId, String brandId, String providerId) {
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategoryId(categoryId);
        product.setBrandId(brandId);
        product.setProviderId(providerId);
        return product;
    }
}
