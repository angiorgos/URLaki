package com.urlaki.Service;

import com.urlaki.Repository.UrlRepository;
import com.urlaki.exception.InvalidUrlException;
import com.urlaki.model.Urls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainServiceTest {

    private MainService mainService;

    private UrlRepository urlRepository;

    @BeforeEach
    public void setUp() {
        urlRepository = mock(UrlRepository.class);
        mainService = new MainService(urlRepository);

        // Νέο URL: δεν υπάρχει στη βάση, και το save γυρνάει πίσω ό,τι του δώσουμε
        when(urlRepository.findByBigURL(any())).thenReturn(Optional.empty());
        when(urlRepository.save(any(Urls.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void testURLShortener_ShouldReturnValidShortURL() {
        String longURL = "https://www.example.com/some/very/long/url";

        Urls result = mainService.URLShortener(longURL);

        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getShortURL(), "Short URL should not be null");
        assertEquals(8, result.getShortURL().length(), "Short URL code should be 8 chars");
        assertEquals(longURL, result.getBigURL(), "Canonical URL should match the input");
        assertNotNull(result.getExpiresAt(), "Expiration should be set");
    }

    @Test
    public void testURLShortener_ShouldReturnExistingWhenAlreadyShortened() {
        String longURL = "https://google.com";
        Urls existing = Urls.builder()
                .bigURL("https://google.com")
                .shortURL("abc12345")
                .build();
        when(urlRepository.findByBigURL("https://google.com")).thenReturn(Optional.of(existing));

        Urls result = mainService.URLShortener(longURL);

        assertEquals("abc12345", result.getShortURL(),
                "Should return the already stored short URL");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://f",            // single-label host
            "https://localhost",    // χωρίς TLD
            "https://example.",     // trailing dot, κενό TLD
            "https://-bad.com"      // label ξεκινά με παύλα
    })
    public void testURLShortener_ShouldRejectInvalidHost(String badURL) {
        assertThrows(InvalidUrlException.class, () -> mainService.URLShortener(badURL),
                "Should reject URL with invalid host: " + badURL);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://example.com",
            "https://www.sub.domain.co.uk/path?q=1",
            "https://93.184.216.34/page"
    })
    public void testURLShortener_ShouldAcceptValidHost(String goodURL) {
        Urls result = mainService.URLShortener(goodURL);
        assertNotNull(result.getShortURL());
    }
}