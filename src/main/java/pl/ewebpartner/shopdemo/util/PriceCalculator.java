package pl.ewebpartner.shopdemo.util;

import org.springframework.stereotype.Component;
import pl.ewebpartner.shopdemo.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PriceCalculator {
    public BigDecimal calculateVAT(BigDecimal net, int vatPercent) {
        return net.multiply(BigDecimal.valueOf(vatPercent))
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateGross(BigDecimal net, int vatPercent) {
        return net.add(calculateVAT(net, vatPercent))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateDiscount(BigDecimal net, int discountPercent) {
        return net.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateNetAfterDiscount(BigDecimal net, int discountPercent) {
        BigDecimal discount = calculateDiscount(net, discountPercent);
        return net.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal applyDiscount(BigDecimal net, int discountPercent) {
        return net.subtract(calculateDiscount(net, discountPercent))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateNetSum(List<Product> products) {
        return products.stream()
                .map(Product::getProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateGrossSum(BigDecimal netSum, int vatPercent) {
        return netSum.multiply(BigDecimal.valueOf(vatPercent + 100)
                        .divide(BigDecimal.valueOf(100)))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
