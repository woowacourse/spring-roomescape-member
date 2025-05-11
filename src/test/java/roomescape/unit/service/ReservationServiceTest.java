package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationService;
import roomescape.unit.repository.FakeMemberRepository;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;
import roomescape.unit.repository.FakeThemeRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationServiceTest {

    private ReservationService reservationService;

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    private LoginMember loginMember;

    @BeforeEach
    void setup() {
        initReservationService();
        initLoginMember();
    }

    @Test
    void 예약을_추가하고_조회할_수_있다() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        CreateReservationRequest request = new CreateReservationRequest(LocalDate.now().plusDays(1L), reservationTimeId,
                themeId);

        // when
        reservationService.addReservation(request, loginMember);

        //then
        assertThat(reservationService.findAll()).hasSize(1);
    }

    @Test
    void 이전_날짜에_예약할_수_없다() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate yesterday = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new CreateReservationRequest(yesterday, reservationTimeId, themeId), loginMember))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은날짜일시_이전_시간에_예약할_수_없다() {
        // given
        ReservationTime pastTime = new ReservationTime(LocalTime.now().minusMinutes(1L));
        long pastTimeId = reservationTimeRepository.add(pastTime).getId();

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate today = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new CreateReservationRequest(today, pastTimeId, themeId), loginMember))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이후_날짜에_예약할_수_있다() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate today = LocalDate.now();

        // when & then
        assertThatCode(() -> reservationService.addReservation(
                new CreateReservationRequest(today, reservationTimeId, themeId), loginMember))
                .doesNotThrowAnyException();
    }

    @Test
    void 같은날짜일시_이후_시간_예약할_수_있다() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate today = LocalDate.now();

        // when & then
        assertThatCode(() -> reservationService.addReservation(
                new CreateReservationRequest(today, reservationTimeId, themeId), loginMember))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약을_삭제하고_조회할_수_있다() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        CreateReservationRequest request = new CreateReservationRequest(
                LocalDate.now().plusDays(1L), reservationTimeId, themeId);

        // when
        Reservation reservation = reservationService.addReservation(request, loginMember);
        int beforeAddSize = reservationService.findAll().size();
        reservationService.deleteReservation(reservation.getId());
        int afterDeleteSize = reservationService.findAll().size();

        //then
        assertThat(beforeAddSize).isEqualTo(1);
        assertThat(afterDeleteSize).isEqualTo(0);
    }

    @Test
    void 중복_예약은_불가능하다() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        reservationService.addReservation(
                new CreateReservationRequest(LocalDate.now(), reservationTimeId, themeId), loginMember);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new CreateReservationRequest(LocalDate.now(), reservationTimeId, themeId), loginMember))
                .isInstanceOf(InvalidReservationException.class);
    }

    @Test
    void 존재하지_않는_예약을_조회시_예외가_발생한다() {
        assertThatThrownBy(() -> reservationService.getReservationById(-1L))
                .isInstanceOf(InvalidReservationException.class);
    }

    @Test
    void 필터에_따라_예약을_조회한다() {
        //given
        Member member = memberRepository.add(new Member("훌라", "email@email.com", "password", Role.USER));
        Theme theme = themeRepository.add(new Theme("테마", "설명", "image.png"));
        ReservationTime reservationTime = reservationTimeRepository.add(
                new ReservationTime(LocalTime.now().plusHours(1L)));
        ReservationTime reservationTime2 = reservationTimeRepository.add(
                new ReservationTime(LocalTime.now().plusHours(2L)));
        ReservationTime reservationTime3 = reservationTimeRepository.add(
                new ReservationTime(LocalTime.now().plusHours(3L)));
        LocalDate tomorrow = LocalDate.now().plusDays(1L);
        Reservation reservation1 = new Reservation(member, tomorrow, reservationTime, theme);
        Reservation reservation2 = new Reservation(member, tomorrow, reservationTime2, theme);
        Reservation reservation3 = new Reservation(member, tomorrow, reservationTime3, theme);
        reservationRepository.add(reservation1);
        reservationRepository.add(reservation2);
        reservationRepository.add(reservation3);

        //when
        List<Reservation> reservations = reservationService.findAllByFilter(member.getId(), theme.getId(),
                tomorrow.minusDays(1), tomorrow.plusDays(1));

        //then
        assertThat(reservations).hasSize(3);
    }

    private void initReservationService() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        memberRepository = new FakeMemberRepository();

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository, memberRepository);
    }

    private void initLoginMember() {
        Member beforeAddMember = new Member("Hula", "test@test.com", "test", Role.USER);
        Member member = memberRepository.add(beforeAddMember);
        loginMember = new LoginMember(member.getId(), member.getName(), member.getRole());
    }
}
