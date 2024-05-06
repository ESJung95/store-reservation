package com.eunsun.storereservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCreateDto {

    private int rating;
    private String content;

}