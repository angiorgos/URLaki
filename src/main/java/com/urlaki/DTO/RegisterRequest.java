package com.urlaki.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 100, message = "Password must be between 5 and 100 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[^A-Za-z0-9]).*$",
        message = "Password must contain at least one uppercase letter and one special character"
    )
    private String password;
}
