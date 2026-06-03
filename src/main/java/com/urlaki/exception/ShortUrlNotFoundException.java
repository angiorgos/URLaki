package com.urlaki.exception;

public class ShortUrlNotFoundException extends RuntimeException {

    public ShortUrlNotFoundException(String shortURL) {
        super("Short URL not found: " + shortURL);
    }
}