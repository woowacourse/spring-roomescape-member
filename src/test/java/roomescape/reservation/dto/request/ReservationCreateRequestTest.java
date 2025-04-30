package roomescape.reservation.dto.request;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservationCreateRequestTest {

    @Test
    void create_shouldThrowException_whenDateformatIllegal() {
        assertThatThrownBy(
                () -> new ReservationCreateRequest(
                        "Danny",
                        LocalDate.parse("2025-12"),
                        1L
                )
        );
    }

    @Test
    void create_shouldThrowException_whenNameNull() {
        assertThatThrownBy(
                () -> new ReservationCreateRequest(
                        null,
                        LocalDate.parse("2025-12-25"),
                        1L
                )
        ).hasMessage("이름은 반드시 입력해야합니다.");
    }
}