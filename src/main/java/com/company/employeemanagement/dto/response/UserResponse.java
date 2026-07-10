package com.company.employeemanagement.dto.response;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private Set<String> roles;
    private ProfileResponse profile;
}
