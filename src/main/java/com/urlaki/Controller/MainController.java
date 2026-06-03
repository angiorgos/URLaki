package com.urlaki.Controller;

import com.urlaki.DTO.URLRequest;
import com.urlaki.DTO.URLResponse;
import com.urlaki.Service.ShortenerService;
import com.urlaki.model.Urls;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final ShortenerService shortenerService;

    @PostMapping("/request")
    public ResponseEntity<URLResponse> shortenURL(@Valid @RequestBody URLRequest request) {
        Urls url = shortenerService.URLShortener(request.getInputURL());
        URLResponse response = new URLResponse(
                url.getShortURL(),
                url.getBigURL(),
                url.getExpiresAt()
        );
        return ResponseEntity.ok(response);
    }
}
