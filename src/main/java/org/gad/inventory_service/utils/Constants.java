package org.gad.inventory_service.utils;

public class Constants {
    private Constants() {
    }

    public static final String BRAND_NOT_FOUND_FLUX = "No brands found";
    public static final String BRAND_NOT_FOUND_UUID = "Brand not found with uuid: ";
    public static final String BRAND_NOT_FOUND_NAME = "Brand not found with name: ";
    public static final String ERROR_SEARCHING_BRAND = "Error when searching for brand: {}";
    public static final String ERROR_SEARCHING_BRANDS = "Error when searching for brands: {}";
    public static final String ERROR_SAVING_BRAND = "Error when saving brand: {}";
    public static final String ERROR_UPDATING_BRAND = "Error when updating brand: {}";
    public static final String ERROR_DELETING_BRAND = "Error when deleting brand: {}";
    public static final String REGEX_PRODUCTS = "^[a-zA-Z]*$";
    public static final String REGEX_UUID = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    public static final String MESSAGE_PARAMETER_PRODUCT_NAME = "Parameter name only accepts letters";
    public static final String MESSAGE_PARAMETER_CATEGORY = "Parameter category only accepts letters";
    public static final String MESSAGE_PARAMETER_BRAND = "Parameter brand only accepts letters";
    public static final String MESSAGE_PARAMETER_PROVIDER = "Parameter provider only accepts letters";
    public static final String MESSAGE_INCORRECT_UUID_FORMAT = "Incorrect UUID format";

    public static final String MESSAGE_PRODUCTS_OK = "Products retrieved successfully";
    public static final String MESSAGE_PRODUCT_OK = "Product retrieved successfully";
    public static final String MESSAGE_PRODUCT_CREATED = "Product created successfully";
    public static final String MESSAGE_PRODUCT_UPDATED = "Product updated successfully";

    public static final String MESSAGE_BRANDS_OK = "Brands retrieved successfully";
    public static final String MESSAGE_BRAND_OK = "Brand retrieved successfully";
    public static final String MESSAGE_BRAND_CREATED = "Brand created successfully";
    public static final String MESSAGE_BRAND_UPDATED = "Brand updated successfully";
}
