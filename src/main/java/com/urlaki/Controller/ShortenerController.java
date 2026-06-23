package com.urlaki.Controller;

import com.urlaki.DTO.URLRequest;
import com.urlaki.DTO.URLResponse;
import com.urlaki.Service.ShortenerService;
import com.urlaki.Service.UserService;
import com.urlaki.model.User;
import com.urlaki.model.Urls;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShortenerController {

    private final ShortenerService shortenerService;
    private final UserService userService;

    @PostMapping("/request")
    public ResponseEntity<URLResponse> shortenURL(
            @Valid @RequestBody URLRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        User owner = userService.getUserByUsername(principal.getUsername());
        Urls url = shortenerService.URLShortener(request.getInputURL(), owner);

        return ResponseEntity.ok(new URLResponse(
                url.getShortUrl(), url.getBigUrl(), url.getExpiresAt()));
    }


    @GetMapping("/my-urls")
    public ResponseEntity<List<URLResponse>> myUrls(@AuthenticationPrincipal UserDetails principal) {
        User owner = userService.getUserByUsername(principal.getUsername());
        List<URLResponse> urls = shortenerService.getUrlsOf(owner).stream()
                .map(u -> new URLResponse(u.getShortUrl(), u.getBigUrl(), u.getExpiresAt()))
                .toList();
        return ResponseEntity.ok(urls);
    }
}
