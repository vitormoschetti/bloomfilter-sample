package cachesample;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ProductService {

    private final CacheMemory cache;
    private final BloomFilter<String> bloomFilter;
    private final ExternalServiceMock externalService;

    public ProductService() {

        //Simulate 10 thousand products
        this.bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 10_000, 0.01);
        this.cache = new CacheMemory();
        this.externalService = new ExternalServiceMock();

        //Populate the bloom filter with 10 thousand products
        for (int i = 0; i < 10_000; i++) {
            bloomFilter.put("SKU" + i);
        }

    }

    public ProductDTO findProduct(String sku) {

        //if the product is not in the bloom filter, throw an exception
        if(!bloomFilter.mightContain(sku)) {
            System.err.println("Product not found: " + sku + " in the bloom filter");
            throw new RuntimeException("Product not found");
        }

        //find the product in the cache
        final var productDTO = cache.get(sku);

        //if the cache hit, return it
        if(Objects.nonNull(productDTO)){
            System.out.println("Product found in the cache: " + productDTO);
            return productDTO;
        }

        System.out.println("Product not found in the cache, calling the external service...");

        //else the cache misses, call the external service and put it in the cache
        final var product = externalService.findProduct(sku);
        System.out.println("External service returned: " + product);
        cache.put(sku, product);
        System.out.println("Product added to the cache: " + product);

        //return the product
        return product;

    }
}
