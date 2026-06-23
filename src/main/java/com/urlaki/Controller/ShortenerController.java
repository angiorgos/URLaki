package com.urlaki.Controller;

import com.urlaki.DTO.URLRequest;
import com.urlaki.DTO.URLResponse;
import com.urlaki.Service.ShortenerService;
import com.urlaki.model.Urls;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.urlaki.Repository.UserRepository;
import com.urlaki.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShortenerController {

    private final ShortenerService shortenerService;
    private final UserRepository userRepository;

    @PostMapping("/request")
    public ResponseEntity<URLResponse> shortenURL(@Valid @RequestBody URLRequest request, Principal principal) {
        User owner = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Urls url = shortenerService.URLShortener(request.getInputURL(), owner);
        URLResponse response = new URLResponse(
                url.getShortUrl(),
                url.getBigUrl(),
                url.getExpiresAt()
        );
        return ResponseEntity.ok(response);
    }
}
