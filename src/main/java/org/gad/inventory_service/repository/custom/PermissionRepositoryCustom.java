package org.gad.inventory_service.repository.custom;

import org.gad.inventory_service.model.Permission;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface PermissionRepositoryCustom {
    Flux<Permission> findByNamesLikeIgnoreCase(Set<String> names);
}
