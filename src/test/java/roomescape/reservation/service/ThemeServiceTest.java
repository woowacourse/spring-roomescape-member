package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.dao.FakeMemberDao;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dao.FakeReservationTimeDao;
import roomescape.reservation.dao.FakeThemeDao;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.DateRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.dto.ThemeResponse;

@DisplayName("테마 로직 테스트")
class ThemeServiceTest {
    ReservationRepository reservationRepository;
    ReservationTimeRepository reservationTimeRepository;
    ThemeRepository themeRepository;
    MemberRepository memberRepository;
    ThemeService themeService;
    ReservationService reservationService;

    Theme theme;

    @BeforeEach
    void setData() {
        memberRepository = new FakeMemberDao();
        reservationRepository = new FakeReservationDao(memberRepository);
        reservationTimeRepository = new FakeReservationTimeDao(reservationRepository);
        themeRepository = new FakeThemeDao(reservationRepository);

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository,
                memberRepository);
        themeService = new ThemeService(themeRepository);

        long id = 1;
        String name = "name";
        String description = "description";
        String thumbnail = "thumbnail";
        Theme savedtheme = new Theme(id, name, description, thumbnail);
        theme = themeRepository.save(savedtheme);
    }

    @DisplayName("테마 조회에 성공한다.")
    @Test
    void findAll() {
        //give & when
        List<ThemeResponse> themes = themeService.findAllThemes();

        //then
        assertAll(
                () -> assertThat(themes).hasSize(1),
                () -> assertThat(themes.get(0).name()).isEqualTo(theme.getName()),
                () -> assertThat(themes.get(0).description()).isEqualTo(theme.getDescription()),
                () -> assertThat(themes.get(0).thumbnail()).isEqualTo(theme.getThumbnail())
        );
    }

    @DisplayName("테마 생성에 성공한다.")
    @Test
    void create() {
        //given
        String name = "name";
        String description = "description";
        String thumbnail = "thumbnail";
        ThemeRequest themeRequest = new ThemeRequest(name, description, thumbnail);

        //when
        ThemeResponse themeResponse = themeService.create(themeRequest);

        //then
        assertAll(
                () -> assertThat(themeResponse.name()).isEqualTo(name),
                () -> assertThat(themeResponse.thumbnail()).isEqualTo(thumbnail),
                () -> assertThat(themeResponse.description()).isEqualTo(description)
        );
    }

    @DisplayName("테마 삭제에 성공한다.")
    @Test
    void delete() {
        //given & when
        themeService.delete(theme.getId());

        //then
        assertThat(themeRepository.findAll()).hasSize(0);
    }

    @DisplayName("존재하지 않는 테마를 삭제할 경우 예외가 발생한다.")
    @Test
    void deleteNotExistTheme() {
        // given & when & then
        assertThatThrownBy(() -> themeService.delete(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인기 테마 찾기에 성공한다.")
    @Test
    void findPopularThemes() {
        //given
        String name2 = "name2";
        String description2 = "description2";
        String thumbnail2 = "thumbnail2";
        themeService.create(new ThemeRequest(name2, description2, thumbnail2));
        reservationTimeRepository.save(new ReservationTime(LocalTime.NOON));

        Member member = memberRepository.save(new Member("name", "email@email.com", "Password", Role.MEMBER));
        ReservationRequest reservationRequest = new ReservationRequest(new DateRequest("2099-04-18"), 1L, theme.getId());
        reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));

        //when
        List<ThemeResponse> themeResponses = themeService.findPopularThemes();

        //then
        assertAll(
                () -> assertThat(themeResponses.size()).isEqualTo(1),
                () -> assertThat(themeResponses.get(0)).isEqualTo(
                        new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail()))
        );
    }
}
