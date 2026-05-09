package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static roomescape.reservation.domain.ReservationStatus.RESERVED;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.date.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

class ReservationTest {

    private final String name = "한다";
    private final LocalDate date = LocalDate.now().plusMonths(1);
    private final ReservationDate reservationDate = ReservationDate.create(date);
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

//    @Test
//    @DisplayName("과거 날짜로 예약 생성 시 예외 발생한다.")
//    void create_before_now() {
//        // given
//        ReservationDate pastDate = ReservationDate.create(LocalDate.now().minusDays(1));
//
//        // when & then
//        assertThatThrownBy(() -> Reservation.create(name, pastDate, reservationTime, theme))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("과거 날짜는 등록할 수 없습니다.");
//    }

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
}
