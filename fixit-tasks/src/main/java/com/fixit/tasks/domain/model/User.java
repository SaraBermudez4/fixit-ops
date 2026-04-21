package com.fixit.tasks.domain.model;

import com.fixit.tasks.domain.enums.Role;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    String dni;
    String name;
    String lastName;
    String email;
    String password;
    String phoneNumber;
    Role role;
}
