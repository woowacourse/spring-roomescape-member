package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @DisplayName("예약 요청 중 이름이 비어있는 경우 예약이 성립하지 않는다.")
    @Test
    void testNameNotEmpty() {
        //given, when, then
        assertThrows(IllegalArgumentException.class,
                () -> {
                    new Reservation(1L, "", LocalDate.now(), new TimeSlot("23:59"));
                });
    }

    @DisplayName("예약 요청 중 날짜가 비어있는 경우 예약이 성립하지 않는다.")
    @Test
    void testDateNotEmpty() {
        //given, when, then
        assertThrows(IllegalArgumentException.class,
                () -> {
                    new Reservation(1L, "Ash", null, new TimeSlot("23:59"));
                });
    }

    @DisplayName("예약 요청 중 시간이 비어있는 경우 예약이 성립하지 않는다.")
    @Test
    void testTimeNotEmpty() {
        //given, when, then
        assertThrows(IllegalArgumentException.class,
                () -> {
                    new Reservation(1L, "Ash", LocalDate.now(), null);
                });
    }

    @DisplayName("예약 요청 중 이전 날짜를 선택할 경우 예약이 성립하지 않는다.")
    @Test
    void testValidDate() {
        //given, when, then
        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1L, "Ash", LocalDate.of(2024, 1, 1), new TimeSlot("23:59"));
        });
    }

    @DisplayName("예약 요청 중 이전 시간을 선택할 경우 예약이 성립하지 않는다.")
    @Test
    void testValidTime() {
        //given, when, then
        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(1L, "Ash", LocalDate.now(), new TimeSlot("00:00"));
        });
    }
}
