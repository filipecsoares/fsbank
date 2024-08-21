package com.fcs.fsbank.controller;

import com.fcs.fsbank.model.Customer;
import com.fcs.fsbank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            Customer savedCustomer = customerRepository.save(customer);
            if(savedCustomer.getId()>0) {
                return ResponseEntity.status(CREATED).body("Given user details are successfully registered");
            } else {
                return ResponseEntity.status(BAD_REQUEST).body("User registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An exception occurred: " + ex.getMessage());
        }
    }
}
