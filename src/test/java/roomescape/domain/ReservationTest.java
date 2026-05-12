package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.global.exception.reservation.InvalidReservationException;

class ReservationTest {

    private ReservationTime time;
    private Theme theme;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        time = ReservationTime.createNew(LocalTime.of(10, 0));
        theme = Theme.createNew("공포의 저택", "무서운 테마입니다.", "https://image.url");
        date = LocalDate.now();
    }

    @Test
    @DisplayName("예약을 정상적으로 생성한다.")
    void createReservation() {
        Reservation reservation = Reservation.createNew("Greene", date, time, theme);

        assertThat(reservation.getName()).isEqualTo("Greene");
        assertThat(reservation.getId()).isNull();
    }

    @Test
    @DisplayName("이름이 정확히 50자인 경우 예약이 정상 생성된다. (경계값 테스트)")
    void validateNameLength_BoundaryPass() {
        String exactBoundaryName = "a".repeat(50);

        assertThatCode(() -> Reservation.createNew(exactBoundaryName, date, time, theme))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이름이 50자를 초과하면 예외가 발생한다. (경계값 밖)")
    void validateNameLength_BoundaryFail() {
        String longName = "a".repeat(51);

        assertThatThrownBy(() -> Reservation.createNew(longName, date, time, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("50자를 넘을 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    @DisplayName("이름이 null이거나 비어있으면 예외가 발생한다.")
    void validateName_NullOrBlank(String invalidName) {
        assertThatThrownBy(() -> Reservation.createNew(invalidName, date, time, theme))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("비어있을 수 없습니다.");
    }
}
