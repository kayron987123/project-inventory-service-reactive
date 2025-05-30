package org.gad.inventory_service.service;

import org.gad.inventory_service.dto.StocktakingDTO;
import org.gad.inventory_service.dto.request.CreateStocktakingRequest;
import org.gad.inventory_service.dto.request.UpdateStocktakingRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface StocktakingService {
    Flux<StocktakingDTO> findAllStocktaking();
    Flux<StocktakingDTO> findAllStocktakingByDateBetween(String dateStart, String dateEnd);
    Flux<StocktakingDTO> findAllStocktakingByProductName(String productName);
    Mono<StocktakingDTO> findStocktakingById(String id);
    Mono<StocktakingDTO> createStocktaking(CreateStocktakingRequest createStocktakingDTO);
    Mono<StocktakingDTO> updateStocktaking(String id, UpdateStocktakingRequest updateStocktakingRequest);
    Mono<Void> deleteStocktakingById(String id);
}
