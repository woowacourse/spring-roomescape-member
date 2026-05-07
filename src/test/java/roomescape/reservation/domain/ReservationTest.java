package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(15, 0));
    private final Theme theme = new Theme(1L, "테마", "설명", "url");
    private final LocalDate futureDate = LocalDate.of(2026, 12, 31);

    @Nested
    @DisplayName("생성 및 이름 검증 테스트")
    class CreationTest {

        @Test
        @DisplayName("정상적인 데이터로 예약 객체를 생성한다.")
        void createSuccess() {
            assertThatCode(() -> new Reservation(1L, "브라운", futureDate, reservationTime, theme))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  "})
        @DisplayName("이름이 비어있거나 공백이면 예외가 발생한다.")
        void failWhenNameIsBlank(String name) {
            assertThatThrownBy(() -> new Reservation(1L, name, futureDate, reservationTime, theme))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름은 비어 있을 수 없습니다.");
        }

        @Test
        @DisplayName("이름이 10자를 초과하면 예외가 발생한다.")
        void failWhenNameIsTooLong() {
            String longName = "열한글자이름입니다용ㅇ";
            assertThatThrownBy(() -> new Reservation(1L, longName, futureDate, reservationTime, theme))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이름은 10글자 이하여야 합니다.");
        }

        @Test
        @DisplayName("필수 데이터(날짜, 시간, 테마)가 누락되면 예외가 발생한다.")
        void failWhenRequiredFieldIsNull() {
            assertThatThrownBy(() -> new Reservation(1L, "브라운", null, reservationTime, theme))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("validateNotPast 메서드는")
    class ValidateNotPastTest {

        @Test
        @DisplayName("예약 일시가 현재 시각보다 미래라면 예외가 발생하지 않는다.")
        void successWhenFuture() {
            // given
            Reservation reservation = new Reservation(1L, "브라운", futureDate, reservationTime, theme);
            LocalDateTime now = LocalDateTime.of(2026, 5, 7, 10, 0);

            // when & then
            assertThatCode(() -> reservation.validateNotPast(now))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예약 일시가 현재 시각보다 과거라면 예외가 발생한다.")
        void failWhenPast() {
            // given
            LocalDate pastDate = LocalDate.of(2026, 5, 6);
            Reservation reservation = new Reservation(1L, "브라운", pastDate, reservationTime, theme);
            LocalDateTime now = LocalDateTime.of(2026, 5, 7, 10, 0);

            // when & then
            assertThatThrownBy(() -> reservation.validateNotPast(now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("과거 시각으로는 예약할 수 없습니다.");
        }

        @Test
        @DisplayName("오늘 날짜라도 예약 시간이 현재 시간보다 이전이면 예외가 발생한다.")
        void failWhenTodayButPastTime() {
            // given
            LocalDate today = LocalDate.of(2026, 5, 7);
            LocalTime pastTime = LocalTime.of(9, 0); // 예약 시간 9시
            ReservationTime time = new ReservationTime(1L, pastTime);

            Reservation reservation = new Reservation(1L, "브라운", today, time, theme);
            LocalDateTime now = LocalDateTime.of(2026, 5, 7, 10, 0);

            // when & then
            assertThatThrownBy(() -> reservation.validateNotPast(now))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
