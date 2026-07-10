package com.company.employeemanagement.mapper;

import com.company.employeemanagement.dto.response.ProfileResponse;
import com.company.employeemanagement.dto.response.UserResponse;
import com.company.employeemanagement.entity.Profile;
import com.company.employeemanagement.entity.Role;
import com.company.employeemanagement.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setEnabled(user.isEnabled());

        if (user.getRoles() != null) {
            Set<String> roles = user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            response.setRoles(roles);
        }

        if (user.getProfile() != null) {

            Profile profile = user.getProfile();

            ProfileResponse profileResponse = new ProfileResponse();

            profileResponse.setId(profile.getId());
            profileResponse.setFirstName(profile.getFirstName());
            profileResponse.setLastName(profile.getLastName());
            profileResponse.setDepartment(profile.getDepartment());
            profileResponse.setDesignation(profile.getDesignation());

            response.setProfile(profileResponse);
        }

        return response;
    }
}