package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeJdbcRepositoryTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("중복된 테마 추가가 불가능한 지 확인한다.")
    void checkDuplicatedTheme() {
        //given
        Theme theme1 = new Theme("테마명", "테마 설명1", "테마 이미지1");
        Theme theme2 = new Theme("테마명", "테마 설명2", "테마 이미지2");
        themeRepository.save(theme1);

        //when & then
        assertThatThrownBy(() ->
                themeRepository.save(theme2)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 동일한 이름을 가진 테마가 존재합니다.");
    }

    @Test
    @DisplayName("삭제하려는 테마에 예약이 존재한다면 삭제가 불가능한지 확인한다.")
    void checkDeleteReservedTheme() {
        //given
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        reservationRepository.save(new Reservation(
                new Member(Role.ADMIN, "admin1@email.com", "password"),
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme
        ));

        //when & then
        assertThatThrownBy(() ->
                themeRepository.deleteById(1L)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현 테마에 예약이 존재합니다.");
    }

    @Test
    @Sql("/data.sql")
    @DisplayName("주간 인기 테마 목록이 10개인지 확인한다.")
    void checkWeeklyHotThemesSize() {
        List<Theme> themes = themeRepository.findWeeklyHotThemes();
        assertThat(themes.size()).isEqualTo(10);
    }
}
