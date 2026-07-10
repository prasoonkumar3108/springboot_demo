package com.company.employeemanagement.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String department;
    private String designation;
}
