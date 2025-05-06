package pl.ewebpartner.shopdemo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Profile("Pro")
public class MyShopPro {
    private final ProductListService productListService;
    private final VatProperties vatProperties;
    private final DiscountProperties discountProperties;


    public MyShopPro(ProductListService productListService, VatProperties vatProperties, DiscountProperties discountProperties) {
        this.productListService = productListService;
        this.vatProperties = vatProperties;
        this.discountProperties = discountProperties;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void initShop() {
        productListService.initProducts();

        BigDecimal totalSumNetPrice = productListService.getProducts().stream().map(Product::getProductPrice).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSumGrossPrice = totalSumNetPrice.multiply(BigDecimal.valueOf(vatProperties.getValueA() + 100).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSumNetPriceAfterDiscount = totalSumNetPrice
                .multiply(BigDecimal.ONE.subtract(
                        BigDecimal.valueOf(discountProperties.getValueA())
                                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSumGrossPriceAfterDiscount = totalSumNetPriceAfterDiscount.multiply(BigDecimal.valueOf(vatProperties.getValueA() + 100).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);

        productListService.getProducts().forEach(product -> {
            BigDecimal vatRate = BigDecimal.valueOf(vatProperties.getValueA());
            BigDecimal discountRate = BigDecimal.valueOf(discountProperties.getValueA());

            // VAT amount and gross price
            BigDecimal vatAmount = product.getProductPrice()
                    .multiply(vatRate)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            BigDecimal productGrossValue = product.getProductPrice()
                    .add(vatAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            // Discount value and discounted price
            BigDecimal discountAmount = product.getProductPrice()
                    .multiply(discountRate)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal discountedNetPrice = product.getProductPrice()
                    .subtract(discountAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal vatMultiplier = BigDecimal.valueOf(vatProperties.getValueA())
                    .add(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            BigDecimal discountedGrossPrice = discountedNetPrice
                    .multiply(vatMultiplier)
                    .setScale(2, RoundingMode.HALF_UP);

            System.out.println(product.getProductName() + " " + product.getProductPrice() + " zł netto + "
                    + vatProperties.getValueA() + "% VAT (" + productGrossValue + " zł brutto)");
            System.out.println("Rabat: " + discountAmount + " zł. Cena po rabacie: " + discountedNetPrice + " zł netto, " + discountedGrossPrice + " zł brutto");
            System.out.println("");
        });

        System.out.println("-----------");
        System.out.println("Razem: " + totalSumNetPrice + " zł netto, " + totalSumGrossPrice + " zł brutto.");
        System.out.println("Po rabacie razem: " + totalSumNetPriceAfterDiscount + " zł netto, " + totalSumGrossPriceAfterDiscount + " zł brutto.");


    }
}
