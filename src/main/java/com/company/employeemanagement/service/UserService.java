package com.company.employeemanagement.service;

import com.company.employeemanagement.dto.request.LoginRequest;
import com.company.employeemanagement.dto.request.RegisterRequest;
import com.company.employeemanagement.dto.response.JwtResponse;
import com.company.employeemanagement.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    JwtResponse login(LoginRequest request);

    UserResponse register(RegisterRequest request);

    Page<UserResponse> findAllUsers(Pageable pageable);

    UserResponse findById(Long id);

    UserResponse updateUser(Long id, RegisterRequest request);

    void deleteUser(Long id);

    UserResponse getCurrentUser(String username);
}
