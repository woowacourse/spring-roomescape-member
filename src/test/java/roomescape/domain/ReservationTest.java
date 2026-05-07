package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;

class ReservationTest {

    private static final ReservationTime TIME = new ReservationTime(1L, "12:00");
    private static final Theme THEME = new Theme(1L, new ThemeName("테마"), "설명", ThemeImageUrl.defaultImageUrl());

    @Test
    void 아이디가_같으면_동일한_객체이다() {
        // given
        Reservation reservation = reservation().withId(1L);
        Reservation sameIdReservation = reservation().withId(1L);

        // when & then
        assertThat(reservation).isEqualTo(sameIdReservation);
    }

    @Test
    void 아이디가_다르면_다른_객체이다() {
        // given
        Reservation reservation = reservation().withId(1L);
        Reservation differentIdReservation = reservation().withId(2L);

        // when & then
        assertThat(reservation).isNotEqualTo(differentIdReservation);
    }

    @Test
    void 아이디가_null이면_다른_객체이다() {
        // given
        Reservation one = reservation();
        Reservation other = reservation();

        // when & then
        assertThat(one).isNotEqualTo(other);
    }

    @Test
    void withId로_아이디를_부여한_새로운_예약을_생성한다() {
        // given
        Reservation reservation = reservation();

        // when
        Reservation saved = reservation.withId(1L);

        // then
        assertThat(saved.getId()).isEqualTo(1L);
    }

    private Reservation reservation() {
        return new Reservation(
            null,
            new MemberName("n"),
            new ReservationLocalDate(LocalDate.now().plusDays(1)),
            TIME,
            THEME
        );
    }
}