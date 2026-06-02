package com.urlaki.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class URLResponse {

    private String shortURL;
    private String originalURL;
    private LocalDateTime expiresAt;
}