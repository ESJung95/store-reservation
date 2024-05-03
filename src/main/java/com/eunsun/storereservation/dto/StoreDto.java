package com.eunsun.storereservation.dto;

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

}