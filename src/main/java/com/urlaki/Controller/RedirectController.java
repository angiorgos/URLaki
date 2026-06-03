package com.urlaki.Controller;

import com.urlaki.Service.RedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RedirectController {

    private final RedirectService redirectService;

    @GetMapping("/{shortURL}")
    public ResponseEntity<Void> redirectToOriginalURL(@PathVariable String shortURL) {
        String bigURL = redirectService.getBigURL(shortURL);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(bigURL)).build();
    }
}