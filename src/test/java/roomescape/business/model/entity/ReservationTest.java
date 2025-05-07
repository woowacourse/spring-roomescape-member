package roomescape.business.model.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.exception.business.PastDateException;
import roomescape.exception.business.ReservationBeforeStartException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class ReservationTest {

    private static final LocalDate DATE = LocalDate.now().plusDays(5);
    private static final ReservationTime RESERVATION_TIME = ReservationTime.beforeSave(LocalTime.of(10, 0));
    private static final Theme THEME = Theme.beforeSave("공포", "", "");
    private static final String NAME = "dompoo";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password1234!";

    @Nested
    class 신규_예약_생성_검증_테스트 {

        @Test
        void 정상적인_예약을_생성할_수_있다() {
            // given
            final User user = User.beforeSave(NAME, EMAIL, PASSWORD);

            // when
            final Reservation reservation = Reservation.beforeSave(user, DATE, RESERVATION_TIME, THEME);

            // then
            assertThat(reservation).isNotNull();
            assertThat(reservation.getName()).isEqualTo(NAME);
            assertThat(reservation.getDate()).isEqualTo(DATE);
            assertThat(reservation.getTime()).isEqualTo(RESERVATION_TIME);
            assertThat(reservation.getTheme()).isEqualTo(THEME);
        }

        @Test
        void 과거_날짜로_예약할_수_없다() {
            final LocalDate pastDate = LocalDate.now().minusDays(1);
            final User user = User.beforeSave(NAME, EMAIL, PASSWORD);

            assertThatThrownBy(() -> Reservation.beforeSave(user, pastDate, RESERVATION_TIME, THEME))
                    .isInstanceOf(PastDateException.class);
        }

        @Test
        void 현재보다_일주일_이후로_예약할_수_없다() {
            final LocalDate over7DaysDate = LocalDate.now().plusDays(8);
            final User user = User.beforeSave(NAME, EMAIL, PASSWORD);

            assertThatThrownBy(() -> Reservation.beforeSave(user, over7DaysDate, RESERVATION_TIME, THEME))
                    .isInstanceOf(ReservationBeforeStartException.class);
        }
    }
}
