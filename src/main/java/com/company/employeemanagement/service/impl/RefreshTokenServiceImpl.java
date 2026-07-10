package com.company.employeemanagement.service.impl;

import com.company.employeemanagement.entity.RefreshToken;
import com.company.employeemanagement.entity.User;
import com.company.employeemanagement.exception.ResourceNotFoundException;
import com.company.employeemanagement.repository.RefreshTokenRepository;
import com.company.employeemanagement.service.RefreshTokenService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 7;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.findByUser(user)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(
                Instant.now().plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {

        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Refresh token not found"));
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {

            refreshTokenRepository.delete(token);

            throw new ResourceNotFoundException("Refresh token expired");
        }

        return token;
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {

        refreshTokenRepository.deleteByUser(user);
    }
}