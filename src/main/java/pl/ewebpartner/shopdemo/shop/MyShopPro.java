package pl.ewebpartner.shopdemo.shop;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.ewebpartner.shopdemo.properties.DiscountProperties;
import pl.ewebpartner.shopdemo.properties.VatProperties;
import pl.ewebpartner.shopdemo.service.ProductListService;
import pl.ewebpartner.shopdemo.util.PriceCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Profile("Pro")
public class MyShopPro {
    private final ProductListService productListService;
    private final VatProperties vatProperties;
    private final DiscountProperties discountProperties;
    private final PriceCalculator priceCalculator;

    public MyShopPro(ProductListService productListService, VatProperties vatProperties, DiscountProperties discountProperties, PriceCalculator priceCalculator) {
        this.productListService = productListService;
        this.vatProperties = vatProperties;
        this.discountProperties = discountProperties;
        this.priceCalculator = priceCalculator;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initShop() {
        productListService.initProducts();
        BigDecimal totalSumNetPrice = priceCalculator.calculateNetSum(productListService.getProducts());
        BigDecimal totalSumGrossPrice = priceCalculator.calculateGrossSum(totalSumNetPrice, vatProperties.getValueA());
        BigDecimal totalSumNetPriceAfterDiscount = priceCalculator.calculateNetAfterDiscount(totalSumNetPrice, discountProperties.getValueA());
        BigDecimal totalSumGrossPriceAfterDiscount = totalSumNetPriceAfterDiscount.multiply(BigDecimal.valueOf(vatProperties.getValueA() + 100).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);

        productListService.getProducts().forEach(product -> {
            int vat = vatProperties.getValueA();
            int discountRate = discountProperties.getValueA();

            BigDecimal productGrossValue = priceCalculator.calculateGross(product.getProductPrice(), vat);
            BigDecimal discountAmount = priceCalculator.calculateDiscount(product.getProductPrice(), discountRate);
            BigDecimal discountedNetPrice = product.getProductPrice().subtract(discountAmount);
            BigDecimal discountedGrossPrice = priceCalculator.calculateGross(discountedNetPrice, vat);

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
