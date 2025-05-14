package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import roomescape.exception.ExistedException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.member.Member;
import roomescape.member.dao.FakeMemberDao;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.FakeReservationTimeDao;
import roomescape.theme.Theme;
import roomescape.theme.dao.FakeThemeDao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationServiceTest {

    private FakeReservationDao fakeReservationDao;
    private FakeReservationTimeDao fakeReservationTimeDao;
    private FakeThemeDao fakeThemeDao;
    private FakeMemberDao fakeMemberDao;
    private ReservationService reservationService;

    private final ReservationTime fakeReservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime fakeReservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));

    private final Theme theme1 = Theme.of(1L, "themeName1", "des", "th");
    private final Theme theme2 = Theme.of(2L, "themeName2", "des", "th");

    private final Member member1 = Member.of(1L, "포라", "sy@gmail.com", "1234", "USER");
    private final Member member2 = Member.of(2L, "라리사", "lalisa@gmail.com", "1234", "USER");

    private final Reservation fakeReservation1 = Reservation.of(1L, member1, LocalDate.of(2025, 7, 25),
            fakeReservationTime1, theme1);
    private final Reservation fakeReservation2 = Reservation.of(2L, member2, LocalDate.of(2025, 12, 25),
            fakeReservationTime2, theme2);

    @BeforeEach
    void setUp() {
        fakeReservationTimeDao = new FakeReservationTimeDao(fakeReservationTime1, fakeReservationTime2);
        fakeReservationDao = new FakeReservationDao(fakeReservation1, fakeReservation2);
        fakeThemeDao = new FakeThemeDao(theme1, theme2);
        fakeMemberDao = new FakeMemberDao(member1, member2);
        reservationService = new ReservationService(fakeReservationDao, fakeReservationTimeDao, fakeThemeDao, fakeMemberDao);
    }

    @Test
    void 예약을_조회할_수_있다() {
        // given & when
        List<ReservationResponse> all = reservationService.findAll();

        // then
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).member().name()).isEqualTo("포라");
        assertThat(all.get(1).member().name()).isEqualTo("라리사");
    }

    @Test
    void 예약을_추가할_수_있다() {
        // given & when
        ReservationCreateRequest request = new ReservationCreateRequest(LocalDate.now().plusDays(1), 1L, 1L);
        Member member = Member.createWithoutId("포비", "a", "1234", "USER");
        reservationService.createReservation(request, member);
        List<ReservationResponse> all = reservationService.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
        assertThat(all.getLast().member().name()).isEqualTo("포비");
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // given & when
        reservationService.delete(fakeReservation1.getId());
        List<ReservationResponse> all = reservationService.findAll();

        // then
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.getFirst().member().name()).isEqualTo("라리사");
    }

    @Test
    void id에_대한_예약이_없을_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.delete(10L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 중복_예약하면_예외가_발생한다() {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(LocalDate.of(2025, 7, 25), 1L, 1L);
        Member member = Member.createWithoutId("라리사", "lalisa@gmail.com", "1234", "USER");

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request, member))
                .isInstanceOf(ExistedException.class);
        assertThat(reservationService.findAll().size()).isEqualTo(2);
    }
}
