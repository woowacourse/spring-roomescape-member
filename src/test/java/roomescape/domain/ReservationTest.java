package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("두 예약이 서로 중복된 예약인지 판단한다.")
    @Test
    void isDuplicateReservation() {
        // given
        LocalDate date = LocalDate.of(2025, 4, 18);
        Member member1 = new Member(1L, "제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        Member member2 = new Member(2L, "플린트", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        ReservationTime time = new ReservationTime("08:30");
        ReservationTheme theme = new ReservationTheme("제목", "설명", "썸네일");
        Reservation reservation = new Reservation(date, member1, time, theme);
        Reservation duplicated = new Reservation(date, member2, time, theme);

        // when
        boolean isDuplicated = reservation.isDuplicateReservation(duplicated);

        // then
        assertThat(isDuplicated).isTrue();
    }

    @DisplayName("날짜가 다르면 중복이 아니다.")
    @Test
    void isNotDuplicate_whenDateIsDifferent() {
        //given
        LocalDate date1 = LocalDate.of(2025, 4, 18);
        LocalDate date2 = LocalDate.of(2025, 4, 19);
        Member member1 = new Member(1L, "제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        Member member2 = new Member(2L, "플린트", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        ReservationTime time = new ReservationTime("08:30");
        ReservationTheme theme = new ReservationTheme("제목", "설명", "썸네일");
        Reservation reservation1 = new Reservation(date1, member1, time, theme);
        Reservation reservation2 = new Reservation(date2, member2, time, theme);

        //when
        boolean isDuplicated = reservation1.isDuplicateReservation(reservation2);

        //then
        assertThat(isDuplicated).isFalse();
    }

    @DisplayName("시간이 다르면 중복이 아니다.")
    @Test
    void isNotDuplicate_whenTimeIsDifferent() {
        //given
        LocalDate date = LocalDate.of(2025, 4, 18);
        Member member1 = new Member(1L, "제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        Member member2 = new Member(2L, "플린트", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        ReservationTheme theme = new ReservationTheme("제목", "설명", "썸네일");
        ReservationTime time1 = new ReservationTime("08:30");
        ReservationTime time2 = new ReservationTime("09:00");
        Reservation reservation1 = new Reservation(date, member1, time1, theme);
        Reservation reservation2 = new Reservation(date, member2, time2, theme);

        //when
        boolean isDuplicated = reservation1.isDuplicateReservation(reservation2);

        //then
        assertThat(isDuplicated).isFalse();
    }
}
