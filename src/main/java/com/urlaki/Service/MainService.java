package com.urlaki.Service;


import com.urlaki.Repository.UrlRepository;
import com.urlaki.exception.InvalidUrlException;
import com.urlaki.model.Urls;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
@Transactional
public class MainService {
    private final UrlRepository urlRepository;

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String URLShortener(String inputURL) {
        String canonicalURL = canonicalize(inputURL);
        BigInteger hashCode = hashFunction(canonicalURL);
        String encoded = base62Encode(hashCode);

        String URLCode = encoded.substring(0, 8);

        Urls url = Urls.builder().bigURL(inputURL).shortURL(URLCode).build();
        urlRepository.save(url);

        return URLCode;
    }


    public BigInteger hashFunction(String canonicalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(canonicalUrl.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }


    public String base62Encode(BigInteger number) {
        if (number.signum() == 0) {
            return "0";
        }
        BigInteger base = BigInteger.valueOf(62);
        StringBuilder sb = new StringBuilder();
        while (number.signum() > 0) {
            BigInteger[] divMod = number.divideAndRemainder(base);
            sb.append(BASE62.charAt(divMod[1].intValue()));
            number = divMod[0];
        }
        return sb.reverse().toString();
    }

    public String canonicalize(String inputURL) {
        try {
            URI uri = new URI(inputURL);
            String host = uri.getHost();
            String lowerHost = host == null ? null : host.toLowerCase();

            URI canonical = new URI(
                    uri.getScheme() == null ? null : uri.getScheme().toLowerCase(),
                    uri.getUserInfo(),
                    lowerHost,
                    uri.getPort(),
                    uri.getPath(),     // case-sensitive, μένει ως έχει
                    uri.getQuery(),    // μένει ως έχει
                    uri.getFragment()
            );

            return canonical.toString();
        } catch (URISyntaxException e) {
            throw new InvalidUrlException(inputURL, e);
        }
    }
}
