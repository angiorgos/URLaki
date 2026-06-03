package com.urlaki.Service;


import com.urlaki.Repository.UrlRepository;
import com.urlaki.exception.ShortUrlExpiredException;
import com.urlaki.exception.ShortUrlNotFoundException;
import com.urlaki.model.Urls;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final UrlRepository urlRepository;
    public String getBigURL(String shortURL){
        Urls url = urlRepository.findByShortURL(shortURL).orElseThrow(() -> new ShortUrlNotFoundException(shortURL));

        if (url.getExpiresAt().isBefore(java.time.LocalDateTime.now())){
            throw new ShortUrlExpiredException(shortURL);
        }
        return url.getBigURL();
    }

}
