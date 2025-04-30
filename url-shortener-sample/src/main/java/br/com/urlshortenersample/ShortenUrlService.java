package br.com.urlshortenersample;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static br.com.urlshortenersample.GenerateKeyUtil.loadExistingKeysIntoBloomFilter;

@Slf4j
@Service
public class ShortenUrlService {

    private final CacheMemory cache;
    private final BloomFilter<String> bloomFilter;

    public ShortenUrlService() {
        this.bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 3_906, 0.01);
        this.cache = new CacheMemory();

        //adicionando algumas chaves no bloom filter para testes de colisão
        loadExistingKeysIntoBloomFilter(bloomFilter, cache);

    }


    @SneakyThrows
    public String generateShortKey(String url) {

        String shortKey;
        int count = 0;
        final int limit = 20;
        do {
            //adicionando entropia na geração de hash
            final var salted = url + count;
            log.info("[generateShortKey] Generating key: {}", salted);
            shortKey = GenerateKeyUtil.generateShortKey(salted);
            log.info("[generateShortKey] Generated key: {}", shortKey);
            count++;
            if(count > limit) {
                throw new RuntimeException("Falha ao gerar chave após " + limit + " tentativas");
            }

        } while (bloomFilter.mightContain(shortKey));

        log.info("[generateShortKey] Adding key: {}", shortKey);
        bloomFilter.put(shortKey);
        cache.put(shortKey, url);

        return shortKey;

    }

    public String getOriginalUrl(String shortUrl) {

        if(bloomFilter.mightContain(shortUrl)) {
            return cache.get(shortUrl);
        }

        return null;
    }


}
