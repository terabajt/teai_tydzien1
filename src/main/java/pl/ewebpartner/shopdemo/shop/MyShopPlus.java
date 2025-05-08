package pl.ewebpartner.shopdemo.shop;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.ewebpartner.shopdemo.properties.VatProperties;
import pl.ewebpartner.shopdemo.service.ProductListService;
import pl.ewebpartner.shopdemo.util.PriceCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Profile("Plus")
public class MyShopPlus {
    private final ProductListService productListService;
    private final VatProperties vatProperties;
    private final PriceCalculator priceCalculator;

    public MyShopPlus(ProductListService productListService, VatProperties vatProperties, PriceCalculator priceCalculator) {
        this.productListService = productListService;
        this.vatProperties = vatProperties;
        this.priceCalculator = priceCalculator;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initShop() {
        productListService.initProducts();
        BigDecimal totalSumNetPrice = priceCalculator.calculateNetSum(productListService.getProducts());
        BigDecimal totalSumGrossPrice = priceCalculator.calculateGrossSum(totalSumNetPrice, vatProperties.getValueA());

        productListService.getProducts().forEach(product -> {
            BigDecimal productGrossValue = product.getProductPrice().multiply(BigDecimal.valueOf(vatProperties.getValueA() + 100)).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            System.out.println(product.getProductName() + " " + product.getProductPrice().setScale(2, RoundingMode.HALF_UP) + " zł netto + " + vatProperties.getValueA() + "% VAT (" + productGrossValue + " zł brutto)");
        });

        System.out.println("-----------");
        System.out.println("Razem: " + totalSumNetPrice + " zł netto, " + totalSumGrossPrice + " zł brutto.");
    }
}
