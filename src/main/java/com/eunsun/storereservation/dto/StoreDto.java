package com.eunsun.storereservation.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto implements Comparable<StoreDto>{

    private Long id;
    private Long managerId;
    private String storeName;
    private String location;
    private String description;
    private String storeCallNumber;

    @Override
    public int compareTo(StoreDto other) {
        return this.storeName.compareTo(other.storeName);
    }
}