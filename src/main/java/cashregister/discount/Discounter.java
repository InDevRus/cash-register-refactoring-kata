package cashregister.discount;

import cashregister.OrderEntry;
import lombok.NonNull;

import javax.money.MonetaryAmount;

public interface Discounter {
    MonetaryAmount findDiscount(@NonNull OrderEntry orderEntry);
    MonetaryAmount findDiscountedCost(@NonNull OrderEntry orderEntry);
}
