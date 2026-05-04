package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidReservationException;

class ReservationTest {

    private static final long DEFAULT_ID = 1;
    private static final String DEFAULT_NAME = "name";
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2025, 1, 1);
    private static final ReservationTime DEFAULT_TIME = ReservationTime.create(LocalTime.of(1, 1));
    private static final Theme DEFAULT_THEME = Theme.create("themeName", "themeDesrciption", "themeUrl");

    @Nested
    class 이름을_검증한다 {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 생성_시에_이름이_비어_있으면_예외를_던진다(String emptyName) {
            assertThatThrownBy(() -> Reservation.create(
                    emptyName,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }

        @Test
        void 생성_시에_이름이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.create(
                    null,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 불러올_때_이름이_비어_있으면_예외를_던진다(String emptyName) {
            assertThatThrownBy(() -> Reservation.retrieve(
                    DEFAULT_ID,
                    emptyName,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }

        @Test
        void 불러올_때_이름이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.retrieve(
                    DEFAULT_ID,
                    null,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }
    }

    @Nested
    class 날짜를_검증한다 {

        @Test
        void 생성_시에_날짜가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.create(
                    DEFAULT_NAME,
                    null,
                    DEFAULT_TIME,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 날짜가 존재해야 합니다.");
        }

        @Test
        void 불러올_때_날짜가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.retrieve(
                    DEFAULT_ID,
                    DEFAULT_NAME,
                    null,
                    DEFAULT_TIME,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 날짜가 존재해야 합니다.");
        }
    }

    @Nested
    class 시간을_검증한다 {

        @Test
        void 생성_시에_시간이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.create(
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    null,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 시간이 존재해야 합니다.");
        }

        @Test
        void 불러올_때_시간이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.retrieve(
                    DEFAULT_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    null,
                    DEFAULT_THEME
            )).isInstanceOf(InvalidReservationException.class)
                    .hasMessage("예약엔 시간이 존재해야 합니다.");
        }
    }
}
