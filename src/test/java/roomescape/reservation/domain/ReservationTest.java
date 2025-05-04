package roomescape.reservation.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.exception.PastDateReservationException;
import roomescape.reservation.exception.PastTimeReservationException;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationTest {

    @Test
    @DisplayName("과거 날짜와 시간에 대한 예약 생성은 불가능하다.")
    void validatePast() {

        final Theme theme = Theme.withoutId(ThemeName.from("공포"),
                ThemeDescription.from("지구별 방탈출 최고"),
                ThemeThumbnail.from("www.making.com"));
        Assertions.setMaxStackTraceElementsDisplayed(Integer.MAX_VALUE);
        assertAll(() -> {

            assertThatThrownBy(() -> Reservation.withoutId(
                    ReserverName.from("시소"),
                    ReservationDate.from(LocalDate.now().minusDays(1L)),
                    ReservationTime.withoutId(LocalTime.now()),
                    theme
            )).isInstanceOf(PastDateReservationException.class);

            assertThatThrownBy(() -> Reservation.withoutId(
                    ReserverName.from("시소"),
                    ReservationDate.from(LocalDate.now()),
                    ReservationTime.withoutId(LocalTime.now().minusMinutes(1L)),
                    theme
            )).isInstanceOf(PastTimeReservationException.class);

        });
    }
}
