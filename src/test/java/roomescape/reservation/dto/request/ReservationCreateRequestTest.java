package roomescape.reservation.dto.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservationCreateRequestTest {

    @Test
    void create_shouldThrowException_whenDateformatIllegal() {
        assertThatThrownBy(
                () -> new ReservationCreateRequest(
                        LocalDate.parse("2025-12"),
                        1L,
                        1L
                )
        );
    }
}
