package com.urlaki.Service;

import com.urlaki.Repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MainServiceTest {

    private MainService mainService;

    private UrlRepository urlRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        urlRepository = mock(UrlRepository.class);
        mainService = new MainService(urlRepository);
    }

    @Test
    public void testURLShortener_ShouldReturnValidShortURL() {
        // Arrange
        String longURL = "https://www.example.com/some/very/long/url";
        System.out.println("\n========== TEST 1: Basic URL Shortening ==========");
        System.out.println("Input:  " + longURL);

        // Act
        String result = mainService.URLShortener(longURL);
        System.out.println("Output: " + result);

        // Assert
        assertNotNull(result, "Short URL should not be null");
        System.out.println("✅ Result is not null");

        assertTrue(result.startsWith("https://urlaki.com/"),
                   "Short URL should start with https://urlaki.com/");
        System.out.println("✅ Starts with https://urlaki.com/");

        assertTrue(result.contains(longURL),
                   "Short URL should contain the input URL");
        System.out.println("✅ Contains original URL");
        System.out.println("========== TEST 1 PASSED! ==========\n");
    }

    @Test
    public void testURLShortener_WithDifferentURLs() {
        System.out.println("\n========== TEST 2: Multiple URLs ==========");
        String[] urls = {
            "https://google.com",
            "https://github.com/user/repo",
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        };

        for (int i = 0; i < urls.length; i++) {
            System.out.println("\nTest " + (i+1) + ":");
            System.out.println("  Input:  " + urls[i]);

            String result = mainService.URLShortener(urls[i]);
            System.out.println("  Output: " + result);

            assertTrue(result.startsWith("https://urlaki.com/"));
            System.out.println("  ✅ Valid short URL format");
        }
        System.out.println("\n========== TEST 2 PASSED! ==========\n");
    }
}

