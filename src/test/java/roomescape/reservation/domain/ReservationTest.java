package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.globalexception.BadRequestException;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.fixture.ReservationTimeFixture;
import roomescape.theme.domain.Theme;

class ReservationTest {

    @Nested
    @DisplayName("예약 시점이 현재보다 과거이면 예외가 발생해야 한다.")
    class isPastTense {

        @DisplayName("예약 시점이 과거이면 예외가 발생한다.")
        @Test
        void isPastTense_throwsExceptionByPastTime() {
            // given
            String dummyName = "kali";
            LocalDate dummyPastDate = LocalDate.of(2024, 4, 25);
            LocalTime dummyTime = LocalTime.of(11, 13);
            ReservationTime reservationTime = ReservationTimeFixture.create(dummyTime);

            Theme theme = new Theme("name1", "dd", "tt");

            // when & then
            Assertions.assertThatThrownBy(
                () -> ReservationFixture.create(dummyName, dummyPastDate, reservationTime, theme)
            ).isInstanceOf(BadRequestException.class);
        }

        @DisplayName("예약 시점이 미래이면 예외를 발생하지 않는다.")
        @Test
        void isPastTense_doesNotThrowExceptionByFutureTime() {
            // given
            String dummyName = "kali";
            LocalDateTime dummyFuture = LocalDateTime.now().plusDays(1);
            LocalDate dummyPastDate = dummyFuture.toLocalDate();
            LocalTime dummyTime = dummyFuture.toLocalTime();
            ReservationTime reservationTime = ReservationTimeFixture.create(dummyTime);

            Theme theme = new Theme("name1", "dd", "tt");

            // when & then
            Assertions.assertThatCode(
                () -> ReservationFixture.create(dummyName, dummyPastDate, reservationTime, theme)
            ).doesNotThrowAnyException();
        }

        @DisplayName("localDate만 다르고 localTime이 같으면 isSameDateTime에서 false이다.")
        @Test
        void isSameDateTime_false_bySameDateDifferenceTime() {
            // given
            String dummyName1 = "kali";
            LocalDateTime dummyDateTime1 = LocalDateTime.now().plusDays(1);
            ReservationTime duplicateReservationTime = ReservationTimeFixture.create(dummyDateTime1.toLocalTime());

            Theme theme = new Theme("name1", "dd", "tt");

            Reservation reservation1 = ReservationFixture.create(dummyName1, dummyDateTime1.toLocalDate(),
                duplicateReservationTime, theme);

            String dummyName2 = "pobi";
            LocalDateTime dummyDateTime2 = LocalDateTime.now().plusDays(2);
            Reservation reservation2 = ReservationFixture.create(dummyName2, dummyDateTime2.toLocalDate(),
                duplicateReservationTime, theme);

            // when
            Assertions.assertThat(reservation1.isSameDateTime(reservation2)).isFalse();
        }

        @DisplayName("date와 reservationTime의 startAt 필드를 합쳐서 LocalDateTime 형식으로 반환할 수 있다")
        @Test
        void getDateTime() {
            // given
            LocalDate localDate = LocalDate.of(2023, 11, 2);
            LocalTime localTime = LocalTime.of(22, 33);

            LocalDateTime expected = LocalDateTime.of(localDate, localTime);

            ReservationTime reservationTime = ReservationTimeFixture.create(localTime);

            // when
            LocalDateTime actual = LocalDateTime.of(localDate, reservationTime.getStartAt());

            // then
            Assertions.assertThat(actual).isEqualTo(expected);
        }
    }
}
