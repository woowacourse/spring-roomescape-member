package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.auth.Role;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ExistedReservationException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.fake.FakeMemberRepository;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.fake.FakeThemeRepository;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();
        memberRepository = new FakeMemberRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository,
                memberRepository);
    }

    @Test
    void 예약을_조회할_수_있다() {
        // given
        // given
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        reservationTimeRepository.create(reservationTime1);
        Theme theme1 = new Theme(1L, "themeName1", "des", "th");
        themeRepository.create(theme1);
        Member member1 = new Member(1L, "포라", "email1@domain.com", "password1", Role.MEMBER);
        memberRepository.save(member1);
        Reservation reservation1 = Reservation.of(null, member1, LocalDate.of(2025, 7, 25),
                reservationTime1, theme1);
        reservationRepository.create(reservation1);
        // when
        List<ReservationResponse> all = reservationService.findReservations(null);

        // then
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).memberName()).isEqualTo("포라");
    }

    @Test
    void 예약을_추가할_수_있다() {
        // given
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        reservationTimeRepository.create(reservationTime1);
        Theme theme1 = new Theme(1L, "themeName1", "des", "th");
        themeRepository.create(theme1);
        Member member1 = new Member(1L, "name1", "email1@domain.com", "password1", Role.MEMBER);
        memberRepository.save(member1);
        Reservation reservation1 = Reservation.of(null, member1, LocalDate.of(2025, 7, 25),
                reservationTime1, theme1);
        // when
        reservationRepository.create(reservation1);

        // then
        List<ReservationResponse> all = reservationService.findReservations(null);
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.getLast().memberName()).isEqualTo("name1");
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // given
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        reservationTimeRepository.create(reservationTime1);
        Theme theme1 = new Theme(1L, "themeName1", "des", "th");
        themeRepository.create(theme1);
        Member member1 = new Member(1L, "name1", "email1@domain.com", "password1", Role.MEMBER);
        memberRepository.save(member1);
        Reservation reservation1 = Reservation.of(null, member1, LocalDate.of(2025, 7, 25),
                reservationTime1, theme1);
        Reservation savedReservation = reservationRepository.create(reservation1);
        // when
        reservationService.delete(savedReservation.getId());

        // then
        List<ReservationResponse> all = reservationService.findReservations(null);
        assertThat(all.size()).isEqualTo(0);
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
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        reservationTimeRepository.create(reservationTime1);
        Theme theme1 = new Theme(1L, "themeName1", "des", "th");
        themeRepository.create(theme1);
        Member member1 = new Member(1L, "name1", "email1@domain.com", "password1", Role.MEMBER);
        memberRepository.save(member1);
        Reservation reservation1 = Reservation.of(null, member1, LocalDate.of(2025, 7, 25),
                reservationTime1, theme1);
        reservationRepository.create(reservation1);
        ReservationCreateRequest request = new ReservationCreateRequest(LocalDate.of(2025, 7, 25), 1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.create(1L, request.timeId(), request.themeId(), request.date()))
                .isInstanceOf(ExistedReservationException.class);
    }
}
