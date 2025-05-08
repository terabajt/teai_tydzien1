package pl.ewebpartner.shopdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.ewebpartner.shopdemo.properties.DiscountProperties;
import pl.ewebpartner.shopdemo.properties.VatProperties;

@SpringBootApplication
@EnableConfigurationProperties({VatProperties.class, DiscountProperties.class})
public class ShopDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopDemoApplication.class, args);
    }

}
