package cashregister.credit;

import cashregister.OrderEntry;
import lombok.NonNull;

public interface CreditsCounter {
    int countCredits(@NonNull OrderEntry orderEntry);
}
