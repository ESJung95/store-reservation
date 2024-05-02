package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Manager;
import com.eunsun.storereservation.domain.Store;
import com.eunsun.storereservation.dto.StoreDto;
import com.eunsun.storereservation.exception.ManagerNotFoundException;
import com.eunsun.storereservation.repository.ManagerRepository;
import com.eunsun.storereservation.repository.StoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;

    // 매장 정보 삭제
    public void deleteStore(Long storeId, Long loginManagerId) {
        Store store = storeRepository.findByIdAndManagerId(storeId, loginManagerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 수정할 권한이 없습니다."));

        storeRepository.deleteById(storeId);
    }
    // 매장 정보 수정
    public StoreDto modifyStoreInfo(Long storeId, StoreDto storeDto, Long loginManagerId) {
        Store store = storeRepository.findByIdAndManagerId(storeId, loginManagerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 수정할 권한이 없습니다."));

        store.setStoreName(storeDto.getStoreName());
        store.setLocation(storeDto.getLocation());
        store.setDescription(storeDto.getDescription());
        store.setStoreCallNumber(storeDto.getStoreCallNumber());

        Store updatedStore = storeRepository.save(store);
        return StoreDto.convertToDto(updatedStore);
    }

    // 매장 정보 저장
    @Transactional
    public StoreDto createStore(StoreDto storeDto, Long loggedInManagerId) {
        Manager manager = managerRepository.findById(loggedInManagerId)
                .orElseThrow(() -> new ManagerNotFoundException(loggedInManagerId + "에 해당하는 관리자를 찾을 수 없습니다."));

        Store store = Store.builder()
                .manager(manager)
                .storeName(storeDto.getStoreName())
                .location(storeDto.getLocation())
                .description(storeDto.getDescription())
                .storeCallNumber(storeDto.getStoreCallNumber())
                .build();

        Store savedStore = storeRepository.save(store);
        return StoreDto.convertToDto(savedStore);
    }

}
