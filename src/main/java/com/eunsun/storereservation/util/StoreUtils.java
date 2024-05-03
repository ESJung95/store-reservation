package com.eunsun.storereservation.util;

import com.eunsun.storereservation.domain.Store;
import com.eunsun.storereservation.dto.StoreDetailDto;
import com.eunsun.storereservation.dto.StoreDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class StoreUtils {

    // store Entity -> store detail Dto : 상세 정보 확인
    public static StoreDetailDto convertToStoreDetailDto(Store store) {
        return StoreDetailDto.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .description(store.getDescription())
                .build();
    }


    // store Entity -> List<> : 매장 조회
    public static List<StoreDto> convertToDtoList(List<Store> stores) {
        return stores.stream()
                .map(StoreUtils::convertToDto)
                .collect(Collectors.toList());
    }

    // store Entity -> store Dto : 매장 정보 저장 , 수정
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