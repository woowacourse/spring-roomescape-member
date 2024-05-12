package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.repository.JdbcThemeRepository;

@ActiveProfiles("test")
@SpringBootTest
@Sql("/delete-data.sql")
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;
    @Autowired
    private JdbcThemeRepository themeRepository;
    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;
    @Autowired
    private JdbcReservationRepository reservationRepository;
    @Autowired
    private JdbcMemberRepository memberRepository;

    @Test
    @DisplayName("예약 테마 생성 시 해당 데이터를 반환한다.")
    void createReservationTime() {
        // given
        var request = new CreateThemeRequest("마크", "노력함", "https://asd.com");

        // when & then
        CreateThemeResponse theme = themeService.createTheme(request);

        assertAll(
                () -> assertThat(theme.name()).isEqualTo("마크"),
                () -> assertThat(theme.description()).isEqualTo("노력함"),
                () -> assertThat(theme.thumbnail()).isEqualTo("https://asd.com"));
    }

    @Test
    @DisplayName("예약 테마 목록 조회 시 저장된 예약 테마에 대한 정보를 반환한다.")
    void getThemes() {
        // given
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);

        // when & then
        assertThat(themeService.getThemes())
                .containsExactly(
                        FindThemeResponse.of(Fixture.THEME_1),
                        FindThemeResponse.of(Fixture.THEME_2));
    }

    @Test
    @DisplayName("예약이 많은 상위 10개의 테마에 대한 정보를 반환한다.")
    void getPopularThemes() {
        // given
        memberRepository.save(Fixture.MEMBER_1);
        memberRepository.save(Fixture.MEMBER_2);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);

        // when & then
        assertThat(themeService.getPopularThemes())
                .containsExactlyInAnyOrderElementsOf(List.of(
                        FindPopularThemesResponse.of(Fixture.THEME_2),
                        FindPopularThemesResponse.of(Fixture.THEME_1)));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 테마를 삭제한다.")
    void deleteById() {
        // when & then
        assertThatCode(() -> themeService.deleteById(3L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("해당하는 시간을 사용 중인 예약이 존재할 경우 예외가 발생한다.")
    void deleteById_ifAlreadyUsed_throwException() {
        // given
        memberRepository.save(Fixture.MEMBER_1);
        themeRepository.save(Fixture.THEME_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationRepository.save(Fixture.RESERVATION_1);

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("테마를 사용 중인 예약이 존재합니다.");
    }
}
