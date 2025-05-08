package pl.ewebpartner.shopdemo.model;

import java.util.ArrayList;
import java.util.List;

public class ProductList {
    private final List<Product> productList = new ArrayList<>();

    public void addProduct(Product product) {
        productList.add(product);
    }

    public List<Product> getProducts() {
        return productList;
    }
}
