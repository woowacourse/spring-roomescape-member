package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotAcceptableReservationException;

class ReservationTest {

    private static final EntityId DEFAULT_RESERVATION_ID = EntityId.random();
    private static final ReservationTime DEFAULT_TIME = new ReservationTime(
            EntityId.random(),
            LocalTime.of(10, 0)
    );
    private static final EntityId DEFAULT_THEME_ID = EntityId.random();
    private static final String DEFAULT_NAME = "name";
    private static final LocalDate DEFAULT_DATE = LocalDate.now().plusYears(1);

    @Test
    void 식별자가_없다면_예외를_던진다() {
        assertThatThrownBy(() -> Reservation.create(
                null,
                DEFAULT_NAME,
                DEFAULT_DATE,
                DEFAULT_TIME,
                DEFAULT_THEME_ID
        )).isInstanceOf(InvalidInputException.class)
                .hasMessage("예약엔 식별자가 존재해야 합니다.");
    }

    @Nested
    class 이름을_검증한다 {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 이름이_비어_있으면_예외를_던진다(String emptyName) {
            assertThatThrownBy(() -> Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    emptyName,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            )).isInstanceOf(InvalidInputException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }

        @Test
        void 이름이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    null,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            )).isInstanceOf(InvalidInputException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }
    }

    @Nested
    class 시간을_검증한다 {

        @Test
        void 시간이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    null,
                    DEFAULT_THEME_ID
            )).isInstanceOf(NotAcceptableReservationException.class)
                    .hasMessageContaining("미래 시간의 예약만 생성/취소/수정할 수 있습니다.");
        }

        @Test
        void 현재보다_과거_시점이라면_예외를_던진다() {
            LocalDate pastDate = LocalDate.now().minusDays(1);

            assertThatThrownBy(() -> Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    pastDate,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            ));
        }

        @Test
        void 현재보다_미래_시점이라면_정상적으로_생성된다() {
            LocalDate futureDate = LocalDate.now().plusDays(1);

            assertThatNoException().isThrownBy(() -> Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    futureDate,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            ));
        }
    }

    @Test
    void 날짜가_없다면_예외를_던진다() {
        assertThatThrownBy(() -> Reservation.create(
                DEFAULT_RESERVATION_ID,
                DEFAULT_NAME,
                null,
                DEFAULT_TIME,
                DEFAULT_THEME_ID
        )).isInstanceOf(NotAcceptableReservationException.class)
                .hasMessageContaining("미래 시간의 예약만 생성/취소/수정할 수 있습니다.");
    }

    @Test
    void 테마가_없다면_예외를_던진다() {
        assertThatThrownBy(() -> Reservation.create(
                DEFAULT_RESERVATION_ID,
                DEFAULT_NAME,
                DEFAULT_DATE,
                DEFAULT_TIME,
                null
        )).isInstanceOf(InvalidInputException.class)
                .hasMessage("예약엔 테마가 존재해야 합니다.");
    }

    @Nested
    class 예약의_날짜와_시간을_수정한다 {

        @Test
        void 미래_시점으로_수정하면_정상적으로_변경된다() {
            // given
            Reservation reservation = Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            );

            LocalDate newDate = DEFAULT_DATE.plusDays(1);
            ReservationTime newTime = new ReservationTime(
                    EntityId.random(),
                    LocalTime.of(14, 0)
            );

            // when
            Reservation updated = reservation.updateDateAndTime(newDate, newTime);

            // then
            assertThat(updated.getDate()).isEqualTo(newDate);
            assertThat(updated.getTime()).isEqualTo(newTime);
        }

        @Test
        void 과거_시점으로_수정하면_예외를_던진다() {
            // given
            Reservation reservation = Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            );

            LocalDate pastDate = LocalDate.now().minusDays(1);

            // when and then
            assertThatThrownBy(() -> reservation.updateDateAndTime(pastDate, DEFAULT_TIME))
                    .isInstanceOf(NotAcceptableReservationException.class)
                    .hasMessageContaining("미래 시간의 예약만 생성/취소/수정할 수 있습니다.");
        }

        @Test
        void 취소된_예약은_수정할_수_없다() {
            // given
            Reservation reservation = Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            );
            Reservation canceledReservation = reservation.updateCanceled(true);

            LocalDate newDate = DEFAULT_DATE.plusDays(1);
            ReservationTime newTime = new ReservationTime(
                    EntityId.random(),
                    LocalTime.of(14, 0)
            );

            // when and then
            assertThatThrownBy(() -> canceledReservation.updateDateAndTime(newDate, newTime))
                    .isInstanceOf(NotAcceptableReservationException.class)
                    .hasMessageContaining("취소된 예약은 수정할 수 없습니다.");
        }
    }

    @Nested
    class 예약의_취소_상태를_수정한다 {

        @Test
        void 취소_상태를_true로_변경한다() {
            // given
            Reservation reservation = Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            );

            // when
            Reservation canceled = reservation.updateCanceled(true);

            // then
            assertThat(canceled.isCanceled()).isTrue();
        }

        @Test
        void 과거_시간대의_예약이라면_예외를_던진다() {
            // given
            LocalDate pastDate = LocalDate.now().minusDays(1);
            Reservation pastReservation = Reservation.retrieve(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    pastDate,
                    false,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            );

            // when and then
            assertThatThrownBy(() -> pastReservation.updateCanceled(true))
                    .isInstanceOf(NotAcceptableReservationException.class)
                    .hasMessageContaining("과거의 예약은 수정할 수 없습니다.");
        }
    }

    @Nested
    class 예약의_취소_가능_여부를_확인한다 {

        @Test
        void 미래의_예약은_취소_가능하다() {
            Reservation reservation = Reservation.create(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    DEFAULT_DATE,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            );

            assertThat(reservation.isCancelable()).isTrue();
        }

        @Test
        void 과거의_예약은_취소_불가능하다() {
            Reservation reservation = Reservation.retrieve(
                    DEFAULT_RESERVATION_ID,
                    DEFAULT_NAME,
                    LocalDate.now().minusDays(1),
                    false,
                    DEFAULT_TIME,
                    DEFAULT_THEME_ID
            );

            assertThat(reservation.isCancelable()).isFalse();
        }
    }

    @Nested
    class 예약의_가능_여부를_정적으로_확인한다 {

        @Test
        void 미래_시점의_예약은_가능하다() {
            LocalDate futureDate = LocalDate.now().plusDays(1);

            assertThat(Reservation.isAvailable(futureDate, DEFAULT_TIME)).isTrue();
        }

        @Test
        void 과거_시점의_예약은_불가능하다() {
            LocalDate pastDate = LocalDate.now().minusDays(1);

            assertThat(Reservation.isAvailable(pastDate, DEFAULT_TIME)).isFalse();
        }

        @Test
        void null_값의_예약은_불가능하다() {
            assertThat(Reservation.isAvailable(null, DEFAULT_TIME)).isFalse();
            assertThat(Reservation.isAvailable(DEFAULT_DATE, null)).isFalse();
        }
    }
}
