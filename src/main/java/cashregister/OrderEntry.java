package cashregister;

import lombok.*;

public record OrderEntry(@NonNull Product product, int quantity) {
}