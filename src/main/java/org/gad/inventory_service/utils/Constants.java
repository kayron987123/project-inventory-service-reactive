package org.gad.inventory_service.utils;


public class Constants {
    private Constants() {
    }

    public static final String[] REPORT_HEADERS = {
            "ID Sale",
            "Product",
            "Amount",
            "Total Price",
            "DateTime"
    };
    public static final String SHEET_NAME = "Sales Report";
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TEXT_AND = " and ";
    public static final String MESSAGE_VALIDATION_INCORRECT = "Validation incorrect";
    public static final String CONTENT_DISPOSITION_EXCEL = "attachment; filename=sales_report.xlsx";

    public static final String DEFAULT_ROLE_FOR_NEW_USER = "ROLE_BASIC_USER";
    public static final String PREFIX_ROLE = "ROLE_";
    public static final String MESSAGE_ID_CANNOT_BE_EMPTY = "Id cannot be empty";

    public static final String MESSAGE_NAME_PRODUCT_CANNOT_BE_EMPTY = "Product name cannot be empty";
    public static final String MESSAGE_NAME_BRAND_CANNOT_BE_EMPTY = "Brand name cannot be empty";
    public static final String MESSAGE_NAME_CATEGORY_CANNOT_BE_EMPTY = "Category name cannot be empty";
    public static final String MESSAGE_NAME_PROVIDER_CANNOT_BE_EMPTY = "Provider name cannot be empty";

    public static final String PRODUCT_NOT_FOUND_FLUX_CRITERIA = "No products found with the given criteria";
    public static final String PRODUCT_NOT_FOUND_FLUX = "No products found";
    public static final String PRODUCT_NOT_FOUND_NAME = "Product not found with name : ";
    public static final String PRODUCT_NOT_FOUND_ID = "Product not found with id: ";
    public static final String ERROR_SEARCHING_PRODUCT = "Error when searching for product: {}";
    public static final String ERROR_CREATING_PRODUCTS = "Error when creating product: {}";
    public static final String ERROR_UPDATING_PRODUCT = "Error when updating product: {}";
    public static final String ERROR_DELETING_PRODUCT = "Error when deleting product: {}";

    public static final String BRAND_NOT_FOUND_FLUX = "No brands found";
    public static final String BRAND_NOT_FOUND_ID = "Brand not found with id: ";
    public static final String BRAND_NOT_FOUND_NAME = "Brand not found with name: ";
    public static final String ERROR_SEARCHING_BRAND = "Error when searching for brand: {}";
    public static final String ERROR_SEARCHING_BRANDS = "Error when searching for brands: {}";
    public static final String ERROR_SAVING_BRAND = "Error when saving brand: {}";
    public static final String ERROR_UPDATING_BRAND = "Error when updating brand: {}";
    public static final String ERROR_DELETING_BRAND = "Error when deleting brand: {}";

    public static final String CATEGORY_NOT_FOUND_FLUX = "No categories found";
    public static final String CATEGORY_NOT_FOUND_ID = "Category not found with id: ";
    public static final String CATEGORY_NOT_FOUND_NAME = "Category not found with name: ";
    public static final String CATEGORY_SEARCHING_BRAND = "Error when searching for category: {}";
    public static final String CATEGORY_SEARCHING_BRANDS = "Error when searching for categories: {}";
    public static final String CATEGORY_SAVING_BRAND = "Error when saving category: {}";
    public static final String CATEGORY_UPDATING_BRAND = "Error when updating category: {}";
    public static final String CATEGORY_DELETING_BRAND = "Error when deleting category: {}";

    public static final String PROVIDER_NOT_FOUND_FLUX = "No providers found";
    public static final String PROVIDER_NOT_FOUND_ID = "Provider not found with id: ";
    public static final String PROVIDER_NOT_FOUND_NAME = "Provider not found with name : ";
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
    public static final String STOCKTAKING_NOT_FOUND_ID = "Stocktaking not found with id: ";
    public static final String STOCKTAKING_NOT_FOUND_BETWEEEN_DATES = "No stocktaking found between the given dates: ";
    public static final String STOCKTAKING_NOT_FOUND_NAME = "Stocktaking not found with name: ";
    public static final String ERROR_SEARCHING_STOCKTAKING = "Error when searching for stocktaking: {}";
    public static final String ERROR_CREATING_STOCKTAKING = "Error when creating stocktaking: {}";
    public static final String ERROR_UPDATING_STOCKTAKING = "Error when updating stocktaking: {}";
    public static final String ERROR_DELETING_STOCKTAKING = "Error when deleting stocktaking: {}";

    public static final String SALE_NOT_FOUND_FLUX = "No sales found";
    public static final String SALE_NOT_FOUND_ID = "Sale not found with id: ";
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

    public static final String MESSAGE_USERS_NOT_FOUND_FLUX = "No users found";
    public static final String USER_NOT_FOUND_ID = "User not found with id: ";
    public static final String USER_NOT_FOUND_USERNAME = "User not found with username: ";
    public static final String USER_NOT_FOUND_NAME = "User not found with name: ";
    public static final String OR_EMAIL = " or email: ";
    public static final String AND_LAST_NAME = " and last name: ";
    public static final String DEFAULT_ROLE_NOT_FOUND = "Default role not found: ";
    public static final String ERROR_SEARCHING_USERS = "Error searching users: {}";
    public static final String ERROR_SEARCHING_USER_BY_USERNAME_EMAIL = "Error finding user by username or email: {}";
    public static final String ERROR_SEARCHING_USERS_BY_NAME_LAST_NAME = "Error finding users by name or last name: {}";
    public static final String ERROR_FINDING_USER_BY_ID = "Error finding user by id: {}";
    public static final String ERROR_CREATING_USER = "Error creating user: {}";
    public static final String ERROR_UPDATING_USER = "Error updating user: {}";
    public static final String ERROR_DELETING_USER = "Error deleting user: {}";

    public static final String ROLES_NOT_FOUND = "Roles not found";
    public static final String ROLE_NOT_FOUND_ID = "Role not found with id: ";
    public static final String ERROR_MESSAGE_FINDING_ROLES = "Error finding all roles: {}";
    public static final String ERROR_MESSAGE_ROLE_NOT_FOUND_ID = "Error when finding role with id {}: {}";
    public static final String ERROR_CREATING_ROLE = "Error creating role: {}";
    public static final String ERROR_UPDATING_ROLE_BY_ID = "Error updating role with id {}: {}";
    public static final String ERROR_DELETING_ROLE_BY_ID = "Error deleting role with id {}: {}";

    public static final String PERMISSION_NOT_FOUND_FLUX = "Permissions not found";
    public static final String PERMISSION_NOT_FOUND_ID = "Permission not found with id: ";
    public static final String ERROR_SEARCHING_PERMISSIONS = "Error searching permissions: {}";
    public static final String ERRORS_SEARCHING_PERMISSION_BY_ID = "Error searching permission with id {}: {}";
    public static final String ERROR_CREATING_PERMISSION = "Error creating permission: {}";
    public static final String ERROR_UPDATING_PERMISSION = "Error updating permission with id {}: {}";
    public static final String ERROR_DELETING_PERMISSION = "Error deleting permission with id {}: {}";

    public static final String REGEX_ONLY_TEXT = "^[a-zA-Z\\s]*$";
    public static final String REGEX_ID = "^[a-fA-F0-9]{24}$";
    public static final String REGEX_RUC = "^\\d{11}$";
    public static final String REGEX_DNI = "^\\d{8}$";
    public static final String REGEX_ONLY_NUMBERS = "^\\d+$";
    public static final String REGEX_DATE_OR_TIME = "^\\d{4}-\\d{2}-\\d{2}(?:[ T]\\d{2}:\\d{2}:\\d{2})?$";
    public static final String REGEX_ONLY_TEST_AND_NUMBERS = "^[a-zA-Z0-9]+$";
    public static final String MESSAGE_INVALID_DATE_OR_FORMAT = "Invalid date format. It must be YYYY-MM-DD O YYY-MM-DD HH: mm: SS";
    public static final String MESSAGE_ONLY_NUMBERS = "Only numbers are accepted";
    public static final String MESSAGE_ONLY_TEST_AND_NUMBERS = "Only letters and numbers are accepted";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String MESSAGE_PARAMETER_LAST_NAME = "Parameter Last Name only accepts letters";

    public static final String PRODUCT_URI = "/api/v1/products/%s";
    public static final String BRAND_URI = "/api/v1/brands/%s";
    public static final String CATEGORY_URI = "/api/v1/categories/%s";
    public static final String PROVIDER_URI = "/api/v1/providers/%s";
    public static final String STOCKTAKING_URI = "/api/v1/stocktaking/%s";
    public static final String SALE_URI = "/api/v1/sales/%s";
    public static final String USER_URI = "/api/v1/users/%s";
    public static final String ROLE_URI = "/api/v1/roles/%s";
    public static final String PERMISSION_URI = "/api/v1/permissions/%s";

    public static final String BRAND_URL = "/api/v1/brands/**";
    public static final String CATEGORY_URL = "/api/v1/categories/**";
    public static final String PRODUCT_URL = "/api/v1/products/**";
    public static final String PROVIDER_URL = "/api/v1/providers/**";
    public static final String SALE_URL = "/api/v1/sales/**";
    public static final String STOCKTAKING_URL = "/api/v1/stocktaking/**";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_INVENTORY_MANAGER = "INVENTORY_MANAGER";
    public static final String ROLE_SALESPERSON = "SALESPERSON";
    public static final String ROLE_WAREHOUSE_STAFF = "WAREHOUSE_STAFF";
    public static final String ROLE_ANALYST = "ANALYST";
    public static final String ROLE_SUPPORT = "SUPPORT";
    public static final String ROLE_BASIC_USER = "BASIC_USER";

    public static final String MESSAGE_PARAMETER_NAME = "Parameter name only accepts letters";
    public static final String MESSAGE_PARAMETER_CATEGORY = "Parameter category only accepts letters";
    public static final String MESSAGE_PARAMETER_BRAND = "Parameter brand only accepts letters";
    public static final String MESSAGE_PARAMETER_PROVIDER = "Parameter provider only accepts letters";
    public static final String MESSAGE_INCORRECT_ID_FORMAT = "ID format is incorrect, it should be a 24-character hexadecimal string";
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
    public static final String MESSAGE_CATEGORY_UPDATED = "Category updated successfully";

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

    public static final String MESSAGE_SALE_OK = "Sale retrieved successfully";
    public static final String MESSAGE_SALES_OK = "Sales retrieved successfully";
    public static final String MESSAGE_SALE_CREATED = "Sale created successfully";
    public static final String MESSAGE_SALE_UPDATED = "Sale updated successfully";

    public static final String MESSAGE_USERS_OK = "Users retrieved successfully";
    public static final String MESSAGE_USER_OK = "User retrieved successfully";
    public static final String MESSAGE_USER_CREATED = "User created successfully";
    public static final String MESSAGE_USER_UPDATED = "User updated successfully";

    public static final String MESSAGE_ROLES_OK = "Roles retrieved successfully";
    public static final String MESSAGE_ROLE_OK = "Role retrieved successfully";
    public static final String MESSAGE_ROLE_CREATED = "Role created successfully";
    public static final String MESSAGE_ROLE_UPDATED = "Role updated successfully";

    public static final String MESSAGE_PERMISSIONS_OK = "Permissions retrieved successfully";
    public static final String MESSAGE_PERMISSION_OK = "Permission retrieved successfully";
    public static final String MESSAGE_PERMISSION_CREATED = "Permission created successfully";
    public static final String MESSAGE_PERMISSION_UPDATED = "Permission updated successfully";

    public static final String TEXT_SALE_DATE = "sale_date";
    public static final String TEXT_SALE_TOTAL_PRICE = "total_price";
}
