package com.company.employeemanagement.service;

import com.company.employeemanagement.entity.RefreshToken;
import com.company.employeemanagement.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshToken findByToken(String token);

    void deleteByUser(User user);
}