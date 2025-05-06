package pl.ewebpartner.shopdemo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductListService {
    private final ProductList productList;

    public ProductListService(ProductList productList) {
        this.productList = productList;
    }


    public void initProducts() {
        Random random = new Random();
        for (int i = 1; i <= 5; i++) {
            String name = "Produkt " + i;
            BigDecimal price = BigDecimal.valueOf(50 + random.nextInt(251));
            Product product = new Product(name, price);
            productList.addProduct(product);
        }
    }

    public List<Product> getProducts() {
        return productList.getProducts();
    }
}
