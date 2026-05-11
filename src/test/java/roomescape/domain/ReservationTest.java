package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public class ReservationTest {

    @Test
    public void 사용자_이름은_2자_이상_20자_이하를_가진다() {
        Theme theme = new Theme(1L, "붉은 요람", "아이의 울음소리가 멈추면, 비로소 당신의 비명이 시작됩니다.", "https://fake.html") {
        };
        int hour = 12;
        int minute = 0;
        ReservationTime reservationTime = new ReservationTime((long) 0, LocalTime.of(hour, minute));
        Reservation reservation = new Reservation(1L, "홍길동", LocalDate.now(),
                reservationTime,
                theme);

        int size = 3;
        int nameSize = reservation.getName().length();

        Assertions.assertEquals(size, nameSize);
    }

    @Test
    public void 사용자_이름이_20자_초과_될_경우_예외가_발생한다() {
        Theme theme = new Theme(1L, "붉은 요람", "아이의 울음소리가 멈추면, 비로소 당신의 비명이 시작됩니다.", "https://fake.html") {
        };
        ReservationTime reservationTime = new ReservationTime((long) 0, LocalTime.of(12, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Reservation(1L, "홍길동란ㅇ렁나ㅓ너러안너러아러니아러니더리너디러니더라어니다ㅓ리너디ㅓ아러다러다러나더라어라더아러다어라더", LocalDate.now(),
                        reservationTime,
                        theme));
    }

    @Test
    public void 사용자_이름이_2자_미만_일_경우_예외가_발생한다() {
        Theme theme = new Theme(1L, "붉은 요람", "아이의 울음소리가 멈추면, 비로소 당신의 비명이 시작됩니다.", "https://fake.html") {
        };
        ReservationTime reservationTime = new ReservationTime((long) 0, LocalTime.of(12, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new Reservation(1L, "단", LocalDate.now(),
                        reservationTime,
                        theme));
    }
}
