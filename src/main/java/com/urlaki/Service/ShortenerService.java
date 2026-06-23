package com.urlaki.Service;


import com.urlaki.Repository.UrlRepository;
import com.urlaki.exception.InvalidUrlException;
import com.urlaki.model.Urls;
import com.urlaki.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class ShortenerService {
    private final UrlRepository urlRepository;

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final long EXPIRATION_DAYS = 30;

    private static final Pattern DOMAIN =
            Pattern.compile("^(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}$");

    private static final Pattern IPV4 =
            Pattern.compile("^(?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$");

    public Urls URLShortener(String inputURL, User owner) {
        String canonicalURL = canonicalize(inputURL);

        return urlRepository.findByOwnerAndBigUrl(owner, canonicalURL)   // ← per-user lookup
                .orElseGet(() -> {
                    //unique hash per user on a link
                    BigInteger hashCode = hashFunction(canonicalURL + ":" + owner.getId());
                    String encoded = base62Encode(hashCode);
                    String URLCode = encoded.substring(0, 8);

                    Urls url = Urls.builder()
                            .bigUrl(canonicalURL)
                            .shortUrl(URLCode)
                            .owner(owner)
                            .expiresAt(LocalDateTime.now().plusDays(EXPIRATION_DAYS))
                            .build();
                    return urlRepository.save(url);
                });
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

    private boolean isValidHost(String host) {
        return DOMAIN.matcher(host).matches() || IPV4.matcher(host).matches();
    }

    public String canonicalize(String inputURL) {
        try {
            URI uri = new URI(inputURL);
            String host = uri.getHost();
            if (host == null || !isValidHost(host)) {
                throw new InvalidUrlException(inputURL);
            }
            String lowerHost = host.toLowerCase();

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
