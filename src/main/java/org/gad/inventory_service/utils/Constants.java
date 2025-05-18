package org.gad.inventory_service.utils;

public class Constants {
    private Constants() {
    }

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TEXT_AND = " and ";

    public static final String PRODUCT_NOT_FOUND_FLUX_CRITERIA = "No products found with the given criteria";
    public static final String PRODUCT_NOT_FOUND_NAME = "Product not found with name: ";
    public static final String PRODUCT_NOT_FOUND_UUID = "Product not found with uuid: ";
    public static final String ERROR_SEARCHING_PRODUCT = "Error when searching for product: {}";
    public static final String ERROR_CREATING_PRODUCTS = "Error when creating product: {}";
    public static final String ERROR_UPDATING_PRODUCT = "Error when updating product: {}";
    public static final String ERROR_DELETING_PRODUCT = "Error when deleting product: {}";

    public static final String BRAND_NOT_FOUND_FLUX = "No brands found";
    public static final String BRAND_NOT_FOUND_UUID = "Brand not found with uuid: ";
    public static final String BRAND_NOT_FOUND_NAME = "Brand not found with name: ";
    public static final String ERROR_SEARCHING_BRAND = "Error when searching for brand: {}";
    public static final String ERROR_SEARCHING_BRANDS = "Error when searching for brands: {}";
    public static final String ERROR_SAVING_BRAND = "Error when saving brand: {}";
    public static final String ERROR_UPDATING_BRAND = "Error when updating brand: {}";
    public static final String ERROR_DELETING_BRAND = "Error when deleting brand: {}";

    public static final String CATEGORY_NOT_FOUND_FLUX = "No categories found";
    public static final String CATEGORY_NOT_FOUND_UUID = "Category not found with uuid: ";
    public static final String CATEGORY_NOT_FOUND_NAME = "Category not found with name: ";
    public static final String CATEGORY_SEARCHING_BRAND = "Error when searching for category: {}";
    public static final String CATEGORY_SEARCHING_BRANDS = "Error when searching for categories: {}";
    public static final String CATEGORY_SAVING_BRAND = "Error when saving category: {}";
    public static final String CATEGORY_UPDATING_BRAND = "Error when updating category: {}";
    public static final String CATEGORY_DELETING_BRAND = "Error when deleting category: {}";

    public static final String PROVIDER_NOT_FOUND_FLUX = "No providers found";
    public static final String PROVIDER_NOT_FOUND_UUID = "Provider not found with uuid: ";
    public static final String PROVIDER_NOT_FOUND_NAME = "Provider not found with name: ";
    public static final String PROVIDER_NOT_FOUND_RUC = "Provider not found with ruc: ";
    public static final String PROVIDER_NOT_FOUND_EMAIL = "Provider not found with email: ";
    public static final String PROVIDER_NOT_FOUND_DNI = "Provider not found with dni: ";
    public static final String ERROR_SEARCHING_PROVIDER = "Error when searching for provider: {}";
    public static final String ERROR_SAVING_PROVIDER = "Error when saving provider: {}";
    public static final String ERROR_UPDATING_PROVIDER = "Error when updating provider: {}";
    public static final String ERROR_DELETING_PROVIDER = "Error when deleting provider: {}";
    public static final String TEXT_PROVIDER = "Provider with ";
    public static final String TEXT_ALREADY_EXISTS = " already exists";
    public static final String TEXT_RUC_ALREADY_EXISTS = "RUC, ";
    public static final String TEXT_EMAIL_ALREADY_EXISTS = "email, ";
    public static final String TEXT_DNI_ALREADY_EXISTS = "DNI, ";

    public static final String STOCKTAKING_NOT_FOUND = "No stocktaking found";
    public static final String STOCKTAKING_NOT_FOUND_UUID = "Stocktaking not found with uuid: ";
    public static final String STOCKTAKING_NOT_FOUND_BETWEEEN_DATES = "No stocktaking found between the given dates: ";
    public static final String STOCKTAKING_NOT_FOUND_NAME = "Stocktaking not found with name: ";
    public static final String ERROR_SEARCHING_STOCKTAKING = "Error when searching for stocktaking: {}";
    public static final String ERROR_CREATING_STOCKTAKING = "Error when creating stocktaking: {}";
    public static final String ERROR_UPDATING_STOCKTAKING = "Error when updating stocktaking: {}";
    public static final String ERROR_DELETING_STOCKTAKING = "Error when deleting stocktaking: {}";

    public static final String SALE_NOT_FOUND_FLUX = "No sales found";
    public static final String SALE_NOT_FOUND_UUID = "Sale not found with uuid: ";
    public static final String SALE_NOT_FOUND_BETWEEN_PRICES = "No sales found between prices: ";
    public static final String SALE_NOT_FOUND_BETWEEN_DATES = "No sales found between dates: ";
    public static final String SALE_NOT_FOUND_NAME = "Sale not found with name product: ";
    public static final String ERROR_SEARCHING_SALE = "Error when searching for sale: {}";
    public static final String ERROR_CREATING_SALE = "Error when creating sale: {}";
    public static final String ERROR_UPDATING_SALE = "Error when updating sale: {}";
    public static final String ERROR_DELETING_SALE = "Error when deleting sale: {}";
    public static final String ERROR_SEARCHING_SALE_UUID = "Error when searching for sale by uuid: {}";
    public static final String ERROR_SEARCHING_SALE_BETWEEN_PRICES = "Error when searching for sales by total price range: {}";
    public static final String ERROR_SEARCHING_SALE_BETWEEN_DATES = "Error when searching for sales by date range: {}";
    public static final String ERROR_SEARCHING_SALE_NAME = "Error when searching for sales by product name: {}";
    public static final String MESSAGE_INVALID_PRICE_RANGE = "The minimum price cannot be greater than the maximum price";

    public static final String REGEX_PRODUCTS = "^[a-zA-Z]*$";
    public static final String REGEX_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String REGEX_RUC = "^\\d{11}$";
    public static final String REGEX_DNI = "^\\d{8}$";

    public static final String PRODUCT_URI = "/api/v1/products/%s";
    public static final String BRAND_URI = "/api/v1/brands/%s";
    public static final String CATEGORY_URI = "/api/v1/categories/%s";
    public static final String PROVIDER_URI = "/api/v1/providers/%s";
    public static final String STOCKTAKING_URI = "/api/v1/stocktaking/%s";

    public static final String MESSAGE_PARAMETER_PRODUCT_NAME = "Parameter name only accepts letters";
    public static final String MESSAGE_PARAMETER_CATEGORY = "Parameter category only accepts letters";
    public static final String MESSAGE_PARAMETER_BRAND = "Parameter brand only accepts letters";
    public static final String MESSAGE_PARAMETER_PROVIDER = "Parameter provider only accepts letters";
    public static final String MESSAGE_INCORRECT_UUID_FORMAT = "Incorrect UUID format";
    public static final String MESSAGE_NAME_CANNOT_BE_EMPTY = "Name cannot be empty";
    public static final String MESSAGE_RUC_CANNOT_BE_EMPTY = "RUC cannot be empty";
    public static final String MESSAGE_DNI_CANNOT_BE_EMPTY = "DNI cannot be empty";
    public static final String MESSAGE_EMAIL_CANNOT_BE_EMPTY = "Email cannot be empty";
    public static final String MESSAGE_EMAIL_INVALID = "Email should be valid";
    public static final String MESSAGE_RUC_INVALID = "RUC should be 11 digits";
    public static final String MESSAGE_DNI_INVALID = "DNI should be 8 digits";
    public static final String MESSAGE_PRODUCT_EMPTY = "Product name cannot be empty";

    public static final String MESSAGE_PRODUCTS_OK = "Products retrieved successfully";
    public static final String MESSAGE_PRODUCT_OK = "Product retrieved successfully";
    public static final String MESSAGE_PRODUCT_CREATED = "Product created successfully";
    public static final String MESSAGE_PRODUCT_UPDATED = "Product updated successfully";

    public static final String MESSAGE_BRANDS_OK = "Brands retrieved successfully";
    public static final String MESSAGE_BRAND_OK = "Brand retrieved successfully";
    public static final String MESSAGE_BRAND_CREATED = "Brand created successfully";
    public static final String MESSAGE_BRAND_UPDATED = "Brand updated successfully";

    public static final String MESSAGE_CATEGORIES_OK = "Categories retrieved successfully";
    public static final String MESSAGE_CATEGORY_OK = "Category retrieved successfully";
    public static final String MESSAGE_CATEGORY_CREATED = "Category created successfully";
    public static final String MESSAGE_CATEGORY_UPDATED = "Brand updated successfully";

    public static final String MESSAGE_PROVIDERS_OK = "Providers retrieved successfully";
    public static final String MESSAGE_PROVIDER_OK = "Provider retrieved successfully";
    public static final String MESSAGE_PROVIDER_CREATED = "Provider created successfully";
    public static final String MESSAGE_PROVIDER_UPDATED = "Provider updated successfully";

    public static final String MESSAGE_STOCKTAKING_OK = "Stocktaking retrieved successfully";
    public static final String MESSAGE_STOCKTAKING_CREATED = "Stocktaking created successfully";
    public static final String MESSAGE_STOCKTAKING_UPDATED = "Stocktaking updated successfully";
    public static final String TEXT_STOCKTAKING_DATE = "stocktaking_date";
    public static final String MESSAGE_INVALID_DATE_FORMAT = "Invalid date format: ";
    public static final String MESSAGE_INVALID_DATE_RANGE = "Start date cannot be after end date";

    public static final String TEXT_SALE_DATE = "sale_date";
    public static final String TEXT_SALE_TOTAL_PRICE = "total_price";
}
