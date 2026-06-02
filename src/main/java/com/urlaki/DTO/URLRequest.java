package com.urlaki.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class URLRequest {

    @NotBlank(message = "URL must not be empty")
    @Size(max = 2048, message = "URL is too long")
    @URL(message = "Malformed URL")
    @Pattern(regexp = "^(?i)(https?)://.*", message = "Only http/https URLs are allowed")
    private String inputURL;
}

