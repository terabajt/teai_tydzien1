package pl.ewebpartner.shopdemo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discount")
public class DiscountProperties {
    private final int valueA;

    public DiscountProperties(int valueA) {
        this.valueA = valueA;
    }

    public int getValueA() {
        return valueA;
    }
}
