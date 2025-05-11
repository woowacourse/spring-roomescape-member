package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.Member;

class ReservationSearchFilterTest {

    private List<Reservation> reservations;

    @BeforeEach
    void setUp() {
        ReservationTime time = new ReservationTime(1L, LocalTime.MIN);

        Theme theme1 = new Theme(1L, "테마1", "설명1", "썸네일1");
        Theme theme2 = new Theme(2L, "테마2", "설명2", "썸네일2");
        Theme theme3 = new Theme(3L, "테마3", "설명3", "썸네일3");

        Member member1 = new Member(1L, "회원1", "email1@email.com", "비밀번호1");
        Member member2 = new Member(2L, "회원2", "email2@email.com", "비밀번호2");
        Member member3 = new Member(3L, "회원3", "email3@email.com", "비밀번호3");

        reservations = Arrays.asList(
                new Reservation(1L, LocalDate.of(2024, 1, 10), time, theme1, member2),
                new Reservation(2L, LocalDate.of(2024, 1, 15), time, theme1, member3),
                new Reservation(3L, LocalDate.of(2024, 1, 20), time, theme2, member1),
                new Reservation(4L, LocalDate.of(2024, 1, 25), time, theme3, member1),
                new Reservation(5L, LocalDate.of(2024, 1, 20), time, theme1, member1),
                new Reservation(6L, LocalDate.of(2024, 1, 30), time, theme2, member2)
        );
    }

    @Test
    @DisplayName("모든 조건을 만족하는 예약만 반환한다")
    void doFilter_whenAllConditionsMatch() {
        // given
        ReservationSearchFilter filter = new ReservationSearchFilter(
                1L,
                1L,
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 25)
        );

        // when
        List<Reservation> result = filter.doFilter(reservations);

        // then
        assertThat(result).hasSize(1);
        Reservation reservation = result.get(0);
        assertThat(reservation.getTheme().getId()).isEqualTo(1L);
        assertThat(reservation.getMember().getId()).isEqualTo(1L);
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2024, 1, 20));
    }

    @Test
    @DisplayName("특정 themeId로 필터링하면 일치하는 예약만 반환한다")
    void doFilter_whenThemeIdMatches() {
        // given
        ReservationSearchFilter filter = new ReservationSearchFilter(1L, null, null, null);

        // when
        List<Reservation> result = filter.doFilter(reservations);

        // then
        assertThat(result).hasSize(3); // themeId가 1인 예약 3개
        assertThat(result).extracting(r -> r.getTheme().getId()).containsOnly(1L);
    }

    @Test
    @DisplayName("특정 memberId로 필터링하면 일치하는 예약만 반환한다")
    void doFilter_whenMemberIdMatches() {
        // given
        ReservationSearchFilter filter = new ReservationSearchFilter(null, 1L, null, null);

        // when
        List<Reservation> result = filter.doFilter(reservations);

        // then
        assertThat(result).hasSize(3); // memberId가 1인 예약 3개
        assertThat(result).extracting(r -> r.getMember().getId()).containsOnly(1L);
    }

    @Test
    @DisplayName("날짜 범위 내의 예약만 반환한다")
    void doFilter_whenDateInRange() {
        // given
        LocalDate from = LocalDate.of(2024, 1, 15);
        LocalDate to = LocalDate.of(2024, 1, 25);
        ReservationSearchFilter filter = new ReservationSearchFilter(null, null, from, to);

        // when
        List<Reservation> result = filter.doFilter(reservations);

        // then
        assertThat(result).hasSize(4);
        assertThat(result).extracting("date")
                .contains(
                        LocalDate.of(2024, 1, 15),
                        LocalDate.of(2024, 1, 20),
                        LocalDate.of(2024, 1, 25)
                );
    }

}
