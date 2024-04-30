package com.eunsun.storereservation.service;

import com.eunsun.storereservation.domain.Customer;
import com.eunsun.storereservation.dto.AuthDto;
import com.eunsun.storereservation.exception.EmailDuplicateException;
import com.eunsun.storereservation.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerCustomer(AuthDto.signUp request) {

        // 가입 이메일 중복 검사
        if(customerRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplicateException();
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Customer 객체 생성
        Customer customer = Customer.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .phone(request.getPhone())
                .build();

        Customer saveCustomer = customerRepository.save(customer);

        log.info("고객의 회원가입 정보를 성공적으로 저장했습니다.");
        return saveCustomer.getEmail();


    }
}
