package roomescape.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final ReservationTime futureTime = new ReservationTime(1L, LocalTime.of(22, 0));
    private final LocalDate futureDate = LocalDate.now().plusDays(1);

    @Test
    @DisplayName("올바른 정보로 예약을 생성하면 예외가 발생하지 않는다")
    void create_Success() {
        assertThatCode(() -> new Reservation("루크", futureDate, futureTime))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource // null과 ""(빈 문자열)을 모두 테스트
    @ValueSource(strings = {" ", "  ", "\t", "\n"}) // 공백 문자들 테스트
    @DisplayName("이름이 비어있거나 공백이면 예외가 발생한다")
    void invalidName_Exception(String name) {
        assertThatThrownBy(() -> new Reservation(name, futureDate, futureTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 필수이며, 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("예약 날짜가 과거이면 예외가 발생한다")
    void pastDate_Exception() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> new Reservation("루크", pastDate, futureTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 날짜는 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("오늘 날짜 예약이라도 이미 지난 시간이면 예외가 발생한다")
    void pastTimeToday_Exception() {
        ReservationTime pastTime = new ReservationTime(1L, LocalTime.now().minusMinutes(1));
        LocalDate today = LocalDate.now();

        assertThatThrownBy(() -> new Reservation("루크", today, pastTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("오늘의 지난 시간은 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("날짜나 시간 정보가 누락되면 예외가 발생한다")
    void nullInfo_Exception() {
        assertThatThrownBy(() -> new Reservation("루크", null, futureTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 필수입니다.");

        assertThatThrownBy(() -> new Reservation("루크", futureDate, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 필수입니다.");
    }
}
