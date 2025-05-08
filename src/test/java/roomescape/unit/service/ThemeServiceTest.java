package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.request.CreateReservationTimeRequest;
import roomescape.dto.request.CreateThemeRequest;
import roomescape.exception.InvalidThemeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;
import roomescape.unit.repository.FakeMemberRepository;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;
import roomescape.unit.repository.FakeThemeRepository;

class ThemeServiceTest {

    private ThemeService themeService;
    private ReservationService reservationService;
    private ReservationTimeService reservationTimeService;

    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        ReservationRepository reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();
        memberRepository = new FakeMemberRepository();

        reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository, memberRepository);
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @Test
    void 테마를_추가할_수_있다() {
        // given
        CreateThemeRequest request = new CreateThemeRequest("방탈출", "게임입니다.", "thumbnail");

        // when
        Theme addedTheme = themeService.addTheme(request);

        //then
        assertThat(addedTheme.getId()).isEqualTo(1L);
    }

    @Test
    void 테마를_조회할_수_있다() {
        // given
        Theme theme = new Theme( "방탈출", "게임입니다.", "thumbnail");
        themeRepository.add(theme);

        // when & then
        assertThat(themeService.findAll()).hasSize(1);
    }

    @Test
    void 테마를_삭제할_수_있다() {
        // given
        Theme theme = new Theme( "방탈출", "게임입니다.", "thumbnail");
        themeRepository.add(theme);

        // when
        themeService.deleteThemeById(1L);

        //then
        assertThat(themeRepository.findAll()).hasSize(0);
    }

    @Test
    void 예약이_존재하는_테마는_삭제할_수_없다() {
        // given
        Member beforeAddMember = new Member( "Hula", "test@test.com", "test", Role.USER);
        Member member = memberRepository.add(beforeAddMember);
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole());

        Theme theme = new Theme( "방탈출", "게임입니다.", "thumbnail");
        Long themeId = themeRepository.add(theme).getId();

        ReservationTime reservationTime = reservationTimeService.addReservationTime(
                new CreateReservationTimeRequest(LocalTime.now()));
        reservationService.addReservation(
                new CreateReservationRequest(LocalDate.now().plusDays(1L), reservationTime.getId(), themeId),
                loginMember);

        // when & then
        assertThatThrownBy(() -> themeService.deleteThemeById(themeId)).isInstanceOf(InvalidThemeException.class);
    }

    @Test
    void 존재하지_않는_테마를_조회시_예외가_발생한다() {
        assertThatThrownBy(() -> themeService.getThemeById(1L))
                .isInstanceOf(InvalidThemeException.class);
    }
}
