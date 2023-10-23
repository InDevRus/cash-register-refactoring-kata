package cashregister.discount;

import cashregister.OrderEntry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.money.MonetaryAmount;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class DiscounterByProductType implements Discounter {
    private static final int DISCOUNT_PRECISION_SCALE = 2;

    private static final BigDecimal ZERO_DISCOUNT = BigDecimal.valueOf(0, DISCOUNT_PRECISION_SCALE);
    private static final BigDecimal FULL_DISCOUNT = BigDecimal.valueOf(100, DISCOUNT_PRECISION_SCALE);

    @RequiredByDiscounter
    private BigDecimal getDiscountCoefficient(OrderEntry orderEntry) {
        return switch (orderEntry.product().type()) {
            case REGULAR, PROMOTED -> ZERO_DISCOUNT;
            case SECOND_70_PERCENT_LESS -> BigDecimal.valueOf(70, DISCOUNT_PRECISION_SCALE);
            case PROMO_3x2 -> FULL_DISCOUNT;
        };
    }

    @RequiredByDiscounter
    private int findItemsToDiscount(OrderEntry orderEntry) {
        return switch (orderEntry.product().type()) {
            case REGULAR, PROMOTED -> 0;
            case SECOND_70_PERCENT_LESS -> orderEntry.quantity() / 2;
            case PROMO_3x2 -> orderEntry.quantity() / 3;
        };
    }

    @Override
    public MonetaryAmount findDiscountedCost(@NonNull OrderEntry orderEntry) {
        var intendedCost = orderEntry.product().price()
                .multiply(BigDecimal.valueOf(orderEntry.quantity()));
        return intendedCost.subtract(findDiscount(orderEntry));
    }

    @Override
    public MonetaryAmount findDiscount(@NonNull OrderEntry orderEntry) {
        return orderEntry.product().price()
                .multiply(findItemsToDiscount(orderEntry))
                .multiply(getDiscountCoefficient(orderEntry));
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface RequiredByDiscounter {
    }
}
