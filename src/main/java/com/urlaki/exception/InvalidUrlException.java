package com.urlaki.exception;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String inputUrl) {
        super("Invalid URL: " + inputUrl);
    }

    public InvalidUrlException(String inputUrl, Throwable cause) {
        super("Invalid URL: " + inputUrl, cause);
    }
}
