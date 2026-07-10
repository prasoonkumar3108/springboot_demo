package com.company.employeemanagement.service.impl;

import com.company.employeemanagement.dto.request.LoginRequest;
import com.company.employeemanagement.dto.request.RegisterRequest;
import com.company.employeemanagement.dto.response.JwtResponse;
import com.company.employeemanagement.dto.response.UserResponse;
import com.company.employeemanagement.entity.Profile;
import com.company.employeemanagement.entity.Role;
import com.company.employeemanagement.entity.User;
import com.company.employeemanagement.exception.DuplicateUserException;
import com.company.employeemanagement.exception.ResourceNotFoundException;
import com.company.employeemanagement.mapper.UserMapper;
import com.company.employeemanagement.repository.RoleRepository;
import com.company.employeemanagement.repository.UserRepository;
import com.company.employeemanagement.security.JwtUtil;
import com.company.employeemanagement.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities().stream().toList();
        String token = jwtUtil.generateToken(authentication.getName(), authorities);
       return new JwtResponse(token, "Bearer");
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUserException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDepartment(request.getDepartment());
        profile.setDesignation(request.getDesignation());
        user.setProfile(profile);

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        user.setRoles(new HashSet<>(Set.of(userRole)));

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, RegisterRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.getProfile().setFirstName(request.getFirstName());
        user.getProfile().setLastName(request.getLastName());
        user.getProfile().setDepartment(request.getDepartment());
        user.getProfile().setDesignation(request.getDesignation());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }
}
