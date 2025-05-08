package pl.ewebpartner.shopdemo.model;

import java.math.BigDecimal;

public class Product {
    private String productName;
    private BigDecimal productPrice;

    public Product(String productName, BigDecimal productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    @Override
    public String toString() {
        return productName + " - " + productPrice + " zł";
    }
}
