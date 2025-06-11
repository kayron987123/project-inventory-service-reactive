package org.gad.inventory_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.gad.inventory_service.dto.request.CreateProviderRequest;
import org.gad.inventory_service.dto.request.UpdateProviderRequest;
import org.gad.inventory_service.dto.response.DataResponse;
import org.gad.inventory_service.service.ProviderService;
import org.gad.inventory_service.utils.Constants;
import org.gad.inventory_service.utils.UtilsMethods;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.gad.inventory_service.utils.Constants.*;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/providers")
@RestController
public class ProviderController {
    private final ProviderService providerService;

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> findProviderById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                               @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return providerService.findProviderById(id)
                .map(provider -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PROVIDER_OK)
                                .data(provider)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/ruc/{ruc}")
    public Mono<ResponseEntity<DataResponse>> findProviderByRuc(@PathVariable @NotBlank(message = Constants.MESSAGE_RUC_CANNOT_BE_EMPTY)
                                                                @Pattern(regexp = REGEX_RUC, message = MESSAGE_RUC_INVALID) String ruc) {
        return providerService.findProviderByRuc(ruc)
                .map(provider -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PROVIDER_OK)
                                .data(provider)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/name/{name}")
    public Mono<ResponseEntity<DataResponse>> findProviderByName(@PathVariable @Pattern(regexp = REGEX_ONLY_TEXT, message = MESSAGE_PARAMETER_NAME)
                                                                 @NotBlank(message = Constants.MESSAGE_NAME_CANNOT_BE_EMPTY) String name) {
        return providerService.findProviderByName(name)
                .map(provider -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PROVIDER_OK)
                                .data(provider)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<DataResponse>> findProviderByEmail(@PathVariable @NotBlank(message = Constants.MESSAGE_EMAIL_CANNOT_BE_EMPTY)
                                                                  @Email(message = MESSAGE_EMAIL_INVALID) String email) {
        return providerService.findProviderByEmail(email)
                .map(provider -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PROVIDER_OK)
                                .data(provider)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping("/dni/{dni}")
    public Mono<ResponseEntity<DataResponse>> findProviderByDni(@PathVariable @NotBlank(message = Constants.MESSAGE_DNI_CANNOT_BE_EMPTY)
                                                                @Pattern(regexp = REGEX_DNI, message = MESSAGE_DNI_INVALID) String dni) {
        return providerService.findProviderByDni(dni)
                .map(provider -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PROVIDER_OK)
                                .data(provider)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findAllProviders() {
        return providerService.findAllProviders()
                .collectList()
                .map(providers -> ResponseEntity.ok(
                        DataResponse.builder()
                                .status(HttpStatus.OK.value())
                                .message(MESSAGE_PROVIDERS_OK)
                                .data(providers)
                                .timestamp(UtilsMethods.datetimeNowFormatted())
                                .build())
                );
    }

    @PostMapping
    public Mono<ResponseEntity<DataResponse>> createProvider(@RequestBody @Valid CreateProviderRequest request) {
        return providerService.saveProvider(request)
                .map(provider -> {
                    URI location = UtilsMethods.createUri(PROVIDER_URI, provider.idProvider());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_PROVIDER_CREATED)
                            .data(provider)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> updateProvider(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                             @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id,
                                                             @RequestBody @Valid UpdateProviderRequest request) {
        return providerService.updateProvider(id, request)
                .map(provider -> {
                    URI location = UtilsMethods.createUri(PROVIDER_URI, provider.idProvider());
                    DataResponse dataResponse = DataResponse.builder()
                            .status(HttpStatus.CREATED.value())
                            .message(MESSAGE_PROVIDER_UPDATED)
                            .data(provider)
                            .timestamp(UtilsMethods.datetimeNowFormatted())
                            .build();
                    return ResponseEntity.created(location).body(dataResponse);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<DataResponse>> deleteProviderById(@PathVariable @Pattern(regexp = REGEX_ID, message = MESSAGE_INCORRECT_ID_FORMAT)
                                                                 @NotBlank(message = MESSAGE_ID_CANNOT_BE_EMPTY) String id) {
        return providerService.deleteProviderById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
