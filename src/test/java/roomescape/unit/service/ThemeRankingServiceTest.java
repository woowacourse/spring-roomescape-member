package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeRankingService;
import roomescape.service.ThemeService;
import roomescape.unit.repository.FakeMemberRepository;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;
import roomescape.unit.repository.FakeThemeRepository;

public class ThemeRankingServiceTest {

    private ReservationService reservationService;
    private ThemeRankingService themeRankingService;

    private ThemeRepository themeRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository(reservationRepository);
        themeRepository = new FakeThemeRepository(reservationRepository);
        memberRepository = new FakeMemberRepository();

        MemberService memberService = new MemberService(memberRepository);
        ThemeService themeService = new ThemeService(themeRepository);
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);
        reservationService = new ReservationService(reservationTimeService, themeService, memberService,
                reservationRepository);
        themeRankingService = new ThemeRankingService(reservationService);
    }

    @Test
    void 최근_일주일을_기준으로_예약이_많은_테마_10개를_확인할_수_있다() {
        // given
        final int THEME_COUNT = 10;
        final int TIME_SLOTS = 6;

        for (int i = 0; i < THEME_COUNT; i++) {
            Theme theme = new Theme("테마" + (i + 1), "테마", "thumbnail");
            themeRepository.add(theme);
        }

        for (int i = 0; i < TIME_SLOTS; i++) {
            LocalTime localTime = LocalTime.of(10 + i, 0);
            reservationTimeRepository.add(new ReservationTime(localTime));
        }

        Member beforeAddMember = new Member("Hula", "test@test.com", "test", Role.USER);
        Member member = memberRepository.add(beforeAddMember);
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole());

        // 테마 1 예약 3개
        reservationService.addReservation(new CreateReservationRequest(LocalDate.now().plusDays(1), 1L, 1L),
                loginMember);
        reservationService.addReservation(new CreateReservationRequest(LocalDate.now().plusDays(1), 2L, 1L),
                loginMember);
        reservationService.addReservation(new CreateReservationRequest(LocalDate.now().plusDays(1), 3L, 1L),
                loginMember);

        // 테마 2 예약 2개
        reservationService.addReservation(new CreateReservationRequest(LocalDate.now().plusDays(1), 1L, 2L),
                loginMember);
        reservationService.addReservation(new CreateReservationRequest(LocalDate.now().plusDays(1), 2L, 2L),
                loginMember);

        // 테마 3 예약 1개
        reservationService.addReservation(new CreateReservationRequest(LocalDate.now().plusDays(1), 1L, 3L),
                loginMember);

        // when
        List<Theme> themeRanking = themeRankingService.getRankingThemes(LocalDate.now().plusDays(6));

        // then
        assertAll(() -> {
            assertThat(themeRanking.getFirst().getId()).isEqualTo(1L);
            assertThat(themeRanking.get(1).getId()).isEqualTo(2L);
            assertThat(themeRanking.get(2).getId()).isEqualTo(3L);
        });
    }
}
