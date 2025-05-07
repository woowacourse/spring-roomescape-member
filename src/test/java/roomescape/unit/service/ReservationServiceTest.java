package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlot;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.AddReservationRequest;
import roomescape.dto.request.AvailableTimeRequest;
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

    @BeforeEach
    void setup() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        memberRepository = new FakeMemberRepository();

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository, memberRepository);
    }

    @Test
    void 예약을_추가하고_조회할_수_있다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        ReservationTime reservationTime = new ReservationTime(null, LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        AddReservationRequest request = new AddReservationRequest(LocalDate.now().plusDays(1L), reservationTimeId,
                themeId);

        // when
        reservationService.addReservation(request, member);

        //then
        assertThat(reservationService.allReservations()).hasSize(1);
    }

    @Test
    void 이전_날짜에_예약할_수_없다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        ReservationTime reservationTime = new ReservationTime(null, LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate yesterday = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest(yesterday, reservationTimeId, themeId), member))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은날짜일시_이전_시간에_예약할_수_없다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        ReservationTime pastTime = new ReservationTime(null, LocalTime.now().minusHours(1L));
        long pastTimeId = reservationTimeRepository.add(pastTime).getId();

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate today = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest(today, pastTimeId, themeId), member))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이후_날짜에_예약할_수_있다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        ReservationTime reservationTime = new ReservationTime(null, LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate today = LocalDate.now();

        // when & then
        assertThatCode(() -> reservationService.addReservation(
                new AddReservationRequest(today, reservationTimeId, themeId), member))
                .doesNotThrowAnyException();
    }

    @Test
    void 같은날짜일시_이후_시간_예약할_수_있다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        ReservationTime reservationTime = new ReservationTime(null, LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        LocalDate today = LocalDate.now();

        // when & then
        assertThatCode(() -> reservationService.addReservation(
                new AddReservationRequest(today, reservationTimeId, themeId), member))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약을_삭제하고_조회할_수_있다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        ReservationTime reservationTime = new ReservationTime(null, LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        AddReservationRequest request = new AddReservationRequest(
                LocalDate.now().plusDays(1L), reservationTimeId, themeId);

        // when
        Reservation reservation = reservationService.addReservation(request, member);
        int beforeAddSize = reservationService.allReservations().size();
        reservationService.deleteReservation(reservation.getId());
        int afterDeleteSize = reservationService.allReservations().size();

        //then
        assertThat(beforeAddSize).isEqualTo(1);
        assertThat(afterDeleteSize).isEqualTo(0);
    }

    @Test
    void 중복_예약은_불가능하다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        ReservationTime reservationTime = new ReservationTime(null, LocalTime.now().plusHours(1L));
        long reservationTimeId = reservationTimeRepository.add(reservationTime).getId();

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        reservationService.addReservation(
                new AddReservationRequest(LocalDate.now(), reservationTimeId, themeId), member);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest(LocalDate.now(), reservationTimeId, themeId), member))
                .isInstanceOf(InvalidReservationException.class);
    }

    @Test
    void 선택된_테마와_날짜에_대해서_가능한_시간들을_확인할_수_있다2() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        LocalDate today = LocalDate.now();
        LocalTime firstTime = LocalTime.now().plusHours(1L);
        LocalTime secondTime = LocalTime.now().plusHours(2L);

        ReservationTime reservationTime1 = new ReservationTime(null, firstTime);
        ReservationTime firstReservationTime = reservationTimeRepository.add(reservationTime1);
        ReservationTime reservationTime2 = new ReservationTime(null, secondTime);
        ReservationTime secondReservationTime = reservationTimeRepository.add(reservationTime2);

        Theme theme = new Theme(null, "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        reservationService.addReservation(
                new AddReservationRequest(today, firstReservationTime.getId(), themeId), member);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(today, themeId);
        List<ReservationSlot> reservationSlots = reservationService.getReservationSlots(
                        availableTimeRequest)
                .getReservationSlots();

        //then
        List<ReservationSlot> expected = List.of(new ReservationSlot(1L, firstTime, true),
                new ReservationSlot(2L, secondTime, false));

        assertThat(reservationSlots).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void 최근_일주일을_기준으로_예약이_많은_테마_10개를_확인할_수_있다() {
        // given
        final int THEME_COUNT = 10;
        final int TIME_SLOTS = 6;

        for (int i = 0; i < THEME_COUNT; i++) {
            Theme theme = new Theme(null, "테마" + (i + 1), "테마", "thumbnail");
            themeRepository.add(theme);
        }

        for (int i = 0; i < TIME_SLOTS; i++) {
            LocalTime localTime = LocalTime.of(10 + i, 0);
            reservationTimeRepository.add(new ReservationTime(null, localTime));
        }

        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        // 테마 1 예약 3개
        reservationService.addReservation(new AddReservationRequest(LocalDate.now().plusDays(1), 1L, 1L),
                member);
        reservationService.addReservation(new AddReservationRequest(LocalDate.now().plusDays(1), 2L, 1L),
                member);
        reservationService.addReservation(new AddReservationRequest(LocalDate.now().plusDays(1), 3L, 1L),
                member);

        // 테마 2 예약 2개
        reservationService.addReservation(new AddReservationRequest(LocalDate.now().plusDays(1), 1L, 2L),
                member);
        reservationService.addReservation(new AddReservationRequest(LocalDate.now().plusDays(1), 2L, 2L),
                member);

        // 테마 3 예약 1개
        reservationService.addReservation(new AddReservationRequest(LocalDate.now().plusDays(1), 1L, 3L),
                member);

        // when
        List<Theme> themeRanking = reservationService.getRankingThemes(LocalDate.now().plusDays(6));

        // then
        assertAll(() -> {
            assertThat(themeRanking.getFirst().getId()).isEqualTo(1L);
            assertThat(themeRanking.get(1).getId()).isEqualTo(2L);
            assertThat(themeRanking.get(2).getId()).isEqualTo(3L);
        });
    }

    @Test
    void 존재하지_않는_예약을_조회시_예외가_발생한다() {
        assertThatThrownBy(() -> reservationService.getReservationById(-1L))
                .isInstanceOf(InvalidReservationException.class);
    }
}
