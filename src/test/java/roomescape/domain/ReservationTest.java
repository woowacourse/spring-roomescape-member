package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.exception.EmptyNameException;

class ReservationTest {

    @DisplayName("이름이 비어 있는 예약은 생성할 수 없다")
    @ParameterizedTest(name = "이름이 {0}이면 생성할 수 없다.")
    @NullAndEmptySource
    void 이름이_비어_있으면_EmptyNameException_예외를_던진다(String emptyName) {
        assertThatThrownBy(() -> Reservation.withoutId(
                        emptyName,
                        LocalDate.now(),
                        ReservationTime.withoutId(LocalTime.now()),
                        Theme.withoutId("samplet heme", "sample description", "sample url")
                )
        ).isExactlyInstanceOf(EmptyNameException.class);
    }
}
