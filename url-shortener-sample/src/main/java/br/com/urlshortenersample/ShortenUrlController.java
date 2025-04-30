package br.com.urlshortenersample;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@AllArgsConstructor
public class ShortenUrlController {

    private final ShortenUrlService service;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenKeyOutput> shortenUrl(@RequestParam("url") String url) {

        final var shortKey = service.generateShortKey(url);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ShortenKeyOutput(shortKey));

    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {

        final var originalUrl = service.getOriginalUrl(shortUrl);

        if (Objects.isNull(originalUrl)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", originalUrl)
                .build();
    }

}
