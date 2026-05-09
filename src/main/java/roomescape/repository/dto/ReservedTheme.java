package roomescape.repository.dto;

import java.util.UUID;

public record ReservedTheme(
        UUID id,
        String name,
        String description,
        String imageUrl,
        int reservationCount
) {
}
