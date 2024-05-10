package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.global.exception.RoomescapeException;

class ReservationTest {

    private final String validName = "sudal";
    private final String validDate = "2060-01-01";
    private final ReservationTime validReservationTime = new ReservationTime("10:00");
    private final Theme validTheme = new Theme("방탈출", "방탈출하는 게임", "https://");

    @DisplayName("성공: 올바른 값 입력하여 생성")
    @Test
    void construct() {
        assertThatCode(
            () -> new Reservation(validName, validDate, validReservationTime, validTheme)
        ).doesNotThrowAnyException();
    }

    @DisplayName("실패: 올바르지 않은 예약자명")
    @ParameterizedTest
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    void construct_IllegalName(String invalidName) {
        assertThatThrownBy(
            () -> new Reservation(invalidName, validDate, validReservationTime, validTheme)
        ).isInstanceOf(RoomescapeException.class)
            .hasMessage("예약자 이름은 null이거나 비어 있을 수 없습니다.");
    }
}
