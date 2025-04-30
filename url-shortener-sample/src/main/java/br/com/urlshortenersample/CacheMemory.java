package br.com.urlshortenersample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMemory {

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String get(String shortUrl) {
        return cache.get(shortUrl);
    }

    public void put(String shortUrl, String originalUrl) {
        cache.put(shortUrl, originalUrl);
    }

}
