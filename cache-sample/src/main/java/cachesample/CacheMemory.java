package cachesample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMemory {

    private final Map<String, ProductDTO> cache = new ConcurrentHashMap<>();

    public ProductDTO get(String sku) {
        return cache.get(sku);
    }

    public void put(String sku, ProductDTO productDTO) {
        cache.put(sku, productDTO);
    }

}