package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("두 예약이 서로 중복된 예약인지 판단한다.")
    @Test
    void isDuplicateReservation() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime time = new ReservationTime(LocalTime.now());
        ReservationTheme theme = new ReservationTheme(1L, "제목", "설명", "썸네일");
        final Member member = new Member(1L, MemberRole.USER, "test", "test", "윓슨", "1111");
        Reservation reservation = new Reservation(member , date, time, theme);

        // when
        boolean isDuplicated = reservation.isDuplicateReservation(reservation);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @DisplayName("날짜가 다르면 중복이 아니다.")
    @Test
    void isNotDuplicate_whenDateIsDifferent() {
        //given
        final LocalDate date1 = LocalDate.now().plusDays(1);
        final LocalDate date2 = LocalDate.now().plusDays(2);
        final ReservationTime time = new ReservationTime(LocalTime.now());
        final ReservationTheme theme = new ReservationTheme(1L, "제목", "설명", "썸네일");
        final Member member = new Member(1L, MemberRole.USER, "test", "test", "윓슨", "1111");
        Reservation reservation1 = new Reservation(member, date1, time, theme);
        Reservation reservation2 = new Reservation(member, date2, time, theme);

        //when
        boolean isDuplicated = reservation1.isDuplicateReservation(reservation2);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @DisplayName("시간이 다르면 중복이 아니다.")
    @Test
    void isNotDuplicate_whenTimeIsDifferent() {
        //given
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTheme theme = new ReservationTheme(1L, "제목", "설명", "썸네일");
        final ReservationTime time1 = new ReservationTime(LocalTime.now());
        final ReservationTime time2 = new ReservationTime(LocalTime.now().minusHours(1));
        final Member member = new Member(1L, MemberRole.USER, "test", "test", "윓슨", "1111");
        Reservation reservation1 = new Reservation(member, date, time1, theme);
        Reservation reservation2 = new Reservation(member, date, time2, theme);

        //when
        boolean isDuplicated = reservation1.isDuplicateReservation(reservation2);

        //then
        assertThat(isDuplicated).isFalse();
    }

}
