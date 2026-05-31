package com.urlaki.Controller;

import com.urlaki.DTO.URLRequest;
import com.urlaki.Service.MainService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final MainService mainService;

    @PostMapping("/request")
    public ResponseEntity<String> shortenURL(@RequestBody URLRequest request) {
        return ResponseEntity.ok().body(mainService.URLShortener(request.getInputURL()));
    }
}
