package roomescape.business.model.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.exception.impl.NameContainsNumberException;
import roomescape.exception.impl.OverMaxNameLengthException;
import roomescape.exception.impl.PastDateException;
import roomescape.exception.impl.ReservationBeforeStartException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class ReservationTest {

    private static final LocalDate DATE = LocalDate.now().plusDays(5);
    private static final ReservationTime RESERVATION_TIME = ReservationTime.beforeSave(LocalTime.of(10, 0));
    private static final Theme THEME = Theme.beforeSave("공포", "", "");
    private static final String NAME = "dompoo";

    @Nested
    class 생성_검증_테스트 {

        @Test
        void 이름은_최대_10자이다() {
            final String name = "dompoolemon";

            assertThatThrownBy(() -> Reservation.beforeSave(name, DATE, RESERVATION_TIME, THEME))
                    .isInstanceOf(OverMaxNameLengthException.class);
        }

        @Test
        void 이름에는_숫자가_포함될_수_없다() {
            final String name = "dompoo1";

            assertThatThrownBy(() -> Reservation.beforeSave(name, DATE, RESERVATION_TIME, THEME))
                    .isInstanceOf(NameContainsNumberException.class);
        }
    }

    @Nested
    class 신규_예약_생성_검증_테스트 {

        @Test
        void 과거_날짜로_예약할_수_없다() {
            final LocalDate pastDate = LocalDate.now().minusDays(1);

            assertThatThrownBy(() -> Reservation.beforeSave(NAME, pastDate, RESERVATION_TIME, THEME))
                    .isInstanceOf(PastDateException.class);
        }

        @Test
        void 현재보다_일주일_이후로_예약할_수_없다() {
            final LocalDate over7DaysDate = LocalDate.now().plusDays(8);

            assertThatThrownBy(() -> Reservation.beforeSave(NAME, over7DaysDate, RESERVATION_TIME, THEME))
                    .isInstanceOf(ReservationBeforeStartException.class);
        }
    }
}
