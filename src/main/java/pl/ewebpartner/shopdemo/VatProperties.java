package pl.ewebpartner.shopdemo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vat")
public class VatProperties {
    private final int valueA;

    public VatProperties(int valueA) {
        this.valueA = valueA;
    }

    public int getValueA() {
        return valueA;
    }
}
