package org.gad.inventory_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.model.*;
import org.gad.inventory_service.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;
    private final StocktakingRepository stocktakingRepository;
    private final SaleRepository saleRepository;
    Random random = new Random();

    @Override
    public void run(String... args) {
        Mono.when(
                        categoryRepository.deleteAll(),
                        brandRepository.deleteAll(),
                        providerRepository.deleteAll(),
                        productRepository.deleteAll(),
                        stocktakingRepository.deleteAll(),
                        saleRepository.deleteAll()

                )
                .thenMany(
                        Flux.defer(() -> {
                                    Flux<Category> categories = categoryRepository.saveAll(Flux.just(
                                            new Category(UUID.randomUUID(), "Appliances"),
                                            new Category(UUID.randomUUID(), "Technology"),
                                            new Category(UUID.randomUUID(), "Home"),
                                            new Category(UUID.randomUUID(), "Sports"),
                                            new Category(UUID.randomUUID(), "Clothing"),
                                            new Category(UUID.randomUUID(), "Toys"),
                                            new Category(UUID.randomUUID(), "Food"),
                                            new Category(UUID.randomUUID(), "Beverages"),
                                            new Category(UUID.randomUUID(), "Furniture"),
                                            new Category(UUID.randomUUID(), "Garden")
                                    ).doOnNext(c -> log.info("Category inserted: {}", c.getName())));

                                    Flux<Brand> brands = brandRepository.saveAll(Flux.just(
                                            new Brand(UUID.randomUUID(), "Samsung"),
                                            new Brand(UUID.randomUUID(), "LG"),
                                            new Brand(UUID.randomUUID(), "Sony"),
                                            new Brand(UUID.randomUUID(), "Xiaomi"),
                                            new Brand(UUID.randomUUID(), "Apple"),
                                            new Brand(UUID.randomUUID(), "Nike"),
                                            new Brand(UUID.randomUUID(), "Adidas"),
                                            new Brand(UUID.randomUUID(), "Coca-Cola"),
                                            new Brand(UUID.randomUUID(), "Nestlé"),
                                            new Brand(UUID.randomUUID(), "IKEA")
                                    ).doOnNext(b -> log.info("Brand inserted: {}", b.getName())));

                                    Flux<Provider> providers = providerRepository.saveAll(Flux.just(
                                            new Provider(UUID.randomUUID(), "Distributor ABC", "12345678901", "12345678", "Main Street 123", "987654321", "abc@gmail.com"),
                                            new Provider(UUID.randomUUID(), "Imports XYZ", "20458796532", "87654321", "First Avenue 456", "987123456", "xyz@hotmail.com"),
                                            new Provider(UUID.randomUUID(), "Tech Global", "10789456321", "11223344", "Commerce Street 789", "963852741", "techglobal@outlook.com"),
                                            new Provider(UUID.randomUUID(), "Food Inc.", "20547896543", "44556677", "Industrial Avenue 101", "912345678", "info@foodinc.com"),
                                            new Provider(UUID.randomUUID(), "Global Beverages", "10458963217", "99887766", "Refreshment Street 202", "933221144", "contact@globalbeverages.com"),
                                            new Provider(UUID.randomUUID(), "Modern Furniture", "20123456789", "55443322", "Design Boulevard 303", "944556677", "sales@modernfurniture.com"),
                                            new Provider(UUID.randomUUID(), "Extreme Sports", "20659874123", "11223399", "Sports Plaza 404", "955667788", "info@extremesports.com"),
                                            new Provider(UUID.randomUUID(), "Fashion Trends", "10987654321", "88776655", "Mall Center 505", "966778899", "contact@fashiontrends.com"),
                                            new Provider(UUID.randomUUID(), "Educational Toys", "20874563219", "33445566", "Kids Park 606", "977889900", "sales@educationaltoys.com"),
                                            new Provider(UUID.randomUUID(), "Professional Gardening", "10234567891", "77889900", "Nursery Street 707", "988990011", "info@progardening.com")
                                    ).doOnNext(p -> log.info("Provider inserted: {}", p.getName())));

                                    return Flux.zip(
                                            categories.collectList(),
                                            brands.collectList(),
                                            providers.collectList()
                                    ).flatMap(tuple -> {
                                        List<Category> categoryList = tuple.getT1();
                                        List<Brand> brandList = tuple.getT2();
                                        List<Provider> providerList = tuple.getT3();

                                        return productRepository.saveAll(Flux.just(
                                                new Product(UUID.randomUUID(), "55-inch TV", "4K UHD Smart TV", new BigDecimal("2499.99"),
                                                        categoryList.get(0), brandList.get(0), providerList.get(0)),
                                                new Product(UUID.randomUUID(), "Refrigerator", "Double door, frost free", new BigDecimal("1899.99"),
                                                        categoryList.get(0), brandList.get(1), providerList.get(0)),
                                                new Product(UUID.randomUUID(), "Washing Machine", "Front load 15kg", new BigDecimal("899.99"),
                                                        categoryList.get(0), brandList.get(2), providerList.get(1)),

                                                new Product(UUID.randomUUID(), "Ultrabook Laptop", "16GB RAM, 512GB SSD", new BigDecimal("3599.99"),
                                                        categoryList.get(1), brandList.get(0), providerList.get(2)),
                                                new Product(UUID.randomUUID(), "5G Smartphone", "128GB storage", new BigDecimal("1299.99"),
                                                        categoryList.get(1), brandList.get(3), providerList.get(2)),
                                                new Product(UUID.randomUUID(), "10-inch Tablet", "2K resolution", new BigDecimal("499.99"),
                                                        categoryList.get(1), brandList.get(4), providerList.get(1)),

                                                new Product(UUID.randomUUID(), "Bed Sheet Set", "Egyptian cotton, king size", new BigDecimal("129.99"),
                                                        categoryList.get(2), brandList.get(5), providerList.get(3)),
                                                new Product(UUID.randomUUID(), "Blackout Curtains", "For large windows", new BigDecimal("89.99"),
                                                        categoryList.get(2), brandList.get(6), providerList.get(4)),

                                                new Product(UUID.randomUUID(), "Soccer Ball", "Official size", new BigDecimal("49.99"),
                                                        categoryList.get(3), brandList.get(5), providerList.get(6)),
                                                new Product(UUID.randomUUID(), "Tennis Racket", "Lightweight", new BigDecimal("129.99"),
                                                        categoryList.get(3), brandList.get(6), providerList.get(6)),

                                                new Product(UUID.randomUUID(), "Sports T-shirt", "Dry-fit technology", new BigDecimal("39.99"),
                                                        categoryList.get(4), brandList.get(5), providerList.get(7)),
                                                new Product(UUID.randomUUID(), "Running Shoes", "Air cushioning", new BigDecimal("119.99"),
                                                        categoryList.get(4), brandList.get(6), providerList.get(7)),

                                                new Product(UUID.randomUUID(), "Building Blocks Set", "250 pieces", new BigDecimal("59.99"),
                                                        categoryList.get(5), brandList.get(8), providerList.get(8)),
                                                new Product(UUID.randomUUID(), "Interactive Doll", "Talks and walks", new BigDecimal("79.99"),
                                                        categoryList.get(5), brandList.get(9), providerList.get(8)),

                                                new Product(UUID.randomUUID(), "Whole Grain Cereal", "500g, high fiber", new BigDecimal("5.99"),
                                                        categoryList.get(6), brandList.get(8), providerList.get(3)),
                                                new Product(UUID.randomUUID(), "Dark Chocolate", "70% cocoa", new BigDecimal("3.99"),
                                                        categoryList.get(6), brandList.get(8), providerList.get(4)),

                                                new Product(UUID.randomUUID(), "Mineral Water", "6-bottle pack", new BigDecimal("4.99"),
                                                        categoryList.get(7), brandList.get(7), providerList.get(4)),
                                                new Product(UUID.randomUUID(), "Cola Drink", "12-can pack", new BigDecimal("7.99"),
                                                        categoryList.get(7), brandList.get(7), providerList.get(4)),

                                                new Product(UUID.randomUUID(), "Sectional Sofa", "Durable fabric", new BigDecimal("899.99"),
                                                        categoryList.get(8), brandList.get(9), providerList.get(5)),
                                                new Product(UUID.randomUUID(), "Coffee Table", "Oak wood", new BigDecimal("299.99"),
                                                        categoryList.get(8), brandList.get(9), providerList.get(5)),

                                                new Product(UUID.randomUUID(), "Gardening Tool Set", "5 pieces", new BigDecimal("49.99"),
                                                        categoryList.get(9), brandList.get(9), providerList.get(9)),
                                                new Product(UUID.randomUUID(), "Gas Grill", "3 burners", new BigDecimal("199.99"),
                                                        categoryList.get(9), brandList.get(1), providerList.get(9))
                                        ).doOnNext(p -> log.info("Product inserted: {}", p.getName())));
                                    });
                                })
                                .thenMany(productRepository.findAll())
                                .flatMap(product ->
                                        Flux.range(0, 3)
                                                .flatMap(i -> {
                                                    int randomQuantity = random.nextInt(100) + 10;
                                                    int randomDays = random.nextInt(365);
                                                    LocalDateTime randomDate = LocalDateTime.now().minusDays(randomDays);

                                                    Stocktaking stocktaking = Stocktaking.builder()
                                                            .idStocktaking(UUID.randomUUID())
                                                            .product(product)
                                                            .quantity(randomQuantity)
                                                            .stocktakingDate(randomDate)
                                                            .build();

                                                    return stocktakingRepository.save(stocktaking)
                                                            .doOnNext(s -> log.info("Stocktaking created for product {}: quantity {}",
                                                                    s.getProduct().getName(), s.getQuantity()));
                                                })
                                )
                                .thenMany(productRepository.findAll().take(10))
                                .flatMap(product -> {
                                    int salesCount = random.nextInt(5) + 1;
                                    return Flux.range(0, salesCount)
                                            .flatMap(i -> {
                                                int quantitySold = random.nextInt(10) + 1;
                                                BigDecimal unitPrice = product.getPrice();
                                                BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantitySold));
                                                LocalDateTime saleDate = LocalDateTime.now()
                                                        .minusDays(random.nextInt(30));

                                                Sale sale = Sale.builder()
                                                        .idSale(UUID.randomUUID())
                                                        .product(product)
                                                        .quantity(quantitySold)
                                                        .totalPrice(totalPrice)
                                                        .saleDate(saleDate)
                                                        .build();

                                                return saleRepository.save(sale)
                                                        .doOnNext(s -> log.info("Venta creada: Producto={}, Cantidad={}, Total={}",
                                                                s.getProduct().getName(), s.getQuantity(), s.getTotalPrice()));
                                            });
                                })
                ).subscribe(
                        null,
                        error -> log.error("Error initializing data: {}", error.getMessage()),
                        () -> log.info("Data initialization completed successfully")
                );
    }
}
