package cachesample;

public class ExternalServiceMock {

    public ProductDTO findProduct(String sku) {

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new ProductDTO(sku, "Product " + sku);
    }

}
