package pl.ewebpartner.shopdemo.service;

import org.springframework.stereotype.Service;
import pl.ewebpartner.shopdemo.model.Product;
import pl.ewebpartner.shopdemo.model.ProductList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class ProductListService {
    private final ProductList productList = new ProductList();

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
