package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTimeTest {

    @Test
    @DisplayName("성공적으로 시간 도메인 객체를 생성한다.")
    void createSuccess() {
        // given
        String timeString = "10:00";

        // when
        ReservationTime reservationTime = ReservationTime.create(timeString);

        // then
        assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(reservationTime.getId()).isNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"10시 00분", "1000", "25:00", "10:61", "오전 10시"})
    @DisplayName("잘못된 시간 형식이나 존재하지 않는 시간 입력 시 예외가 발생한다.")
    void validateTimeFormatTest(String invalidTime) {
        // when & then
        assertThatThrownBy(() -> ReservationTime.create(invalidTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시간 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("생성된 시간 객체의 필드 값을 확인한다.")
    void getterTest() {
        // given
        LocalTime now = LocalTime.now();
        ReservationTime reservationTime = new ReservationTime(1L, now);

        // then
        assertThat(reservationTime.getId()).isEqualTo(1L);
        assertThat(reservationTime.getStartAt()).isEqualTo(now);
    }
}
