package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.model.*;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("테마 저장")
    @Test
    void save() {
        final ThemeSaveRequest themeSaveRequest = new ThemeSaveRequest("감자", "설명", "섬네일");
        final ThemeResponse themeResponse = themeService.saveTheme(themeSaveRequest);

        assertThat(themeResponse).isEqualTo(new ThemeResponse(themeRepository.findById(themeResponse.id()).get()));
    }

    @DisplayName("테마 조회")
    @Test
    void getThemes() {
        themeRepository.save(new Theme("이름1", "설명1", "섬네일1"));
        themeRepository.save(new Theme("이름2", "설명2", "섬네일2"));
        final List<ThemeResponse> themeResponses = themeService.getThemes();

        assertThat(themeResponses).hasSize(2)
                .containsExactly(
                        new ThemeResponse(themeRepository.findById(1L).get()),
                        new ThemeResponse(themeRepository.findById(2L).get())
                );
    }

    @DisplayName("테마 삭제")
    @Test
    void deleteTheme() {
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "섬네일1"));
        themeService.deleteTheme(theme.getId());

        assertThat(themeRepository.findById(theme.getId())).isEmpty();
    }

    @DisplayName("존재하지 않는 테마 삭제 시 예외 발생")
    @Test
    void deleteNonExistTheme() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @DisplayName("예약이 존재하는 테마 삭제 시 예외 발생")
    @Test
    void deleteReservationExistTheme() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "섬네일1"));
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        reservationRepository.save(new Reservation(member, LocalDate.now().plusMonths(1), reservationTime, theme));

        assertThatThrownBy(() -> themeService.deleteTheme(theme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약이 존재하는 테마는 삭제할 수 없습니다.");
    }

    @DisplayName("인기 테마 조회")
    @Test
    void getPopularThemes() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final List<Theme> themes = Stream.of(
                    new Theme("이름1", "설명1", "섬네일1"),
                    new Theme("이름2", "설명2", "섬네일2"),
                    new Theme("이름3", "설명3", "섬네일3"),
                    new Theme("이름4", "설명4", "섬네일4"))
                .map(themeRepository::save)
                .toList();
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final LocalDate localDate = LocalDate.now().plusWeeks(1);
        reservationRepository.save(new Reservation(member, localDate, reservationTime, themes.get(2)));
        reservationRepository.save(new Reservation(member, localDate.minusDays(1), reservationTime, themes.get(2)));
        reservationRepository.save(new Reservation(member, localDate.minusDays(2), reservationTime, themes.get(2)));
        reservationRepository.save(new Reservation(member, localDate, reservationTime, themes.get(1)));
        reservationRepository.save(new Reservation(member, localDate.minusDays(1), reservationTime, themes.get(1)));
        reservationRepository.save(new Reservation(member, localDate, reservationTime, themes.get(0)));

        final List<ThemeResponse> themeResponses = themeService.getPopularThemes(localDate);
        assertThat(themeResponses)
                .hasSize(3)
                .containsExactly(
                        new ThemeResponse(themes.get(2)),
                        new ThemeResponse(themes.get(1)),
                        new ThemeResponse(themes.get(0))
                );
    }
}
