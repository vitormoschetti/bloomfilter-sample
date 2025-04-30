package cachesample;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        final var productService = new ProductService();
        final var scanner = new Scanner(System.in);

        System.out.println("Enter a SKU to get a product: (ex: SKU1, SKU99, SKU9999):");

        while (true) {
            System.out.println("> ");
            final var sku = scanner.nextLine();

            try {
                final var product = productService.findProduct(sku);
                System.out.println("Product: " + product);
            } catch (RuntimeException e) {
                System.err.println("Error: " + e.getMessage());
            }


        }


    }

}
