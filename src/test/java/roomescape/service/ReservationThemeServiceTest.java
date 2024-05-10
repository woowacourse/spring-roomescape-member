package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTheme;
import roomescape.dto.ReservationThemeRequestDto;
import roomescape.exception.NotRemovableByConstraintException;
import roomescape.exception.WrongStateException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class ReservationThemeServiceTest {

    @Autowired
    private ReservationThemeService reservationThemeService;

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void getAllThemesTest() {
        List<ReservationTheme> reservationThemes = reservationThemeService.getAllThemes();

        assertThat(reservationThemes.size()).isGreaterThan(0);
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void insertThemeTest() {
        ReservationThemeRequestDto reservationThemeRequestDto = new ReservationThemeRequestDto(
                "레벨3 탈출", "우테코 레벨3 탈출", "https://hi.com"
        );
        ReservationTheme reservationTheme = reservationThemeService.insertTheme(reservationThemeRequestDto);

        assertThat(reservationTheme.getName()).isEqualTo("레벨3 탈출");
        assertThat(reservationTheme.getDescription()).isEqualTo("우테코 레벨3 탈출");
        assertThat(reservationTheme.getThumbnail()).isEqualTo("https://hi.com");
    }

    @DisplayName("테마 ID를 이용하여 테마를 삭제한다.")
    @Test
    void deleteThemeTest() {
        ReservationThemeRequestDto reservationThemeRequestDto = new ReservationThemeRequestDto(
                "레벨3 탈출", "우테코 레벨3 탈출", "https://hi.com"
        );
        ReservationTheme reservationTheme = reservationThemeService.insertTheme(reservationThemeRequestDto);

        int sizeBeforeDelete = reservationThemeService.getAllThemes().size();
        assertThatCode(() -> reservationThemeService.deleteTheme(reservationTheme.getId())).doesNotThrowAnyException();
        assertThat(reservationThemeService.getAllThemes().size()).isEqualTo(sizeBeforeDelete - 1);
    }

    @DisplayName("이미 예약이 존재하는 테마는 삭제할 수 없다.")
    @Test
    void deleteInvalidThemeIdTest() {
        assertThatThrownBy(() -> reservationThemeService.deleteTheme(1L))
                .isInstanceOf(NotRemovableByConstraintException.class);
    }

    @DisplayName("지난 일주일간 가장 많이 예약된 테마를 조회한다.")
    @Test
    void getBestThemesTest() {
        List<ReservationTheme> reservationThemes = reservationThemeService.getBestThemes();

        List<Long> bestThemeIds = reservationThemes.stream()
                        .map(ReservationTheme::getId).toList();

        assertThat(bestThemeIds).isEqualTo(List.of(3L,1L,2L));
    }

    @DisplayName("이미 존재하는 테마 이름이면 예외를 발생한다.")
    @Test
    void insertExistNameThemeTest() {
        String name = "testTheme1";
        ReservationThemeRequestDto reservationThemeRequestDto = new ReservationThemeRequestDto(name, "testDesc", "testImg");

        assertThatThrownBy(() -> reservationThemeService.insertTheme(reservationThemeRequestDto))
                .isInstanceOf(WrongStateException.class)
                .hasMessage("이미 존재하는 테마 이름입니다.");
    }
}
