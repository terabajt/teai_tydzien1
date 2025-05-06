package pl.ewebpartner.shopdemo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Profile("Plus")
public class MyShopPlus {
    private final ProductListService productListService;
    private final VatProperties vatProperties;


    public MyShopPlus(ProductListService productListService, VatProperties vatProperties) {
        this.productListService = productListService;
        this.vatProperties = vatProperties;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void initShop() {
        productListService.initProducts();

        BigDecimal totalSumNetPrice = productListService.getProducts().stream().map(Product::getProductPrice).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalSumGrossPrice = totalSumNetPrice.multiply(BigDecimal.valueOf(vatProperties.getValueA() + 100).divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_UP);
        productListService.getProducts().stream().forEach(product -> {
            BigDecimal productGrossValue = product.getProductPrice().multiply(BigDecimal.valueOf(vatProperties.getValueA() + 100)).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            System.out.println(product.getProductName() + " " + product.getProductPrice().setScale(2, RoundingMode.HALF_UP) + " zł netto + " + vatProperties.getValueA() + "% VAT (" + productGrossValue + " zł brutto)");
        });

        System.out.println("-----------");
        System.out.println("Razem: "+ totalSumNetPrice + " zł netto, " + totalSumGrossPrice + " zł brutto.");
    }
}
