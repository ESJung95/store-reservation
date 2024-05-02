package com.eunsun.storereservation.dto;

import com.eunsun.storereservation.domain.Store;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {

    private Long id;
    private Long manager;
    private String storeName;
    private String location;
    private String description;
    private String storeCallNumber;

    public static StoreDto convertToDto(Store store) {
        if (store.getManager() == null) {
            log.warn("Store의 Manager == null");
            return null;
        }

        return StoreDto.builder()
                .id(store.getId())
                .manager(store.getManager().getId())
                .storeName(store.getStoreName())
                .location(store.getLocation())
                .description(store.getDescription())
                .storeCallNumber(store.getStoreCallNumber())
                .build();
    }
}