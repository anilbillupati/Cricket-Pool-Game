package com.CricketBetting.Cricket.domain.request;

import com.CricketBetting.Cricket.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "Full name is mandatory")
    private String fullName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Email must end with @gmail.com")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private Role role;
}