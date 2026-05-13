package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static roomescape.reservation.domain.ReservationStatus.CANCELED;
import static roomescape.reservation.domain.ReservationStatus.RESERVED;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.date.domain.ReservationDate;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    private final String name = "한다";
    private final LocalDate date = LocalDate.now().plusMonths(1);
    private final ReservationDate reservationDate = ReservationDate.create(date);
    private final ReservationDate pastDate = ReservationDate.load(2L, LocalDate.now().minusDays(1), true);
    private final LocalTime startAt = LocalTime.of(15, 40);
    private final ReservationTime reservationTime = ReservationTime.create(startAt);
    private final Theme theme = Theme.load(1L, "테마", "설명", "썸네일", true);
    private final Reservation reservation = Reservation.load(1L, name, reservationDate, reservationTime, theme, RESERVED);

    @Test
    @DisplayName("예약 id를 가져온다.")
    void getId() {
        //given
        Long expected = 1L;

        //when
        Long actual = reservation.id();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("예약자명을 가져온다.")
    void getName() {
        //given
        String expected = "한다";

        //when
        String actual = reservation.name();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("예약날짜를 가져온다.")
    void getDate() {
        //given
        LocalDate expected = LocalDate.now().plusMonths(1);

        //when
        LocalDate actual = reservation.date().date();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("예약시간을 가져온다.")
    void getTime() {
        //given
        LocalTime expected = startAt;

        //when
        LocalTime actual = reservation.time().startAt();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("두 예약 객체의 동등성을 비교한다.")
    void equals() {
        //given
        Reservation otherReservation = Reservation.load(1L, name, reservationDate, reservationTime, theme, RESERVED);

        // when & then
        assertThat(reservation)
                .usingRecursiveComparison()
                .isEqualTo(otherReservation);
    }

    @Test
    @DisplayName("아직 DB에 추가되지 않은 예약은 id가 없다.")
    void unpersist_reservation_null_id() {
        // given & when
        Reservation unpersistReservation = Reservation.create("한다", reservationDate, reservationTime, theme);

        // then
        assertThat(unpersistReservation.id())
                .isNull();
    }

    @Test
    @DisplayName("예약자명이 유효하지 않은 경우 생성 시 예외가 발생한다.")
    void validateName() {
        // given
        String nullName = null;
        String emptyName = "";

        // when & then
        assertThatThrownBy(() -> Reservation.create(nullName, reservationDate, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 필수입니다.");

        assertThatThrownBy(() -> Reservation.create(emptyName, reservationDate, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약자 이름은 필수입니다.");
    }

    @Test
    @DisplayName("예약 시간이 유효하지 않은 경우 생성 시 예외가 발생한다.")
    void validateTime() {
        // given
        ReservationTime nullTime = null;

        // when & then
        assertThatThrownBy(() -> Reservation.create(name, reservationDate, nullTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간은 필수입니다.");
    }

    @Test
    @DisplayName("예약 날짜가 유효하지 않은 경우 생성 시 예외가 발생한다.")
    void validateDate() {
        // given
        ReservationDate nullDate = null;

        // when & then
        assertThatThrownBy(() -> Reservation.create(name, nullDate, reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 필수입니다.");
    }

    @Test
    @DisplayName("예약 ID가 유효하지 않은 경우 생성 시 예외가 발생한다.")
    void validateId() {
        // given
        Long nullId = null;

        // when & then
        assertThatThrownBy(() -> Reservation.load(nullId, name, reservationDate, reservationTime, theme, RESERVED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 ID는 필수입니다.");
    }

    @Test
    @DisplayName("아직 지나지 않은 본인의 예약은 취소할 수 있다.")
    void cancel() {
        // given
        String reservationName = "송송";
        Reservation reserved = ReservationFixture.reservation(reservationName, reservationDate, reservationTime, theme);

        // when
        reserved.cancel(reservationName);

        // then
        assertThat(reserved.status())
                .isEqualTo(CANCELED);
    }

    @Test
    @DisplayName("본인의 예약이 아닌데 취소를하면 예외가 발생한다.")
    void cancel_not_owner() {
        // given
        String reservationName = "송송";
        String anotherName = "주주";
        Reservation reserved = ReservationFixture.reservation(reservationName, reservationDate, reservationTime, theme);

        // when & then
        assertThatThrownBy(() -> reserved.cancel(anotherName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 예약만 취소할 수 있습니다.");
    }

    @Test
    @DisplayName("이미 취소된 예약을 취소하면 예외가 발생한다.")
    void cancel_already_canceled() {
        // given
        String reservationName = "송송";
        Reservation reserved = ReservationFixture.reservation(reservationName, reservationDate, reservationTime, theme);
        reserved.updateStatus(CANCELED);

        // when & then
        assertThatThrownBy(() -> reserved.cancel(reservationName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 취소된 예약입니다.");
    }

    @Test
    @DisplayName("이미 지난 예약을 취소하면 예외가 발생한다.")
    void cancel_not_past() {
        // given
        String reservationName = "송송";
        Reservation reserved = Reservation.load(2L, reservationName, pastDate, reservationTime, theme, RESERVED);

        // when & then
        assertThatThrownBy(() -> reserved.cancel(reservationName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 지난 예약은 취소할 수 없습니다.");
    }

}
