package com.eunsun.storereservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDetailDto {

    private Long id;
    private String storeName;
    private String description;

}
