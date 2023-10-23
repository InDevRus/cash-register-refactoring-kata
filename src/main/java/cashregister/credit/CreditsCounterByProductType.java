package cashregister.credit;

import cashregister.OrderEntry;
import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CreditsCounterByProductType implements CreditsCounter {
    @RequiredByCreditsCounter
    @Override
    public int countCredits(@NonNull OrderEntry orderEntry) {
        return switch (orderEntry.product().type()) {
            case REGULAR -> creditsForRegular(orderEntry);
            case PROMOTED -> creditsForPromoted(orderEntry);
            case SECOND_70_PERCENT_LESS, PROMO_3x2 -> 0;
        };
    }

    private int creditsForRegular(OrderEntry orderEntry) {
        return orderEntry.quantity() > 10 ? 1 : 0;
    }

    private int creditsForPromoted(OrderEntry orderEntry) {
        return orderEntry.quantity();
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    private @interface RequiredByCreditsCounter {

    }
}
