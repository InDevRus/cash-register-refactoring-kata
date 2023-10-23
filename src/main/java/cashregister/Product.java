package cashregister;

import lombok.*;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

public record Product(@NonNull String name, @NonNull MonetaryAmount price, @NonNull ProductType type) {
    public static final CurrencyUnit DEFAULT_UNIT = Monetary.getCurrency("EUR");
}