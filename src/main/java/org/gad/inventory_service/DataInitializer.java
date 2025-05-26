package org.gad.inventory_service;

import lombok.extern.slf4j.Slf4j;
import org.gad.inventory_service.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(ReactiveMongoTemplate reactiveMongoTemplate) {
        return args ->
                Mono.when(
                        reactiveMongoTemplate.dropCollection(Permission.class),
                        reactiveMongoTemplate.dropCollection(Role.class),
                        reactiveMongoTemplate.dropCollection(User.class),
                        reactiveMongoTemplate.dropCollection(Category.class),
                        reactiveMongoTemplate.dropCollection(Brand.class),
                        reactiveMongoTemplate.dropCollection(Provider.class),
                        reactiveMongoTemplate.dropCollection(Product.class),
                        reactiveMongoTemplate.dropCollection(Stocktaking.class),
                        reactiveMongoTemplate.dropCollection(Sale.class)
                ).thenMany(
                        Flux.just(
                                        Permission.builder().name("CREATE_BRAND").isActive(true).build(),
                                        Permission.builder().name("READ_BRAND").isActive(true).build(),
                                        Permission.builder().name("UPDATE_BRAND").isActive(true).build(),
                                        Permission.builder().name("DELETE_BRAND").isActive(true).build(),

                                        Permission.builder().name("CREATE_CATEGORY").isActive(true).build(),
                                        Permission.builder().name("READ_CATEGORY").isActive(true).build(),
                                        Permission.builder().name("UPDATE_CATEGORY").isActive(true).build(),
                                        Permission.builder().name("DELETE_CATEGORY").isActive(true).build(),

                                        Permission.builder().name("CREATE_PERMISSION").isActive(true).build(),
                                        Permission.builder().name("READ_PERMISSION").isActive(true).build(),
                                        Permission.builder().name("UPDATE_PERMISSION").isActive(true).build(),
                                        Permission.builder().name("DELETE_PERMISSION").isActive(true).build(),

                                        Permission.builder().name("CREATE_PRODUCT").isActive(true).build(),
                                        Permission.builder().name("READ_PRODUCT").isActive(true).build(),
                                        Permission.builder().name("UPDATE_PRODUCT").isActive(true).build(),
                                        Permission.builder().name("DELETE_PRODUCT").isActive(true).build(),

                                        Permission.builder().name("CREATE_PROVIDER").isActive(true).build(),
                                        Permission.builder().name("READ_PROVIDER").isActive(true).build(),
                                        Permission.builder().name("UPDATE_PROVIDER").isActive(true).build(),
                                        Permission.builder().name("DELETE_PROVIDER").isActive(true).build(),

                                        Permission.builder().name("CREATE_ROLE").isActive(true).build(),
                                        Permission.builder().name("READ_ROLE").isActive(true).build(),
                                        Permission.builder().name("UPDATE_ROLE").isActive(true).build(),
                                        Permission.builder().name("DELETE_ROLE").isActive(true).build(),

                                        Permission.builder().name("CREATE_SALE").isActive(true).build(),
                                        Permission.builder().name("READ_SALE").isActive(true).build(),
                                        Permission.builder().name("UPDATE_SALE").isActive(true).build(),
                                        Permission.builder().name("DELETE_SALE").isActive(true).build(),

                                        Permission.builder().name("CREATE_STOCKTAKING").isActive(true).build(),
                                        Permission.builder().name("READ_STOCKTAKING").isActive(true).build(),
                                        Permission.builder().name("UPDATE_STOCKTAKING").isActive(true).build(),
                                        Permission.builder().name("DELETE_STOCKTAKING").isActive(true).build(),

                                        Permission.builder().name("CREATE_USER").isActive(true).build(),
                                        Permission.builder().name("READ_USER").isActive(true).build(),
                                        Permission.builder().name("UPDATE_USER").isActive(true).build(),
                                        Permission.builder().name("DELETE_USER").isActive(true).build()
                                ).flatMap(reactiveMongoTemplate::save)
                                .collectList()
                                .flatMapMany(permissions -> {
                                    Permission createBrand = permissions.get(0);
                                    Permission readBrand = permissions.get(1);
                                    Permission updateBrand = permissions.get(2);
                                    Permission deleteBrand = permissions.get(3);

                                    Permission createCategory = permissions.get(4);
                                    Permission readCategory = permissions.get(5);
                                    Permission updateCategory = permissions.get(6);
                                    Permission deleteCategory = permissions.get(7);

                                    Permission createPermission = permissions.get(8);
                                    Permission readPermission = permissions.get(9);
                                    Permission updatePermission = permissions.get(10);
                                    Permission deletePermission = permissions.get(11);

                                    Permission createProduct = permissions.get(12);
                                    Permission readProduct = permissions.get(13);
                                    Permission updateProduct = permissions.get(14);
                                    Permission deleteProduct = permissions.get(15);

                                    Permission createProvider = permissions.get(16);
                                    Permission readProvider = permissions.get(17);
                                    Permission updateProvider = permissions.get(18);
                                    Permission deleteProvider = permissions.get(19);

                                    Permission createRole = permissions.get(20);
                                    Permission readRole = permissions.get(21);
                                    Permission updateRole = permissions.get(22);
                                    Permission deleteRole = permissions.get(23);

                                    Permission createSale = permissions.get(24);
                                    Permission readSale = permissions.get(25);
                                    Permission updateSale = permissions.get(26);
                                    Permission deleteSale = permissions.get(27);

                                    Permission createStocktaking = permissions.get(28);
                                    Permission readStocktaking = permissions.get(29);
                                    Permission updateStocktaking = permissions.get(30);
                                    Permission deleteStocktaking = permissions.get(31);

                                    Permission createUser = permissions.get(32);
                                    Permission readUser = permissions.get(33);
                                    Permission updateUser = permissions.get(34);
                                    Permission deleteUser = permissions.get(35);

                                    return Flux.just(
                                            Role.builder().name("ROLE_ADMIN").permissions(Set.of(
                                                    createBrand, readBrand,
                                                    updateBrand, deleteBrand,
                                                    createCategory, readCategory,
                                                    updateCategory, deleteCategory,
                                                    createPermission, readPermission,
                                                    updatePermission, deletePermission,
                                                    createProduct, readProduct,
                                                    updateProduct, deleteProduct,
                                                    createProvider, readProvider,
                                                    updateProvider, deleteProvider,
                                                    createRole, readRole,
                                                    updateRole, deleteRole,
                                                    createSale, readSale,
                                                    updateSale, deleteSale,
                                                    createStocktaking, readStocktaking,
                                                    updateStocktaking, deleteStocktaking,
                                                    createUser, readUser,
                                                    updateUser, deleteUser
                                            )).isActive(true).build(),
                                            Role.builder().name("ROLE_INVENTORY_MANAGER").permissions(Set.of(
                                                    createProduct, readProduct,
                                                    updateProduct, deleteProduct,
                                                    createCategory, readCategory,
                                                    updateCategory, deleteCategory,
                                                    createBrand, readBrand,
                                                    updateBrand, deleteBrand,
                                                    createProvider, readProvider,
                                                    updateProvider, deleteProvider,
                                                    createStocktaking, readStocktaking,
                                                    readSale)).isActive(true).build(),
                                            Role.builder().name("ROLE_SALESPERSON").permissions(Set.of(
                                                    createSale, readSale,
                                                    updateSale, deleteSale,
                                                    readProduct, readStocktaking)).isActive(true).build(),
                                            Role.builder().name("ROLE_WAREHOUSE_STAFF").permissions(Set.of(
                                                    createStocktaking, readStocktaking,
                                                    updateStocktaking, deleteStocktaking,
                                                    readProduct, readProvider)).isActive(true).build(),
                                            Role.builder().name("ROLE_ANALYST").permissions(Set.of(
                                                    readBrand, readCategory,
                                                    readProduct, readProvider,
                                                    readRole, readSale,
                                                    readStocktaking, readUser)).isActive(true).build(),
                                            Role.builder().name("ROLE_SUPPORT").permissions(Set.of(
                                                    createUser, readUser,
                                                    updateUser, createRole,
                                                    readRole, updateRole,
                                                    readBrand, readCategory,
                                                    readProduct, readProvider,
                                                    readSale, readStocktaking)).isActive(true).build(),
                                            Role.builder().name("ROLE_BASIC_USER").permissions(Set.of(
                                                    readProduct, readCategory,
                                                    readBrand, readProvider)).isActive(true).build()
                                    ).flatMap(reactiveMongoTemplate::save);
                                })
                                .collectList()
                                .flatMapMany(roles -> {
                                    Role admin = roles.get(0);
                                    Role inventoryManager = roles.get(1);
                                    Role salesPerson = roles.get(2);
                                    Role warehouseStaff = roles.get(3);
                                    Role analyst = roles.get(4);
                                    Role support = roles.get(5);
                                    Role roleBasic = roles.get(6);

                                    return Flux.just(
                                            User.builder()
                                                    .name("Admin").lastName("System").username("admin")
                                                    .password("$2a$10$xyz123").email("admin@inventory.com")
                                                    .phone("123456789").roles(Set.of(admin)).isActive(true).build(),
                                            User.builder()
                                                    .name("Juan").lastName("Perez").username("jperez")
                                                    .password("$2a$10$xyz456").email("jperez@inventory.com")
                                                    .phone("987654321").roles(Set.of(inventoryManager)).isActive(true).build(),
                                            User.builder()
                                                    .name("Maria").lastName("Gomez").username("mgomez")
                                                    .password("$2a$10$xyz789").email("mgomez@inventory.com")
                                                    .phone("555444333").roles(Set.of(salesPerson)).isActive(true).build(),
                                            User.builder()
                                                    .name("Carlos").lastName("Lopez").username("clopez")
                                                    .password("$2a$10$xyz012").email("clopez@inventory.com")
                                                    .phone("111222333").roles(Set.of(warehouseStaff)).isActive(true).build(),
                                            User.builder()
                                                    .name("Ana").lastName("Martinez").username("amartinez")
                                                    .password("$2a$10$xyz345").email("amartinez@inventory.com")
                                                    .phone("444555666").roles(Set.of(analyst)).isActive(true).build(),
                                            User.builder()
                                                    .name("Luis").lastName("Rodriguez").username("lrodriguez")
                                                    .password("$2a$10$xyz678").email("lrodriguez@inventory.com")
                                                    .phone("777888999").roles(Set.of(support)).isActive(true).build(),
                                            User.builder()
                                                    .name("Basic").lastName("User").username("basicuser")
                                                    .password("$2a$10$xyz123").email("userbasic@gmail.com")
                                                    .phone("000111222").roles(Set.of(roleBasic)).isActive(true).build()

                                    ).flatMap(reactiveMongoTemplate::save);
                                })
                                .thenMany(
                                        Flux.just(
                                                Category.builder().name("Electrónicos").isActive(true).build(),
                                                Category.builder().name("Ropa").isActive(true).build(),
                                                Category.builder().name("Hogar").isActive(true).build(),
                                                Category.builder().name("Deportes").isActive(true).build(),
                                                Category.builder().name("Juguetes").isActive(true).build(),
                                                Category.builder().name("Alimentos").isActive(true).build()
                                        ).flatMap(reactiveMongoTemplate::save)
                                )
                                .thenMany(
                                        Flux.just(
                                                Brand.builder().name("Samsung").isActive(true).build(),
                                                Brand.builder().name("Nike").isActive(true).build(),
                                                Brand.builder().name("Sony").isActive(true).build(),
                                                Brand.builder().name("Adidas").isActive(true).build(),
                                                Brand.builder().name("Apple").isActive(true).build(),
                                                Brand.builder().name("LG").isActive(true).build()
                                        ).flatMap(reactiveMongoTemplate::save)
                                )
                                .thenMany(
                                        Flux.just(
                                                Provider.builder()
                                                        .name("TecnoSuministros S.A.").ruc("12345678901").dni("12345678")
                                                        .address("Av. Tecnológica 123").phone("111222333")
                                                        .email("contacto@tecnosum.com").isActive(true).build(),
                                                Provider.builder()
                                                        .name("Distribuidora Rápida").ruc("98765432109").dni("23456789")
                                                        .address("Calle Veloz 456").phone("444555666")
                                                        .email("info@distribuidorarapida.com").isActive(true).build(),
                                                Provider.builder()
                                                        .name("Juan Pérez").ruc("12345678901").dni("87654321")
                                                        .address("Jr. Independencia 789").phone("777888999")
                                                        .email("juanperez@proveedor.com").isActive(true).build(),
                                                Provider.builder()
                                                        .name("Importaciones Elite").ruc("45678912345").dni("12345978")
                                                        .address("Av. Importadora 321").phone("222333444")
                                                        .email("ventas@eliteimport.com").isActive(true).build(),
                                                Provider.builder()
                                                        .name("María Gómez").ruc("98765498765").dni("76543210")
                                                        .address("Calle Proveedora 654").phone("555666777")
                                                        .email("mariagomez@proveedor.com").isActive(true).build(),
                                                Provider.builder()
                                                        .name("Global Suppliers").ruc("32165498701").dni("98765432")
                                                        .address("Av. Mundial 987").phone("888999000")
                                                        .email("contact@globalsuppliers.com").isActive(true).build()
                                        ).flatMap(reactiveMongoTemplate::save)
                                )
                                .thenMany(
                                        reactiveMongoTemplate.findAll(Category.class).collectList().flatMapMany(categories -> {
                                            Category electronicos = categories.get(0);
                                            Category ropa = categories.get(1);
                                            Category hogar = categories.get(2);
                                            Category deportes = categories.get(3);

                                            return reactiveMongoTemplate.findAll(Brand.class).collectList().flatMapMany(brands -> {
                                                Brand samsung = brands.get(0);
                                                Brand nike = brands.get(1);
                                                Brand sony = brands.get(2);
                                                Brand adidas = brands.get(3);
                                                Brand apple = brands.get(4);

                                                return reactiveMongoTemplate.findAll(Provider.class).collectList().flatMapMany(providers -> {
                                                    Provider prov1 = providers.get(0);
                                                    Provider prov2 = providers.get(1);
                                                    Provider prov3 = providers.get(2);
                                                    Provider prov4 = providers.get(3);
                                                    Provider prov5 = providers.get(4);
                                                    Provider prov6 = providers.get(5);

                                                    return Flux.just(
                                                            Product.builder()
                                                                    .name("Smartphone Galaxy S21").description("Teléfono inteligente de última generación")
                                                                    .price(new BigDecimal("899.99")).categoryId(electronicos.getIdCategory())
                                                                    .brandId(samsung.getIdBrand()).providerId(prov1.getIdProvider()).isActive(true).build(),
                                                            Product.builder()
                                                                    .name("Zapatillas Air Max").description("Zapatillas deportivas para running")
                                                                    .price(new BigDecimal("129.99")).categoryId(ropa.getIdCategory())
                                                                    .brandId(nike.getIdBrand()).providerId(prov2.getIdProvider()).isActive(true).build(),
                                                            Product.builder()
                                                                    .name("Televisor OLED 55").description("TV con tecnología OLED y resolución 4K")
                                                                    .price(new BigDecimal("1299.99")).categoryId(electronicos.getIdCategory())
                                                                    .brandId(apple.getIdBrand()).providerId(prov4.getIdProvider()).isActive(true).build(),
                                                            Product.builder()
                                                                    .name("Sofá Seccional").description("Sofá de 3 plazas en tela resistente")
                                                                    .price(new BigDecimal("599.99")).categoryId(hogar.getIdCategory())
                                                                    .brandId(apple.getIdBrand()).providerId(prov3.getIdProvider()).isActive(true).build(),
                                                            Product.builder()
                                                                    .name("Balón de Fútbol").description("Balón oficial tamaño 5")
                                                                    .price(new BigDecimal("29.99")).categoryId(deportes.getIdCategory())
                                                                    .brandId(adidas.getIdBrand()).providerId(prov5.getIdProvider()).isActive(true).build(),
                                                            Product.builder()
                                                                    .name("Auriculares Inalámbricos").description("Auriculares con cancelación de ruido")
                                                                    .price(new BigDecimal("199.99")).categoryId(electronicos.getIdCategory())
                                                                    .brandId(sony.getIdBrand()).providerId(prov6.getIdProvider()).isActive(true).build()
                                                    ).flatMap(reactiveMongoTemplate::save);
                                                });
                                            });
                                        })
                                )
                                .thenMany(
                                        reactiveMongoTemplate.findAll(Product.class).collectList().flatMapMany(products -> {
                                            Product prod1 = products.get(0);
                                            Product prod2 = products.get(1);
                                            Product prod3 = products.get(2);
                                            Product prod4 = products.get(3);
                                            Product prod5 = products.get(4);
                                            Product prod6 = products.get(5);

                                            return reactiveMongoTemplate.findAll(User.class).collectList().flatMapMany(users -> {
                                                User user1 = users.get(0);
                                                User user2 = users.get(1);
                                                User user3 = users.get(2);
                                                User user4 = users.get(3);
                                                User user5 = users.get(4);
                                                User user6 = users.get(5);

                                                return Flux.just(
                                                        Stocktaking.builder()
                                                                .productId(prod1.getIdProduct()).quantity(50)
                                                                .performedBy(user1.getIdUser()).build(),
                                                        Stocktaking.builder()
                                                                .productId(prod2.getIdProduct()).quantity(120)
                                                                .performedBy(user2.getIdUser()).build(),
                                                        Stocktaking.builder()
                                                                .productId(prod3.getIdProduct()).quantity(25)
                                                                .performedBy(user3.getIdUser()).build(),
                                                        Stocktaking.builder()
                                                                .productId(prod4.getIdProduct()).quantity(15)
                                                                .performedBy(user4.getIdUser()).build(),
                                                        Stocktaking.builder()
                                                                .productId(prod5.getIdProduct()).quantity(80)
                                                                .performedBy(user5.getIdUser()).build(),
                                                        Stocktaking.builder()
                                                                .productId(prod6.getIdProduct()).quantity(40)
                                                                .performedBy(user6.getIdUser()).build()
                                                ).flatMap(reactiveMongoTemplate::save);
                                            });
                                        })
                                )
                                .thenMany(
                                        reactiveMongoTemplate.findAll(Product.class).collectList().flatMapMany(products -> {
                                            Product prod1 = products.get(0);
                                            Product prod2 = products.get(1);
                                            Product prod3 = products.get(2);
                                            Product prod4 = products.get(3);
                                            Product prod5 = products.get(4);
                                            Product prod6 = products.get(5);

                                            return Flux.just(
                                                    Sale.builder()
                                                            .productId(prod1.getIdProduct()).quantity(2)
                                                            .totalPrice(prod1.getPrice().multiply(new BigDecimal(2))).build(),
                                                    Sale.builder()
                                                            .productId(prod2.getIdProduct()).quantity(1)
                                                            .totalPrice(prod2.getPrice()).build(),
                                                    Sale.builder()
                                                            .productId(prod3.getIdProduct()).quantity(1)
                                                            .totalPrice(prod3.getPrice()).build(),
                                                    Sale.builder()
                                                            .productId(prod4.getIdProduct()).quantity(1)
                                                            .totalPrice(prod4.getPrice()).build(),
                                                    Sale.builder()
                                                            .productId(prod5.getIdProduct()).quantity(3)
                                                            .totalPrice(prod5.getPrice().multiply(new BigDecimal(3))).build(),
                                                    Sale.builder()
                                                            .productId(prod6.getIdProduct()).quantity(2)
                                                            .totalPrice(prod6.getPrice().multiply(new BigDecimal(2))).build()
                                            ).flatMap(reactiveMongoTemplate::save);
                                        })
                                )
                ).subscribe(
                        null,
                        p -> log.error("Error al inicializar la base de datos: {}", p.getMessage()),
                        () -> log.info("Base de datos inicializada correctamente con datos de prueba.")
                );
    }
}