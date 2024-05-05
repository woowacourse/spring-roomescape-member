package roomescape.reservation.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.util.DummyDataFixture;

class ReservationTest {

    @Nested
    class createReservation extends DummyDataFixture {

        @Test
        @DisplayName("예약 객체 생성 시 예약자 명이 공백인 경우 예외를 반환한다.")
        void createReservation_WhenNameIsBlank() {
            assertThatThrownBy(
                    () -> new Reservation(
                            1L,
                            null,
                            LocalDate.parse("2024-02-02"),
                            super.getReservationTimeById(1L),
                            super.getThemeById(1L)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약자 명은 공백 문자가 불가능합니다.");
        }

        @Test
        @DisplayName("예약 객체 생성 시 예약자 명이 공백인 경우 예외를 반환한다.")
        void createReservation_WhenNameOverLength() {
            assertThatThrownBy(
                    () -> Reservation.of(
                            1L,
                            "a".repeat(256),
                            LocalDate.parse("2024-02-02"),
                            super.getReservationTimeById(1L),
                            super.getThemeById(1L)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약자 명은 최대 255자까지 입력이 가능합니다.");
        }

        @Test
        @DisplayName("예약 객체 생성 시 예약자 명이 공백인 경우 예외를 반환한다.")
        void createReservation_WhenReservationDateIsNull() {
            assertThatThrownBy(
                    () -> Reservation.of(
                            1L,
                            "몰리",
                            null,
                            super.getReservationTimeById(1L),
                            super.getThemeById(1L)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 생성 시 예약 날짜는 필수입니다.");
        }

        @Test
        @DisplayName("예약 객체 생성 시 예약자 명이 공백인 경우 예외를 반환한다.")
        void createReservation_WhenReservationTimeIsNull() {
            assertThatThrownBy(
                    () -> Reservation.of(
                            1L,
                            "몰리",
                            LocalDate.parse("2024-02-02"),
                            null,
                            super.getThemeById(1L)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 생성 시 예약 시간은 필수입니다.");
        }

        @Test
        @DisplayName("예약 객체 생성 시 예약 테마가 공백인 경우 예외를 반환한다.")
        void createReservation_WhenReservationThemeIsNull() {
            assertThatThrownBy(
                    () -> Reservation.of(
                            1L,
                            "몰리",
                            LocalDate.parse("2024-02-02"),
                            super.getReservationTimeById(1L),
                            null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 생성 시 예약 테마는 필수입니다.");
        }
    }

    @Nested
    class isBeforeDateTimeThanNow extends DummyDataFixture {

        @Test
        @DisplayName("주어진 날짜와 시간보다 예약의 날짜와 시간이 이전인 경우 참을 반환한다.")
        void isBeforeDateTimeThanNow() {
            Reservation reservation = Reservation.of(
                    1L,
                    "아서",
                    LocalDate.parse("2024-04-23"),
                    new ReservationTime(null, LocalTime.parse("10:00")),
                    super.getThemeById(1L));
            assertTrue(reservation.isBeforeDateTimeThanNow(LocalDateTime.parse("2024-05-23T10:15:30")));
        }

        @Test
        @DisplayName("주어진 날짜와 시간보다 예약의 날짜가 이후인 경우 거짓을 반환한다.")
        void isBeforeDateTimeThanNow_WhenDataIsAfter() {
            Reservation reservation = Reservation.of(
                    1L,
                    "아서",
                    LocalDate.parse("2024-04-23"),
                    new ReservationTime(null, LocalTime.parse("10:00")),
                    super.getThemeById(1L));
            assertFalse(
                    reservation.isBeforeDateTimeThanNow(
                            LocalDateTime.of(LocalDate.parse("2024-01-23"), LocalTime.parse("09:00"))));
        }

        @Test
        @DisplayName("주어진 날짜와 시간보다 예약의 시간이 이후인 경우 거짓을 반환한다.")
        void isBeforeDateTimeThanNow_WhenTimeIsAfter() {
            LocalDate sameDate = LocalDate.parse("2024-04-23");
            Reservation reservation = Reservation.of(
                    1L,
                    "아서",
                    sameDate,
                    new ReservationTime(null, LocalTime.parse("10:00")),
                    super.getThemeById(1L));
            assertFalse(reservation.isBeforeDateTimeThanNow(LocalDateTime.of(sameDate, LocalTime.parse("09:00"))));
        }

        @Test
        @DisplayName("주어진 날짜와 시간보다 예약의 날짜와 시간이 동일한 경우 거짓을 반환한다.")
        void isBeforeDateTimeThanNow_WhenDataTimeIsSame() {
            LocalDate sameDate = LocalDate.parse("2024-04-23");
            LocalTime sameTime = LocalTime.parse("10:00");
            Reservation reservation = Reservation.of(
                    1L,
                    "아서",
                    sameDate,
                    new ReservationTime(null, sameTime),
                    super.getThemeById(1L));
            assertFalse(reservation.isBeforeDateTimeThanNow(LocalDateTime.of(sameDate, sameTime)));
        }
    }
}
