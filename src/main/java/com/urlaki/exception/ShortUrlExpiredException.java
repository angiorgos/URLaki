package com.urlaki.exception;

public class ShortUrlExpiredException extends RuntimeException {

    public ShortUrlExpiredException(String shortURL) {
        super("Short URL expired: " + shortURL);
    }
}