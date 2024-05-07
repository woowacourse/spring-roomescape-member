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
import roomescape.util.ReservationTimeFixture;
import roomescape.util.ThemeFixture;

class ReservationTest {

    @Nested
    class createReservation {

        @Test
        @DisplayName("예약 객체 생성 시 예약자 명이 공백인 경우 예외를 반환한다.")
        void createReservation_WhenNameIsBlank() {
            assertThatThrownBy(
                    () -> Reservation.of(
                            1L,
                            null,
                            LocalDate.parse("2024-02-02"),
                            ReservationTimeFixture.getOne(),
                            ThemeFixture.getOne()))
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
                            ReservationTimeFixture.getOne(),
                            ThemeFixture.getOne()))
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
                            ReservationTimeFixture.getOne(),
                            ThemeFixture.getOne()))
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
                            ThemeFixture.getOne()))
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
                            ReservationTimeFixture.getOne(),
                            null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 생성 시 예약 테마는 필수입니다.");
        }
    }
}
