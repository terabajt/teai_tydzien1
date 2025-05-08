package pl.ewebpartner.shopdemo.shop;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.ewebpartner.shopdemo.service.ProductListService;
import pl.ewebpartner.shopdemo.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Profile("Start")
public class MyShopStart {

    private final ProductListService productListService;

    public MyShopStart(ProductListService productListService) {
        this.productListService = productListService;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void initShop() {
        productListService.initProducts();

        BigDecimal totalSumPrice = productListService.getProducts().stream().map(Product::getProductPrice).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        productListService.getProducts().stream().forEach(System.out::println);
        System.out.println("-----------");
        System.out.println("Razem: " + totalSumPrice + " zł.");

    }

}
